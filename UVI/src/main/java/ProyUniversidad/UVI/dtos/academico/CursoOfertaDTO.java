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
public class CursoOfertaDTO {

  private Long cursoId;
  private String cursoCodigo;
  private String cursoNombre;
  private Integer creditos;
  private String departamento;

  private List<SeccionDTO> seccionesDisponibles;
}