package ProyUniversidad.UVI.models.matricula.repositorys;

import ProyUniversidad.UVI.models.matricula.models.Cuota;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CuotaRepository extends MongoRepository<Cuota, String> {
    List<Cuota> findByAlumnoIdAndEstado(Long alumnoId, String estado);
}