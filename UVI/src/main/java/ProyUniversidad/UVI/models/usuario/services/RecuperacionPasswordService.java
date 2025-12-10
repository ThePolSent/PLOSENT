package ProyUniversidad.UVI.models.usuario.services;

import ProyUniversidad.UVI.models.usuario.models.Usuario;
import ProyUniversidad.UVI.models.usuario.repositorys.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RecuperacionPasswordService {

    private final UsuarioRepository usuarioRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void solicitarRecuperacion(String usernameInput, String emailDestinoInput) {
        Usuario usuario = usuarioRepository.findByUsername(usernameInput)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set("RESET_TOKEN:" + token, usuario.getUsername(), 15, TimeUnit.MINUTES);

        // Usar el email que viene del frontend, si existe
        String emailFinal = (emailDestinoInput != null && !emailDestinoInput.isEmpty()) 
                            ? emailDestinoInput 
                            : "oaquino747@gmail.com";

        enviarCorreo(usuario.getUsername(), emailFinal, token);
    }

    public void cambiarPassword(String token, String nuevaPassword) {
        String username = (String) redisTemplate.opsForValue().get("RESET_TOKEN:" + token);
        
        if (username == null) {
            throw new RuntimeException("El enlace de recuperación es inválido o ha expirado.");
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));
        
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);

        redisTemplate.delete("RESET_TOKEN:" + token);
    }

    private void enviarCorreo(String username, String emailDestino, String token) {
        String enlace = "http://localhost:5173/reset-password?token=" + token;

        System.out.println(">>> ENVIANDO A: " + emailDestino + " | Link: " + enlace);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("marceloalarconmanay@gmail.com"); 
            message.setTo(emailDestino);
            message.setSubject("Recuperación de Contraseña - UVI (Demo)");
            message.setText("Hola " + username + ",\n\n" +
                    "Estás recibiendo este correo porque solicitaste recuperar tu contraseña en la Demo.\n" +
                    "Haz clic aquí para cambiarla:\n\n" +
                    enlace + "\n\n" +
                    "Este enlace expira en 15 minutos.");

            mailSender.send(message);
            System.out.println(">>> ¡CORREO ENVIADO CON ÉXITO A " + emailDestino + "!");
        } catch (Exception e) {
            System.err.println(">>> ERROR AL ENVIAR CORREO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error enviando correo: " + e.getMessage());
        }
    }
}