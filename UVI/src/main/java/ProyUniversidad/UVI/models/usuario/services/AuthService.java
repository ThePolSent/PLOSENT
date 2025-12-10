package ProyUniversidad.UVI.models.usuario.services;

import ProyUniversidad.UVI.dtos.usuario.LoginDTO;
import ProyUniversidad.UVI.dtos.usuario.LoginResponseDTO;
import ProyUniversidad.UVI.dtos.usuario.RegistroUsuarioDTO;
import ProyUniversidad.UVI.dtos.usuario.TokenRefreshDTO;


public interface AuthService {

  LoginResponseDTO login(LoginDTO request);

  LoginResponseDTO register(RegistroUsuarioDTO request);

  LoginResponseDTO refreshToken(TokenRefreshDTO request);

  void logout(String refreshToken);
}