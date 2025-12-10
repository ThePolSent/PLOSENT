package ProyUniversidad.UVI.models.academico.repository;

import ProyUniversidad.UVI.models.academico.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
  Optional<Curso> findByCodigo(String codigo);

  List<Curso> findByActivoTrue();

  List<Curso> findByDepartamento(String departamento);

  boolean existsByCodigo(String codigo);
}