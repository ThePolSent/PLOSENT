package ProyUniversidad.UVI.dtos.academico;

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
public class DocenteDTO {

  private Long id;

  @NotNull(message = "El ID del usuario es obligatorio")
  private Long usuarioId;

  @NotBlank(message = "El código del docente es obligatorio")
  @Size(max = 15, message = "El código no puede exceder 15 caracteres")
  private String codigoDocente;

  @NotBlank(message = "El nombre completo es obligatorio")
  @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
  private String nombreCompleto;

  private String especialidad;

  private String categoria;

  private String gradoAcademico;

  private String departamento;

  private String curriculum;

  private Boolean activo = true;

  private String username; 
  private Integer seccionesActuales;
}
