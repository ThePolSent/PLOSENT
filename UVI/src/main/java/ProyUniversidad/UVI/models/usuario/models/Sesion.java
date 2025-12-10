package ProyUniversidad.UVI.models.usuario.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;


@RedisHash("sesion") 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sesion {

  @Id
  private String id;

  private Long usuarioId;

  private String username;

  private LocalDateTime fechaCreacion = LocalDateTime.now();

  @TimeToLive
  private Long expiracionSegundos;

  private String ipCliente;
}
