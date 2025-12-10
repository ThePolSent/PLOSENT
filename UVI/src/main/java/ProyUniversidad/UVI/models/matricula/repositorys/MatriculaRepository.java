package ProyUniversidad.UVI.models.matricula.repositorys;

import ProyUniversidad.UVI.models.matricula.models.Matricula;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends MongoRepository<Matricula, String> {
    
    List<Matricula> findByAlumnoId(Long alumnoId);
    
    Optional<Matricula> findByAlumnoIdAndCicloId(Long alumnoId, Long cicloId);
    
    Optional<Matricula> findByCodigoComprobante(String codigoComprobante);
    
    List<Matricula> findByCursosSeccionId(Long seccionId);
}