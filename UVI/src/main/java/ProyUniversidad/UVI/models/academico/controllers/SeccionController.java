package ProyUniversidad.UVI.models.academico.controllers;

import ProyUniversidad.UVI.dtos.academico.SeccionDTO;
import ProyUniversidad.UVI.models.academico.models.Seccion;
import ProyUniversidad.UVI.models.academico.service.SeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/secciones")
@RequiredArgsConstructor
public class SeccionController {

    private final SeccionService seccionService;

    @GetMapping
    public ResponseEntity<List<SeccionDTO>> listarTodas() {
        return ResponseEntity.ok(seccionService.listarTodasDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seccion> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(seccionService.obtenerPorId(id));
    }

    @GetMapping("/ciclo/{cicloId}")
    public ResponseEntity<List<Seccion>> listarPorCiclo(@PathVariable Long cicloId) {
        return ResponseEntity.ok(seccionService.listarPorCiclo(cicloId));
    }

    @GetMapping("/ciclo/{cicloId}/vacantes")
    public ResponseEntity<List<SeccionDTO>> listarConVacantes(@PathVariable Long cicloId) {
        return ResponseEntity.ok(seccionService.listarSeccionesDisponiblesDTO(cicloId));
    }

    @GetMapping("/curso/{cursoId}/ciclo/{cicloId}")
    public ResponseEntity<List<Seccion>> listarPorCursoYCiclo(@PathVariable Long cursoId, @PathVariable Long cicloId) {
        return ResponseEntity.ok(seccionService.listarPorCursoYCiclo(cursoId, cicloId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seccion> crear(@RequestBody Seccion seccion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seccionService.crear(seccion));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seccion> actualizar(@PathVariable Long id, @RequestBody Seccion seccion) {
        return ResponseEntity.ok(seccionService.actualizar(id, seccion));
    }
}