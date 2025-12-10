package ProyUniversidad.UVI.security;

import ProyUniversidad.UVI.models.usuario.models.Usuario;
import ProyUniversidad.UVI.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class JwtProvedoor {

  private final JwtConfig jwtConfig;

  @Autowired
  public JwtProvedoor(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public String generarTokenAcceso(UserDetails userDetails, Usuario usuario) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", getRoles(userDetails));
    claims.put("id", usuario.getId());
    claims.put("tipoUsuario", usuario.getRol().name()); 

    return crearToken(claims, userDetails.getUsername(), jwtConfig.getExpirationMs());
  }


  private String crearToken(Map<String, Object> claims, String subject, long expirationTime) {
    Date ahora = new Date();
    Date fechaExpiracion = new Date(ahora.getTime() + expirationTime);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(ahora)
        .setExpiration(fechaExpiracion)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }


  public LocalDateTime obtenerExpiracion(String token) {
    Date fechaExpiracion = extractClaim(token, Claims::getExpiration);
    return fechaExpiracion.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }


  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }


  public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public boolean isTokenValido(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpirado(token);
  }

  
  private boolean isTokenExpirado(String token) {
    return extractExpiration(token).before(new Date());
  }

 
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

 
  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret()); // ✅ CORREGIDO: usa getSecret()
    return Keys.hmacShaKeyFor(keyBytes);
  }


  private List<String> getRoles(UserDetails userDetails) {
    return userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
  }
}