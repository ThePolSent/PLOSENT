package ProyUniversidad.UVI.models.matricula.repositorys;

import ProyUniversidad.UVI.models.matricula.models.Pago;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends MongoRepository<Pago, String> {
    boolean existsByAlumnoIdAndCicloIdAndConcepto(Long alumnoId, Long cicloId, String concepto);
}