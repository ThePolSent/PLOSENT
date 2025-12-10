package ProyUniversidad.UVI.models.historial.repositorys;

import ProyUniversidad.UVI.models.historial.models.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
}