package ProyUniversidad.UVI.dtos.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRefreshDTO {
  @NotBlank(message = "El refresh token es obligatorio")
  private String refreshToken;
}