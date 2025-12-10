package ProyUniversidad.UVI.models.academico.service;

import ProyUniversidad.UVI.models.academico.models.Curso;
import ProyUniversidad.UVI.models.academico.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "postgresqlTransactionManager")
public class CursoService {

  private final CursoRepository cursoRepository;

  public List<Curso> listarTodos() {
    return cursoRepository.findAll();
  }

  public List<Curso> listarActivos() {
    return cursoRepository.findByActivoTrue();
  }

  public Curso obtenerPorId(Long id) {
    return cursoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));
  }

  public Curso obtenerPorCodigo(String codigo) {
    return cursoRepository.findByCodigo(codigo)
        .orElseThrow(() -> new RuntimeException("Curso no encontrado con código: " + codigo));
  }

  public Curso crear(Curso curso) {
    if (curso.getActivo() == null) {
        curso.setActivo(true); 
    }

    if (cursoRepository.existsByCodigo(curso.getCodigo())) {
      throw new RuntimeException("Ya existe un curso con el código: " + curso.getCodigo());
    }

    // 3. Validaciones de negocio
    if (curso.getCreditos() == null || curso.getCreditos() <= 0) {
      throw new RuntimeException("Los créditos deben ser mayores a 0");
    }

    if (curso.getHorasTeoricas() != null && curso.getHorasTeoricas() < 0) {
      throw new RuntimeException("Las horas teóricas no pueden ser negativas");
    }

    if (curso.getHorasPracticas() != null && curso.getHorasPracticas() < 0) {
      throw new RuntimeException("Las horas prácticas no pueden ser negativas");
    }

    return cursoRepository.save(curso);
  }

  public Curso actualizar(Long id, Curso cursoActualizado) {
    Curso cursoExistente = obtenerPorId(id);

    cursoExistente.setNombre(cursoActualizado.getNombre());
    cursoExistente.setDescripcion(cursoActualizado.getDescripcion());

    if (cursoActualizado.getCreditos() != null && cursoActualizado.getCreditos() > 0) {
      cursoExistente.setCreditos(cursoActualizado.getCreditos());
    }

    cursoExistente.setHorasTeoricas(cursoActualizado.getHorasTeoricas());
    cursoExistente.setHorasPracticas(cursoActualizado.getHorasPracticas());
    cursoExistente.setDepartamento(cursoActualizado.getDepartamento());
    cursoExistente.setActivo(cursoActualizado.getActivo());

    return cursoRepository.save(cursoExistente);
  }

  public void eliminar(Long id) {
    Curso curso = obtenerPorId(id);
    curso.setActivo(false);
    cursoRepository.save(curso);
  }

  public List<Curso> buscarPorDepartamento(String departamento) {
    if (departamento == null || departamento.trim().isEmpty()) {
      throw new RuntimeException("El departamento no puede estar vacío");
    }
    return cursoRepository.findByDepartamento(departamento);
  }
}