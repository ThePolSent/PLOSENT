package ProyUniversidad.UVI.models.usuario.controllers;

import ProyUniversidad.UVI.dtos.usuario.RegistroUsuarioDTO;
import ProyUniversidad.UVI.models.usuario.models.Alumno;
import ProyUniversidad.UVI.models.usuario.models.DocenteUsuario;
import ProyUniversidad.UVI.models.usuario.models.Usuario;
import ProyUniversidad.UVI.models.usuario.services.AdminService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") 
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/crear-admin")
    public ResponseEntity<Usuario> crearAdmin(@Valid @RequestBody RegistroUsuarioDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.crearAdmin(request));
    }

    @PostMapping("/crear-alumno")
    public ResponseEntity<Alumno> crearAlumno(@RequestBody RegistroAlumnoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.crearAlumno(request.getUsuario(), request.getCodigo(), request.getCarrera()));
    }

    @PostMapping("/crear-docente")
    public ResponseEntity<DocenteUsuario> crearDocente(@RequestBody RegistroDocenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.crearDocente(request.getUsuario(), request.getCodigo(), request.getEspecialidad()));
    }

    @Data
    public static class RegistroAlumnoRequest {
        private RegistroUsuarioDTO usuario;
        private String codigo;
        private String carrera;
    }

    @Data
    public static class RegistroDocenteRequest {
        private RegistroUsuarioDTO usuario;
        private String codigo;
        private String especialidad;
    }
}