package ProyUniversidad.UVI.models.usuario.repositorys;

import ProyUniversidad.UVI.models.usuario.models.Sesion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SesionRepository extends CrudRepository<Sesion, String> {
  Optional<Sesion> findByUsuarioId(Long usuarioId);

  void deleteById(String id);
}