package ProyUniversidad.UVI.models.academico.repository;

import ProyUniversidad.UVI.models.academico.models.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {

}