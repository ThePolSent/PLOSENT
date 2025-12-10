package ProyUniversidad.UVI.models.usuario.repositorys;

import ProyUniversidad.UVI.models.usuario.models.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {


  Optional<Alumno> findByCodigoUniversitario(String codigoUniversitario);
}
