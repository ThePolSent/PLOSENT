package ProyUniversidad.UVI.models.academico.service;

import ProyUniversidad.UVI.dtos.academico.HorarioDTO;
import ProyUniversidad.UVI.dtos.academico.SeccionDTO;
import ProyUniversidad.UVI.models.academico.models.Seccion;
import ProyUniversidad.UVI.models.academico.repository.SeccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "postgresqlTransactionManager")
@Slf4j
public class SeccionService {

  private final SeccionRepository seccionRepository;
  private static final int MAX_REINTENTOS = 3;

  public List<SeccionDTO> listarTodasDTO() {
    List<Seccion> secciones = seccionRepository.findAll();
    return secciones.stream().map(this::convertirADTO).collect(Collectors.toList());
  }

  public List<SeccionDTO> listarSeccionesDisponiblesDTO(Long cicloId) {
      List<Seccion> secciones = seccionRepository.findSeccionesConVacantes(cicloId);
      return secciones.stream().map(this::convertirADTO).collect(Collectors.toList());
  }

  private SeccionDTO convertirADTO(Seccion s) {
      return SeccionDTO.builder()
              .id(s.getId())
              .codigo(s.getCodigo())
              .cursoId(s.getCurso().getId())
              .cursoNombre(s.getCurso().getNombre())
              .cursoCodigo(s.getCurso().getCodigo())
              
              .creditos(s.getCurso().getCreditos()) 
              .cursoCiclo(s.getCurso().getCiclo())  
              
              .cicloAcademicoId(s.getCicloAcademico().getId())
              .docenteId(s.getDocente().getId())
              .docenteNombre(s.getDocente().getNombreCompleto())
              .vacantesTotales(s.getVacantesTotales())
              .vacantesOcupadas(s.getVacantesOcupadas())
              .vacantesDisponibles(s.getVacantesTotales() - s.getVacantesOcupadas())
              .modalidad(s.getModalidad())
              .activo(s.getActivo())
              .horarios(s.getHorarios().stream().map(h -> HorarioDTO.builder()
                      .id(h.getId())
                      .diaSemana(h.getDiaSemana())
                      .horaInicio(h.getHoraInicio())
                      .horaFin(h.getHoraFin())
                      .aulaNombre(h.getAula() != null ? h.getAula().getNombre() : "Virtual")
                      .build()).collect(Collectors.toList()))
              .build();
  }

  public List<Seccion> listarTodas() { return seccionRepository.findAll(); }
  public Seccion obtenerPorId(Long id) { return seccionRepository.findById(id).orElseThrow(() -> new RuntimeException("Sección no encontrada")); }
  public List<Seccion> listarPorCiclo(Long cicloId) { return seccionRepository.findByCicloAcademicoIdAndActivoTrue(cicloId); }
  public List<Seccion> listarPorCursoYCiclo(Long cursoId, Long cicloId) { return seccionRepository.findByCursoIdAndCicloAcademicoId(cursoId, cicloId); }
  public List<Seccion> listarSeccionesConVacantes(Long cicloId) { return seccionRepository.findSeccionesConVacantes(cicloId); }

  public Seccion crear(Seccion seccion) {
    if (seccion.getVacantesTotales() == null || seccion.getVacantesTotales() <= 0) throw new RuntimeException("Vacantes deben ser > 0");
    if (seccion.getVacantesOcupadas() == null) seccion.setVacantesOcupadas(0);
    if (seccion.getActivo() == null) seccion.setActivo(true);
    return seccionRepository.save(seccion);
  }

  public Seccion actualizar(Long id, Seccion seccionActualizada) {
    Seccion s = obtenerPorId(id);
    if (seccionActualizada.getVacantesTotales() < s.getVacantesOcupadas()) throw new RuntimeException("No se pueden reducir vacantes bajo las ocupadas");
    s.setVacantesTotales(seccionActualizada.getVacantesTotales());
    s.setModalidad(seccionActualizada.getModalidad());
    s.setActivo(seccionActualizada.getActivo());
    return seccionRepository.save(s);
  }

  public void incrementarVacantesOcupadas(Long seccionId) {
    int intentos = 0;
    while (intentos < MAX_REINTENTOS) {
      try {
        Seccion s = obtenerPorId(seccionId);
        if (s.getVacantesOcupadas() >= s.getVacantesTotales()) throw new RuntimeException("Sin vacantes");
        s.setVacantesOcupadas(s.getVacantesOcupadas() + 1);
        seccionRepository.save(s);
        return;
      } catch (ObjectOptimisticLockingFailureException e) {
        intentos++;
        if (intentos >= MAX_REINTENTOS) throw new RuntimeException("Error concurrencia");
        try { Thread.sleep(100 * intentos); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
      }
    }
  }

  public void decrementarVacantesOcupadas(Long seccionId) { }
}