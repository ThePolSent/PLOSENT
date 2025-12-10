package ProyUniversidad.UVI.dtos.usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResumenDTO {
    private String estadoAcademico; 
    private Double promedioPonderado;
    private Double deudaTotal;
}