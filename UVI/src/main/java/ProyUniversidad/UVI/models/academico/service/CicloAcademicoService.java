package ProyUniversidad.UVI.models.academico.service;

import ProyUniversidad.UVI.models.academico.models.CicloAcademico;
import ProyUniversidad.UVI.models.academico.repository.CicloAcademicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "postgresqlTransactionManager")
public class CicloAcademicoService {

  private final CicloAcademicoRepository cicloRepository;

  public List<CicloAcademico> listarTodos() {
    return cicloRepository.findAll();
  }

  public List<CicloAcademico> listarActivos() {
    return cicloRepository.findByActivoTrue();
  }

  public CicloAcademico obtenerPorId(Long id) {
    return cicloRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ciclo académico no encontrado con ID: " + id));
  }

  public CicloAcademico obtenerCicloActual() {
    return cicloRepository.findFirstByActivoTrueOrderByFechaInicioDesc()
        .orElseThrow(() -> new RuntimeException("No hay ciclo académico activo"));
  }

  public CicloAcademico crear(CicloAcademico ciclo) {
    validarFechasCiclo(ciclo);

    return cicloRepository.save(ciclo);
  }

  public CicloAcademico actualizar(Long id, CicloAcademico cicloActualizado) {
    CicloAcademico cicloExistente = obtenerPorId(id);

    validarFechasCiclo(cicloActualizado);

    cicloExistente.setNombre(cicloActualizado.getNombre());
    cicloExistente.setFechaInicio(cicloActualizado.getFechaInicio());
    cicloExistente.setFechaFin(cicloActualizado.getFechaFin());
    cicloExistente.setFechaInicioMatricula(cicloActualizado.getFechaInicioMatricula());
    cicloExistente.setFechaFinMatricula(cicloActualizado.getFechaFinMatricula());
    cicloExistente.setActivo(cicloActualizado.getActivo());

    return cicloRepository.save(cicloExistente);
  }

  public void eliminar(Long id) {
    CicloAcademico ciclo = obtenerPorId(id);
    ciclo.setActivo(false);
    cicloRepository.save(ciclo);
  }

  private void validarFechasCiclo(CicloAcademico ciclo) {
    if (ciclo.getFechaInicio().isAfter(ciclo.getFechaFin())) {
      throw new RuntimeException("La fecha de inicio del ciclo no puede ser posterior a su fecha de fin");
    }

    if (ciclo.getFechaInicioMatricula() != null && ciclo.getFechaFinMatricula() != null) {
      
      if (ciclo.getFechaInicioMatricula().isAfter(ciclo.getFechaFinMatricula())) {
        throw new RuntimeException("La fecha de inicio de matrícula no puede ser posterior al fin de matrícula");
      }

      if (ciclo.getFechaInicioMatricula().isAfter(ciclo.getFechaFin())) {
         throw new RuntimeException("La matrícula no puede iniciar después de que el ciclo ha terminado");
      }
    }
  }

  public boolean estaEnPeriodoMatricula(Long cicloId) {
    CicloAcademico ciclo = obtenerPorId(cicloId);

    if (!ciclo.getActivo()) {
      return false;
    }

    if (ciclo.getFechaInicioMatricula() == null || ciclo.getFechaFinMatricula() == null) {
      return false;
    }

    LocalDate hoy = LocalDate.now();
    return !hoy.isBefore(ciclo.getFechaInicioMatricula()) && !hoy.isAfter(ciclo.getFechaFinMatricula());
  }
}