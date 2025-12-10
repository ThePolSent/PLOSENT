package ProyUniversidad.UVI.models.usuario.controllers;

import ProyUniversidad.UVI.dtos.usuario.*;
import ProyUniversidad.UVI.models.usuario.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/register")
  public ResponseEntity<LoginResponseDTO> register(@Valid @RequestBody RegistroUsuarioDTO request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponseDTO> refresh(@Valid @RequestBody TokenRefreshDTO request) {
    return ResponseEntity.ok(authService.refreshToken(request));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestBody TokenRefreshDTO request) {
    authService.logout(request.getRefreshToken());
    return ResponseEntity.noContent().build();
  }
}