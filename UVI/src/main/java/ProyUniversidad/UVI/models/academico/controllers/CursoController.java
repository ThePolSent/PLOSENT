package ProyUniversidad.UVI.models.academico.controllers;

import ProyUniversidad.UVI.models.academico.models.Curso;
import ProyUniversidad.UVI.models.academico.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

  private final CursoService cursoService;

  @GetMapping
  public ResponseEntity<List<Curso>> listarTodos() {
    return ResponseEntity.ok(cursoService.listarActivos());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
    return ResponseEntity.ok(cursoService.obtenerPorId(id));
  }

  @GetMapping("/codigo/{codigo}")
  public ResponseEntity<Curso> obtenerPorCodigo(@PathVariable String codigo) {
    return ResponseEntity.ok(cursoService.obtenerPorCodigo(codigo));
  }

  @GetMapping("/departamento/{departamento}")
  public ResponseEntity<List<Curso>> buscarPorDepartamento(@PathVariable String departamento) {
    return ResponseEntity.ok(cursoService.buscarPorDepartamento(departamento));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Curso> crear(@RequestBody Curso curso) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.crear(curso));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Curso> actualizar(@PathVariable Long id, @RequestBody Curso curso) {
    return ResponseEntity.ok(cursoService.actualizar(id, curso));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    cursoService.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}