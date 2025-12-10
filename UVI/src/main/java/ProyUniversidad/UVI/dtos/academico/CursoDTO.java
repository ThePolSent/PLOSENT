package ProyUniversidad.UVI.dtos.academico;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {

  private Long id;

  @NotBlank(message = "El código del curso es obligatorio")
  @Size(max = 20, message = "El código no puede exceder 20 caracteres")
  private String codigo;

  @NotBlank(message = "El nombre del curso es obligatorio")
  @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
  private String nombre;

  private String descripcion;

  @NotNull(message = "Los créditos son obligatorios")
  @Min(value = 1, message = "Los créditos deben ser al menos 1")
  private Integer creditos;

  private Integer horasTeoricas;

  private Integer horasPracticas;

  private String departamento;

  private Boolean activo = true;

  private Integer numeroSecciones;
}