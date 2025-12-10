package ProyUniversidad.UVI.models.matricula.services;

import ProyUniversidad.UVI.dtos.matricula.MatriculaPeticionDTO;
import ProyUniversidad.UVI.models.academico.models.Curso;
import ProyUniversidad.UVI.models.academico.models.Horario;
import ProyUniversidad.UVI.models.academico.models.Seccion;
import ProyUniversidad.UVI.models.academico.service.SeccionService;
import ProyUniversidad.UVI.models.historial.services.AuditoriaService;
import ProyUniversidad.UVI.models.historial.services.HistorialService;
import ProyUniversidad.UVI.models.matricula.models.Cuota;
import ProyUniversidad.UVI.models.matricula.models.Matricula;
import ProyUniversidad.UVI.models.matricula.repositorys.CuotaRepository;
import ProyUniversidad.UVI.models.matricula.repositorys.MatriculaRepository;
import ProyUniversidad.UVI.models.usuario.models.Alumno;
import ProyUniversidad.UVI.models.usuario.repositorys.AlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final SeccionService seccionService;
    private final HistorialService historialService;
    private final AlumnoRepository alumnoRepository;
    private final AuditoriaService auditoriaService;
    private final PagoServicio pagoServicio;
    private final CuotaRepository cuotaRepository; // Para generar las pensiones

    @Transactional(transactionManager = "postgresqlTransactionManager")
    public Matricula registrarMatricula(MatriculaPeticionDTO peticion) {
        
        // 1. Obtener Alumno y Validar existencia
        Alumno alumno = alumnoRepository.findById(peticion.getAlumnoId())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        // 2. REGLA: Validar que haya pagado el "Derecho de Matrícula" previamente
        if (!pagoServicio.tieneMatriculaHabilitada(peticion.getAlumnoId(), peticion.getCicloId())) {
             throw new RuntimeException("No estás habilitado. Debes pagar primero el Derecho de Matrícula.");
        }

        // 3. REGLA: Validar Turno de Matrícula
        if (alumno.getFechaTurnoMatricula() != null && LocalDateTime.now().isBefore(alumno.getFechaTurnoMatricula())) {
            auditoriaService.registrarEvento(alumno.getUsername(), "INTENTO_FALLIDO", "Intento de matrícula antes de turno");
            throw new RuntimeException("Aún no es su turno de matrícula. Su turno inicia el: " + alumno.getFechaTurnoMatricula());
        }

        // 4. Validar si ya está matriculado en este ciclo
        if (matriculaRepository.findByAlumnoIdAndCicloId(peticion.getAlumnoId(), peticion.getCicloId()).isPresent()) {
            throw new RuntimeException("El alumno ya se encuentra matriculado en este ciclo.");
        }

        // 5. Obtener secciones solicitadas
        List<Seccion> secciones = peticion.getSeccionesIds().stream()
                .map(seccionService::obtenerPorId)
                .collect(Collectors.toList());

        // 6. Validar Cruce de Horarios (Global)
        validarCruceHorarios(secciones);
        
        List<Matricula.DetalleCursoMatricula> detalles = new ArrayList<>();
        int totalCreditos = 0;

        // 7. Procesar cada sección individualmente
        for (Seccion seccion : secciones) {
            Curso curso = seccion.getCurso();

            // REGLA: Validar Nivel del Curso (No adelantar más de 1 ciclo)
            int cicloAlumno = alumno.getCicloActual() != null ? alumno.getCicloActual() : 1;
            int cicloCurso = curso.getCiclo() != null ? curso.getCiclo() : 1;

            if (cicloCurso > cicloAlumno + 1) {
                throw new RuntimeException(
                    "No puedes matricularte en '" + curso.getNombre() + 
                    "' (Ciclo " + cicloCurso + ") porque estás en el Ciclo " + cicloAlumno + 
                    ". Solo puedes adelantar cursos de hasta 1 ciclo superior."
                );
            }

            // REGLA: Prerrequisitos
            if (!historialService.cumplePrerrequisitos(peticion.getAlumnoId(), curso)) {
                throw new RuntimeException("No cumple los prerrequisitos para el curso: " + curso.getNombre());
            }

            // REGLA: Curso ya aprobado
            if (historialService.cursoYaAprobado(peticion.getAlumnoId(), curso.getId())) {
                throw new RuntimeException("El curso ya fue aprobado anteriormente: " + curso.getNombre());
            }

            // REGLA: Correquisitos
            if (curso.getCorrequisitos() != null && !curso.getCorrequisitos().isEmpty()) {
                for (Curso correquisito : curso.getCorrequisitos()) {
                    boolean aprobado = historialService.cursoYaAprobado(peticion.getAlumnoId(), correquisito.getId());
                    // Verificar si el correquisito está en la lista actual de matrícula
                    boolean matriculandoAhora = secciones.stream()
                            .anyMatch(s -> s.getCurso().getId().equals(correquisito.getId()));

                    if (!aprobado && !matriculandoAhora) {
                        throw new RuntimeException("Debe matricularse simultáneamente en el correquisito: " 
                                + correquisito.getNombre() + " o haberlo aprobado previamente.");
                    }
                }
            }

            // REGLA: Vacantes (Reserva con concurrencia optimista)
            seccionService.incrementarVacantesOcupadas(seccion.getId());

            // Snapshot para MongoDB
            List<String> horariosTxt = seccion.getHorarios().stream()
                    .map(h -> h.getDiaSemana() + " " + h.getHoraInicio() + "-" + h.getHoraFin())
                    .collect(Collectors.toList());

            detalles.add(Matricula.DetalleCursoMatricula.builder()
                    .cursoCodigo(curso.getCodigo())
                    .cursoNombre(curso.getNombre())
                    .creditos(curso.getCreditos())
                    .seccionId(seccion.getId())
                    .seccionCodigo(seccion.getCodigo())
                    .horariosTexto(horariosTxt)
                    .build());
            
            totalCreditos += curso.getCreditos();
        }

        // --- CÁLCULO FINANCIERO ---
        // Costo Total del Ciclo = Mensualidad * 5 meses
        double costoCredito = 40.00; // Configurable
        double mensualidad = totalCreditos * costoCredito;
        double costoTotalCiclo = mensualidad * 5;

        // 8. Guardar Matrícula (Estado MATRICULADO directamente)
        Matricula matricula = Matricula.builder()
                .alumnoId(alumno.getId())
                .codigoUniversitario(alumno.getCodigoUniversitario()) 
                .nombreAlumno(alumno.getNombreCompleto())             
                .cicloId(peticion.getCicloId())
                .cursos(detalles)
                .totalCreditos(totalCreditos)
                .costoTotal(costoTotalCiclo)
                .fechaMatricula(LocalDateTime.now())
                .estado("MATRICULADO") 
                .codigoComprobante(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();

        Matricula guardada = matriculaRepository.save(matricula);

        // 9. Generar Pensiones (Cuotas Mensuales)
        generarCuotas(guardada, mensualidad);

        // 10. Auditoría final
        auditoriaService.registrarEvento(
            alumno.getUsername(), 
            "MATRICULA_EXITOSA", 
            "Matrícula confirmada. Créditos: " + totalCreditos + ". Código: " + guardada.getCodigoComprobante()
        );

        return guardada;
    }

    private void generarCuotas(Matricula matricula, double montoMensualidad) {
        for (int i = 1; i <= 5; i++) {
            Cuota cuota = Cuota.builder()
                    .matriculaId(matricula.getId())
                    .alumnoId(matricula.getAlumnoId())
                    .numero(i)
                    .monto(Math.round(montoMensualidad * 100.0) / 100.0) // Redondeo a 2 decimales
                    .vencimiento(LocalDate.now().plusMonths(i))    // Vencimiento escalonado
                    .estado("PENDIENTE")
                    .build();
            
            cuotaRepository.save(cuota);
        }
    }

    private void validarCruceHorarios(List<Seccion> secciones) {
        for (int i = 0; i < secciones.size(); i++) {
            for (int j = i + 1; j < secciones.size(); j++) {
                if (hayCruce(secciones.get(i), secciones.get(j))) {
                    throw new RuntimeException("Existe cruce de horarios entre " +
                            secciones.get(i).getCurso().getNombre() + " y " +
                            secciones.get(j).getCurso().getNombre());
                }
            }
        }
    }

    private boolean hayCruce(Seccion s1, Seccion s2) {
        for (Horario h1 : s1.getHorarios()) {
            for (Horario h2 : s2.getHorarios()) {
                if (h1.getDiaSemana().equals(h2.getDiaSemana())) {
                    if (h1.getHoraInicio().isBefore(h2.getHoraFin()) && 
                        h1.getHoraFin().isAfter(h2.getHoraInicio())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Este método ya no se usa directamente para el total, 
    // pero lo dejamos por compatibilidad si algún test lo llama
    private Double calcularCosto(int creditos) {
        return creditos * 40.00; 
    }
}