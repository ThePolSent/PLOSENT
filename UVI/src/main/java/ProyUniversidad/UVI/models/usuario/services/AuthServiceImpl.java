package ProyUniversidad.UVI.models.usuario.services;

import ProyUniversidad.UVI.config.JwtConfig;
import ProyUniversidad.UVI.dtos.usuario.*;
import ProyUniversidad.UVI.models.historial.services.AuditoriaService; 
import ProyUniversidad.UVI.models.usuario.models.Sesion;
import ProyUniversidad.UVI.models.usuario.models.Usuario;
import ProyUniversidad.UVI.models.usuario.repositorys.SesionRepository;
import ProyUniversidad.UVI.models.usuario.repositorys.UsuarioRepository;
import ProyUniversidad.UVI.security.JwtProvedoor;
import ProyUniversidad.UVI.security.UsuarioDetalles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UsuarioRepository usuarioRepository;
  private final SesionRepository sesionRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvedoor jwtProvedoor;
  private final JwtConfig jwtConfig;
  private final AuditoriaService auditoriaService; 

  @Override
  @Transactional(transactionManager = "mysqlTransactionManager")
  public LoginResponseDTO login(LoginDTO request) {
    Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

    if (!usuario.getActivo()) {
      throw new BadCredentialsException("Usuario inactivo");
    }

    if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
      throw new BadCredentialsException("Credenciales inválidas");
    }

    UsuarioDetalles userDetails = new UsuarioDetalles(usuario);
    String accessToken = jwtProvedoor.generarTokenAcceso(userDetails, usuario);
    String refreshToken = UUID.randomUUID().toString();

    Sesion sesion = Sesion.builder()
        .id(refreshToken)
        .usuarioId(usuario.getId())
        .username(usuario.getUsername())
        .expiracionSegundos(jwtConfig.getRefreshExpirationSegundos())
        .build();
    sesionRepository.save(sesion);

    auditoriaService.registrarEvento(
        usuario.getUsername(), 
        "LOGIN", 
        "Inicio de sesión exitoso. Rol: " + usuario.getRol()
    );

    return LoginResponseDTO.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .username(usuario.getUsername())
        .rol(usuario.getRol().name())
        .expiresIn(jwtConfig.getExpirationSegundos())
        .build();
  }

  @Override
  @Transactional(transactionManager = "mysqlTransactionManager")
  public LoginResponseDTO register(RegistroUsuarioDTO request) {
    if (usuarioRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("El usuario ya existe");
    }

    Usuario.Rol rol;
    try {
      rol = Usuario.Rol.valueOf(request.getRol().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Rol inválido: " + request.getRol());
    }

    Usuario usuario = Usuario.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .rol(rol)
        .nombreCompleto(request.getNombreCompleto()) 
        .activo(true)
        .build();

    usuarioRepository.save(usuario);

    auditoriaService.registrarEvento(
        "SISTEMA", 
        "REGISTRO_USUARIO", 
        "Nuevo usuario registrado: " + request.getUsername() + " (" + rol + ")"
    );

    LoginDTO loginDTO = LoginDTO.builder()
        .username(request.getUsername())
        .password(request.getPassword())
        .build();

    return login(loginDTO);
  }

  @Override
  @Transactional(transactionManager = "mysqlTransactionManager")
  public LoginResponseDTO refreshToken(TokenRefreshDTO request) {
    Sesion sesion = sesionRepository.findById(request.getRefreshToken())
        .orElseThrow(() -> new BadCredentialsException("Refresh token inválido o expirado"));

    Usuario usuario = usuarioRepository.findById(sesion.getUsuarioId())
        .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

    if (!usuario.getActivo()) {
      throw new BadCredentialsException("Usuario inactivo");
    }

    UsuarioDetalles userDetails = new UsuarioDetalles(usuario);
    String nuevoAccessToken = jwtProvedoor.generarTokenAcceso(userDetails, usuario);

    return LoginResponseDTO.builder()
        .accessToken(nuevoAccessToken)
        .refreshToken(request.getRefreshToken())
        .username(usuario.getUsername())
        .rol(usuario.getRol().name())
        .expiresIn(jwtConfig.getExpirationSegundos())
        .build();
  }

  @Override
  @Transactional(transactionManager = "mysqlTransactionManager")
  public void logout(String refreshToken) {
    sesionRepository.deleteById(refreshToken);
  }
}