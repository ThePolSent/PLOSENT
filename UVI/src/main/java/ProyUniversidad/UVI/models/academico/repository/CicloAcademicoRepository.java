package ProyUniversidad.UVI.models.academico.repository;

import ProyUniversidad.UVI.models.academico.models.CicloAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CicloAcademicoRepository extends JpaRepository<CicloAcademico, Long> {
  Optional<CicloAcademico> findByCodigo(String codigo);

  List<CicloAcademico> findByActivoTrue();

  Optional<CicloAcademico> findFirstByActivoTrueOrderByFechaInicioDesc();
}