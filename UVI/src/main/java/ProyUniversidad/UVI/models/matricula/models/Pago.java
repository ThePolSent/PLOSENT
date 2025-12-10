package ProyUniversidad.UVI.models.matricula.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "pagos")
@Data
@Builder
public class Pago {
    @Id
    private String id;
    private Long alumnoId;
    private Long cicloId;
    private String concepto; 
    private Double monto;
    private LocalDateTime fechaPago;
    private String metodoPago; 
}