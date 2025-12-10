package ProyUniversidad.UVI.models.usuario.repositorys;

import ProyUniversidad.UVI.models.usuario.models.DocenteUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DocenteUsuarioRepository extends JpaRepository<DocenteUsuario, Long> {


  Optional<DocenteUsuario> findByCodigoDocente(String codigoDocente);
}