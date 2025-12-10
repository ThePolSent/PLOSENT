package ProyUniversidad.UVI.models.usuario.controllers;

import ProyUniversidad.UVI.models.usuario.services.RecuperacionPasswordService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/password")
@RequiredArgsConstructor
public class PasswordController {

    private final RecuperacionPasswordService recuperacionService;

    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitar(@RequestBody SolicitarDTO dto) {
        recuperacionService.solicitarRecuperacion(dto.getUsername(), dto.getEmail());
        return ResponseEntity.ok("Correo enviado (Revisar bandeja de entrada).");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset(@RequestBody ResetDTO dto) {
        recuperacionService.cambiarPassword(dto.getToken(), dto.getNuevaPassword());
        return ResponseEntity.ok("Contraseña actualizada correctamente. Ya puedes iniciar sesión.");
    }

    @Data
    public static class SolicitarDTO { 
        private String username; 
        private String email; 
    }

    @Data
    public static class ResetDTO { private String token; private String nuevaPassword; }
}