package ProyUniversidad.UVI.dtos.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token; 
    private String refreshToken; 
    private String tipoToken = "Bearer";
    private LocalDateTime expiracionToken;
    private Long id;
    private String username;
    private List<String> roles;
    private String tipoUsuario; 
}