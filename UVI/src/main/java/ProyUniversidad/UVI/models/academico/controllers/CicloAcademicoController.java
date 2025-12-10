package ProyUniversidad.UVI.models.academico.controllers;

import ProyUniversidad.UVI.models.academico.models.CicloAcademico;
import ProyUniversidad.UVI.models.academico.service.CicloAcademicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciclos")
@RequiredArgsConstructor
public class CicloAcademicoController {

  private final CicloAcademicoService cicloService;

  @GetMapping
  public ResponseEntity<List<CicloAcademico>> listarTodos() {
    return ResponseEntity.ok(cicloService.listarActivos());
  }

  @GetMapping("/actual")
  public ResponseEntity<CicloAcademico> obtenerCicloActual() {
    return ResponseEntity.ok(cicloService.obtenerCicloActual());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CicloAcademico> obtenerPorId(@PathVariable Long id) {
    return ResponseEntity.ok(cicloService.obtenerPorId(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CicloAcademico> crear(@RequestBody CicloAcademico ciclo) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cicloService.crear(ciclo));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CicloAcademico> actualizar(
      @PathVariable Long id,
      @RequestBody CicloAcademico ciclo) {
    return ResponseEntity.ok(cicloService.actualizar(id, ciclo));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    cicloService.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}