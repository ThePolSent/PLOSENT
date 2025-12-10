package ProyUniversidad.UVI.models.historial.services;

import ProyUniversidad.UVI.models.historial.models.LogAuditoria;
import ProyUniversidad.UVI.models.historial.repositorys.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final LogAuditoriaRepository logRepository;

    @Transactional(transactionManager = "postgresqlTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public void registrarEvento(String usuario, String accion, String detalles) {
        try {
            LogAuditoria log = LogAuditoria.builder()
                    .usuario(usuario)
                    .accion(accion)
                    .detalles(detalles)
                    .fechaHora(LocalDateTime.now())
                    .build();
            
            logRepository.save(log);
        } catch (Exception e) {
            System.err.println("Error al guardar auditoría: " + e.getMessage());
        }
    }
}