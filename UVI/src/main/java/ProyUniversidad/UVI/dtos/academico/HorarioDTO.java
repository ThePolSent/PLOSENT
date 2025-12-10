package ProyUniversidad.UVI.dtos.academico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDTO {

  private Long id;

  @NotNull(message = "El ID de la sección es obligatorio")
  private Long seccionId;

  private Long aulaId;

  @NotBlank(message = "El día de la semana es obligatorio")
  private String diaSemana; 

  @NotNull(message = "La hora de inicio es obligatoria")
  private LocalTime horaInicio;

  @NotNull(message = "La hora de fin es obligatoria")
  private LocalTime horaFin;

  private String tipo;

  private String aulaNombre;
  private String aulaEdificio;
}