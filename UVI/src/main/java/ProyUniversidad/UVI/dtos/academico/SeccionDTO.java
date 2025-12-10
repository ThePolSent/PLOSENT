package ProyUniversidad.UVI.dtos.academico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeccionDTO {
  private Long id;
  private String codigo;
  private Long cursoId;
  private String cursoNombre;
  private String cursoCodigo;
  
  private Integer creditos;   
  private Integer cursoCiclo; 
  private Long cicloAcademicoId;
  private String cicloNombre;
  private Long docenteId;
  private String docenteNombre;
  private Integer vacantesTotales;
  private Integer vacantesOcupadas;
  private Integer vacantesDisponibles;
  private String modalidad;
  private Boolean activo;
  private List<HorarioDTO> horarios;
}