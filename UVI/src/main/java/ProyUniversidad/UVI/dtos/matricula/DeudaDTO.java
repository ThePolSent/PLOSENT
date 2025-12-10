package ProyUniversidad.UVI.dtos.matricula;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class DeudaDTO {
    private String id; 
    private String concepto; 
    private Double monto;
    private LocalDate vencimiento;
    private String tipo; 
}