package ProyUniversidad.UVI.models.historial.controllers;

import ProyUniversidad.UVI.models.academico.models.Curso;
import ProyUniversidad.UVI.models.academico.repository.CursoRepository;
import ProyUniversidad.UVI.models.historial.models.HistorialAcademico;
import ProyUniversidad.UVI.models.historial.repositorys.HistorialAcademicoRepository;
import ProyUniversidad.UVI.models.usuario.models.Usuario;
import ProyUniversidad.UVI.models.usuario.repositorys.UsuarioRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ALUMNO')")
public class HistorialController {

    private final HistorialAcademicoRepository historialRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    @GetMapping("/mi-historial")
    public ResponseEntity<List<HistorialDTO>> obtenerMiHistorial(Authentication auth) {
        String username = auth.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<HistorialAcademico> historial = historialRepository.findByAlumnoId(usuario.getId());

        List<HistorialDTO> respuesta = historial.stream().map(h -> {
            Curso curso = cursoRepository.findById(h.getCursoId()).orElse(null);
            
            return HistorialDTO.builder()
                    .curso(curso != null ? curso.getNombre() : "Desconocido")
                    .codigoCurso(curso != null ? curso.getCodigo() : "---")
                    .cicloId(h.getCicloId()) 
                    .nota(h.getNota())
                    .estado(h.getEstado().name())
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    @Data @Builder
    public static class HistorialDTO {
        private String curso;
        private String codigoCurso;
        private Long cicloId;
        private Double nota;
        private String estado;
    }
}