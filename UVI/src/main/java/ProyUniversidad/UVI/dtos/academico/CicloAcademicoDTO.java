package ProyUniversidad.UVI.dtos.academico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CicloAcademicoDTO {

  private Long id;

  @NotBlank(message = "El código del ciclo es obligatorio")
  @Size(max = 20, message = "El código no puede exceder 20 caracteres")
  private String codigo;

  @NotBlank(message = "El nombre del ciclo es obligatorio")
  @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
  private String nombre;

  @NotNull(message = "La fecha de inicio es obligatoria")
  private LocalDate fechaInicio;

  @NotNull(message = "La fecha de fin es obligatoria")
  private LocalDate fechaFin;

  private LocalDate fechaInicioMatricula;

  private LocalDate fechaFinMatricula;

  private Boolean activo = true;

  private Boolean enMatricula; 
  private Integer totalSecciones;
}