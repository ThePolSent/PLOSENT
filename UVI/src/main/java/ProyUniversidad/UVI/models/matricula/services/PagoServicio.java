package ProyUniversidad.UVI.models.matricula.services;

import ProyUniversidad.UVI.models.matricula.models.Matricula;
import ProyUniversidad.UVI.models.matricula.models.Pago;
import ProyUniversidad.UVI.models.matricula.repositorys.MatriculaRepository;
import ProyUniversidad.UVI.models.matricula.repositorys.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagoServicio {

    private final PagoRepository pagoRepository;
    private final MatriculaRepository matriculaRepository; 


    public boolean tieneMatriculaHabilitada(Long alumnoId, Long cicloId) {
        return pagoRepository.existsByAlumnoIdAndCicloIdAndConcepto(alumnoId, cicloId, "DERECHO_MATRICULA");
    }

    public void pagarDerechoMatricula(Long alumnoId, Long cicloId) {
        if (tieneMatriculaHabilitada(alumnoId, cicloId)) {
            throw new RuntimeException("Ya has pagado el derecho de matrícula para este ciclo.");
        }

        Pago pago = Pago.builder()
                .alumnoId(alumnoId)
                .cicloId(cicloId)
                .concepto("DERECHO_MATRICULA")
                .monto(350.00)
                .fechaPago(LocalDateTime.now())
                .metodoPago("TARJETA_ONLINE")
                .build();
        
        pagoRepository.save(pago);
    }

    public Matricula procesarPago(String matriculaId) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada"));

        if ("PAGADO".equals(matricula.getEstado())) {
            throw new RuntimeException("Esta matrícula ya ha sido pagada.");
        }

        if ("ANULADO".equals(matricula.getEstado())) {
            throw new RuntimeException("No se puede pagar una matrícula anulada.");
        }

        matricula.setEstado("PAGADO");
        Matricula matriculaGuardada = matriculaRepository.save(matricula);

        Pago pago = Pago.builder()
                .alumnoId(matricula.getAlumnoId())
                .cicloId(matricula.getCicloId())
                .concepto("MENSUALIDAD_MATRICULA")
                .monto(matricula.getCostoTotal())
                .fechaPago(LocalDateTime.now())
                .metodoPago("TARJETA_ONLINE")
                .build();
        pagoRepository.save(pago);

        return matriculaGuardada;
    }
}