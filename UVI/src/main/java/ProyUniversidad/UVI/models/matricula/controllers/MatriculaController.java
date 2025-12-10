package ProyUniversidad.UVI.models.matricula.controllers;

import ProyUniversidad.UVI.dtos.matricula.DeudaDTO;
import ProyUniversidad.UVI.dtos.matricula.MatriculaPeticionDTO;
import ProyUniversidad.UVI.dtos.matricula.PagoTarjetaDTO;
import ProyUniversidad.UVI.models.academico.service.CicloAcademicoService;
import ProyUniversidad.UVI.models.matricula.models.Cuota;
import ProyUniversidad.UVI.models.matricula.models.Matricula;
import ProyUniversidad.UVI.models.matricula.models.Pago;
import ProyUniversidad.UVI.models.matricula.repositorys.CuotaRepository;
import ProyUniversidad.UVI.models.matricula.repositorys.MatriculaRepository;
import ProyUniversidad.UVI.models.matricula.repositorys.PagoRepository;
import ProyUniversidad.UVI.models.matricula.services.MatriculaService;
import ProyUniversidad.UVI.models.matricula.services.PagoServicio;
import ProyUniversidad.UVI.models.usuario.repositorys.UsuarioRepository;
import ProyUniversidad.UVI.utils.GeneradorComprobante;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final MatriculaRepository matriculaRepository;
    private final GeneradorComprobante generadorComprobante;
    private final PagoServicio pagoServicio;
    private final CicloAcademicoService cicloService;
    private final UsuarioRepository usuarioRepository;
    private final CuotaRepository cuotaRepository;
    private final PagoRepository pagoRepository;

    @PostMapping
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<Matricula> registrarMatricula(@Valid @RequestBody MatriculaPeticionDTO peticion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.registrarMatricula(peticion));
    }

    @GetMapping("/{id}/comprobante")
    @PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    public ResponseEntity<byte[]> descargarComprobante(@PathVariable String id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada"));

        byte[] pdfBytes = generadorComprobante.generarPDF(matricula);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "comprobante_" + matricula.getCodigoComprobante() + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/mi-horario")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<?> obtenerMiHorario(Authentication auth) {
        String username = auth.getName();
        Long alumnoId = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")).getId();

        Long cicloId = cicloService.obtenerCicloActual().getId();

        return matriculaRepository.findByAlumnoIdAndCicloId(alumnoId, cicloId)
                .map(m -> ResponseEntity.ok(m.getCursos())) 
                .orElse(ResponseEntity.ok(List.of()));      
    }


    @GetMapping("/estado-pago")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<Boolean> verificarEstadoPago(Authentication auth) {
        String username = auth.getName();
        Long alumnoId = usuarioRepository.findByUsername(username).orElseThrow().getId();
        Long cicloId = cicloService.obtenerCicloActual().getId();
        
        return ResponseEntity.ok(pagoServicio.tieneMatriculaHabilitada(alumnoId, cicloId));
    }

    @GetMapping("/ya-matriculado")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<Boolean> verificarSiYaEstaMatriculado(Authentication auth) {
        String username = auth.getName();
        Long alumnoId = usuarioRepository.findByUsername(username).orElseThrow().getId();
        Long cicloId = cicloService.obtenerCicloActual().getId();
        
        return ResponseEntity.ok(matriculaRepository.findByAlumnoIdAndCicloId(alumnoId, cicloId).isPresent());
    }

    @PostMapping("/pagar-derecho")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<String> pagarDerecho(Authentication auth) {
        String username = auth.getName();
        Long alumnoId = usuarioRepository.findByUsername(username).orElseThrow().getId();
        Long cicloId = cicloService.obtenerCicloActual().getId();
        
        pagoServicio.pagarDerechoMatricula(alumnoId, cicloId);
        return ResponseEntity.ok("Pago registrado exitosamente. Ahora puedes seleccionar tus cursos.");
    }

    @GetMapping("/deudas")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<List<DeudaDTO>> verDeudas(Authentication auth) {
        String username = auth.getName();
        Long alumnoId = usuarioRepository.findByUsername(username).orElseThrow().getId();
        Long cicloId = cicloService.obtenerCicloActual().getId();

        List<DeudaDTO> deudas = new ArrayList<>();

        if (!pagoServicio.tieneMatriculaHabilitada(alumnoId, cicloId)) {
            deudas.add(DeudaDTO.builder()
                    .id("DERECHO-" + cicloId) 
                    .concepto("Derecho de Matrícula - Ciclo Actual")
                    .monto(350.00)
                    .vencimiento(LocalDate.now()) 
                    .tipo("DERECHO")
                    .build());
        }

        List<Cuota> cuotas = cuotaRepository.findByAlumnoIdAndEstado(alumnoId, "PENDIENTE");
        for (Cuota c : cuotas) {
            deudas.add(DeudaDTO.builder()
                    .id(c.getId())
                    .concepto("Pensión " + c.getNumero() + " - Mensualidad")
                    .monto(c.getMonto())
                    .vencimiento(c.getVencimiento())
                    .tipo("PENSION")
                    .build());
        }

        return ResponseEntity.ok(deudas);
    }

    @PostMapping("/pagar-online")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<?> pagarOnline(@RequestBody PagoTarjetaDTO pago, Authentication auth) {
        String username = auth.getName();
        Long alumnoId = usuarioRepository.findByUsername(username).orElseThrow().getId();

        if (pago.getMatriculaId().startsWith("DERECHO")) {
            String[] parts = pago.getMatriculaId().split("-");
            Long cicloId = Long.parseLong(parts[1]);
            
            pagoServicio.pagarDerechoMatricula(alumnoId, cicloId);
            return ResponseEntity.ok("Derecho de matrícula pagado correctamente.");

        } else {
            Cuota cuota = cuotaRepository.findById(pago.getMatriculaId())
                    .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));
            
            if ("PAGADO".equals(cuota.getEstado())) {
                throw new RuntimeException("Esta cuota ya fue pagada.");
            }

            cuota.setEstado("PAGADO");
            cuotaRepository.save(cuota);

            Pago registroPago = Pago.builder()
                    .alumnoId(alumnoId)
                    .cicloId(null) // Opcional buscarlo
                    .concepto("PENSION_CUOTA_" + cuota.getNumero())
                    .monto(cuota.getMonto())
                    .fechaPago(LocalDateTime.now())
                    .metodoPago("TARJETA_ONLINE")
                    .build();
            pagoRepository.save(registroPago);

            return ResponseEntity.ok("Pensión pagada correctamente.");
        }
    }

    @GetMapping("/actual")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<Matricula> obtenerMatriculaActual(Authentication auth) {
        String username = auth.getName();
        Long alumnoId = usuarioRepository.findByUsername(username).orElseThrow().getId();
        Long cicloId = cicloService.obtenerCicloActual().getId();

        return matriculaRepository.findByAlumnoIdAndCicloId(alumnoId, cicloId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}