package ProyUniversidad.UVI.dtos.usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
  private String accessToken;
  private String refreshToken;
  private String username;
  private String rol;
  private Long expiresIn; 
}