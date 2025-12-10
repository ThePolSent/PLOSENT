package ProyUniversidad.UVI.models.matricula.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "cuotas")
@Data
@Builder
public class Cuota {
    @Id
    private String id;
    private String matriculaId; 
    private Long alumnoId;
    private Integer numero; 
    private Double monto;
    private LocalDate vencimiento;
    private String estado; 
}