package ProyUniversidad.UVI.models.usuario.controllers;

import ProyUniversidad.UVI.dtos.usuario.DashboardResumenDTO;
import ProyUniversidad.UVI.models.academico.service.CicloAcademicoService;
import ProyUniversidad.UVI.models.matricula.models.Cuota;
import ProyUniversidad.UVI.models.matricula.repositorys.CuotaRepository;
import ProyUniversidad.UVI.models.matricula.services.PagoServicio;
import ProyUniversidad.UVI.models.usuario.models.Alumno;
import ProyUniversidad.UVI.models.usuario.repositorys.AlumnoRepository;
import ProyUniversidad.UVI.models.usuario.repositorys.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    
    private final PagoServicio pagoServicio;
    private final CuotaRepository cuotaRepository;
    private final CicloAcademicoService cicloService;

    @GetMapping("/perfil")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<Alumno> miPerfil(Authentication auth) {
        String username = auth.getName();
        Long id = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")).getId();
        
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
                
        return ResponseEntity.ok(alumno);
    }

    @GetMapping("/dashboard-resumen")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<DashboardResumenDTO> obtenerResumenDashboard(Authentication auth) {
        String username = auth.getName();
        Long alumnoId = usuarioRepository.findByUsername(username).orElseThrow().getId();
        Alumno alumno = alumnoRepository.findById(alumnoId).orElseThrow();

        double deudaTotal = 0.0;
        
        try {
            Long cicloId = cicloService.obtenerCicloActual().getId();
            if (!pagoServicio.tieneMatriculaHabilitada(alumnoId, cicloId)) {
                deudaTotal += 350.00; // Derecho matrícula
            }
        } catch (Exception e) {
        }

        List<Cuota> cuotasPendientes = cuotaRepository.findByAlumnoIdAndEstado(alumnoId, "PENDIENTE");
        for (Cuota c : cuotasPendientes) {
            deudaTotal += c.getMonto();
        }

        String estado = "Regular";
        if (alumno.getPromedioGeneral() != null && alumno.getPromedioGeneral() < 13.0) {
            estado = "En Riesgo";
        }

        return ResponseEntity.ok(DashboardResumenDTO.builder()
                .estadoAcademico(estado)
                .promedioPonderado(alumno.getPromedioGeneral() != null ? alumno.getPromedioGeneral() : 0.0)
                .deudaTotal(deudaTotal)
                .build());
    }
}