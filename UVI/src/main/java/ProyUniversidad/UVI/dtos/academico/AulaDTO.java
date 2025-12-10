package ProyUniversidad.UVI.dtos.academico;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AulaDTO {

  private Long id;

  @NotBlank(message = "El código del aula es obligatorio")
  @Size(max = 20, message = "El código no puede exceder 20 caracteres")
  private String codigo;

  @NotBlank(message = "El nombre del aula es obligatorio")
  @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
  private String nombre;

  private String edificio;

  private String piso;

  @Min(value = 1, message = "La capacidad debe ser al menos 1")
  private Integer capacidad;

  private String tipo; 

  private String equipamiento;

  private Boolean activo = true;

  private Boolean disponible;
}