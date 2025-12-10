package ProyUniversidad.UVI.dtos.usuario;

import jakarta.validation.constraints.Email;
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
public class RegistroUsuarioDTO {
  @NotBlank(message = "El nombre de usuario es obligatorio")
  @Size(min = 4, max = 50)
  private String username;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 6)
  private String password;

  @NotBlank(message = "El rol es obligatorio")
  private String rol; 

  @Email(message = "Debe ser un formato de correo válido")
  private String email;

  @NotBlank(message = "El nombre completo es obligatorio")
  private String nombreCompleto;
}