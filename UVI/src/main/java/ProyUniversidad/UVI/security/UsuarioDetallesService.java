package ProyUniversidad.UVI.security;

import ProyUniversidad.UVI.models.usuario.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UsuarioDetallesService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  @Autowired
  public UsuarioDetallesService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return usuarioRepository.findByUsername(username)
        .map(UsuarioDetalles::new)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
  }
}