package ProyUniversidad.UVI.models.academico.repository;

import ProyUniversidad.UVI.models.academico.models.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
  Optional<Docente> findByUsuarioId(Long usuarioId);

  Optional<Docente> findByCodigoDocente(String codigoDocente);

  List<Docente> findByActivoTrue();

  List<Docente> findByDepartamento(String departamento);
}