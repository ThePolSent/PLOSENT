package ProyUniversidad.UVI.dtos.academico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfertaAcademicaDTO {

  private Long cicloId;
  private String cicloNombre;
  private String cicloCodigo;
  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private Boolean matriculaAbierta;

  private List<CursoOfertaDTO> cursosOfertados;
}