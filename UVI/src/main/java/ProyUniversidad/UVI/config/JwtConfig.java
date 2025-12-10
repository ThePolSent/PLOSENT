package ProyUniversidad.UVI.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Data;


@Configuration
@Data
public class JwtConfig {

  @Value("${jwt.secret-key}")
  private String secretKey;

  @Value("${jwt.expiration-ms}")
  private long expirationMs;

  @Value("${jwt.refresh-expiration-ms}")
  private long refreshExpirationMs;

  public String getSecret() {
    return secretKey;
  }

  public long getExpirationSegundos() {
    return expirationMs / 1000;
  }


  public long getRefreshExpirationSegundos() {
    return refreshExpirationMs / 1000;
  }
}