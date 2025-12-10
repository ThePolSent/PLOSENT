package ProyUniversidad.UVI.dtos.academico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeccionCrearDTO {

  @NotBlank(message = "El código de sección es obligatorio")
  private String codigo;

  @NotNull(message = "El ID del curso es obligatorio")
  private Long cursoId;

  @NotNull(message = "El ID del ciclo académico es obligatorio")
  private Long cicloAcademicoId;

  @NotNull(message = "El ID del docente es obligatorio")
  private Long docenteId;

  @NotNull(message = "Las vacantes totales son obligatorias")
  @Min(value = 1, message = "Debe haber al menos 1 vacante")
  private Integer vacantesTotales;

  private String modalidad;

  @Valid
  private List<HorarioDTO> horarios;
}