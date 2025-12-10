package ProyUniversidad.UVI.security;

import ProyUniversidad.UVI.models.usuario.models.Usuario;
import ProyUniversidad.UVI.models.usuario.repositorys.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFiltro extends OncePerRequestFilter {

  private final JwtProvedoor jwtProvedoor;
  private final UsuarioDetallesService usuarioDetallesService;
  private final UsuarioRepository usuarioRepository; 

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    String username = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(7);
      try {
        username = jwtProvedoor.extractUsername(jwt);
      } catch (Exception e) {
        log.warn("Error al extraer el username del token: {}", e.getMessage());
      }
    } else {
      filterChain.doFilter(request, response);
      return;
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UsuarioDetalles userDetails = (UsuarioDetalles) this.usuarioDetallesService.loadUserByUsername(username);

      if (jwtProvedoor.isTokenValido(jwt, userDetails)) {

        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario == null) {
          log.error("Usuario del token no encontrado en MySQL: {}", username);
          filterChain.doFilter(request, response);
          return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("Usuario autenticado exitosamente: {}", username);
      } else {
        log.warn("Token JWT inválido o expirado para el usuario: {}", username);
      }
    }

    filterChain.doFilter(request, response);
  }
}