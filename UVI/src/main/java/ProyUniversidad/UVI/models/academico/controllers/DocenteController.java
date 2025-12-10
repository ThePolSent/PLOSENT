package ProyUniversidad.UVI.models.academico.controllers;

import ProyUniversidad.UVI.dtos.academico.DocenteDTO;
import ProyUniversidad.UVI.models.academico.models.Seccion;
import ProyUniversidad.UVI.models.academico.models.Docente;
import ProyUniversidad.UVI.models.academico.repository.DocenteRepository;
import ProyUniversidad.UVI.models.academico.service.SeccionService;
import ProyUniversidad.UVI.models.academico.service.CicloAcademicoService;
import ProyUniversidad.UVI.models.matricula.models.Matricula;
import ProyUniversidad.UVI.models.matricula.repositorys.MatriculaRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteRepository docenteRepository;
    private final SeccionService seccionService;
    private final CicloAcademicoService cicloService;
    private final MatriculaRepository matriculaRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DocenteDTO>> listarTodos() {
        List<Docente> docentes = docenteRepository.findAll();
        
        List<DocenteDTO> dtos = docentes.stream().map(d -> DocenteDTO.builder()
                .id(d.getId())
                .codigoDocente(d.getCodigoDocente())
                .nombreCompleto(d.getNombreCompleto())
                .especialidad(d.getEspecialidad())
                .usuarioId(d.getUsuarioId())
                .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{docenteId}/secciones-actuales")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMIN')") // Admin también puede ver por si acaso
    public ResponseEntity<List<SeccionDocenteDTO>> misSecciones(@PathVariable Long docenteId) {
        Long cicloActualId = cicloService.obtenerCicloActual().getId();
        List<Seccion> secciones = seccionService.listarPorCiclo(cicloActualId);
        
        List<SeccionDocenteDTO> misSecciones = secciones.stream()
                .filter(s -> s.getDocente().getId().equals(docenteId))
                .map(s -> SeccionDocenteDTO.builder()
                        .id(s.getId())
                        .cursoNombre(s.getCurso().getNombre())
                        .codigoSeccion(s.getCodigo())
                        .horario(s.getHorarios().toString()) 
                        .inscritos(s.getVacantesOcupadas())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(misSecciones);
    }

    @GetMapping("/secciones/{seccionId}/alumnos")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMIN')")
    public ResponseEntity<List<AlumnoResumenDTO>> obtenerAlumnos(@PathVariable Long seccionId) {
        List<Matricula> matriculas = matriculaRepository.findByCursosSeccionId(seccionId);

        List<AlumnoResumenDTO> alumnos = matriculas.stream()
                .map(m -> AlumnoResumenDTO.builder()
                        .codigo(m.getCodigoUniversitario())
                        .nombre(m.getNombreAlumno())
                        .fechaInscripcion(m.getFechaMatricula().toLocalDate().toString())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(alumnos);
    }


    @Data @Builder
    public static class SeccionDocenteDTO {
        private Long id;
        private String cursoNombre;
        private String codigoSeccion;
        private String horario;
        private Integer inscritos;
    }

    @Data @Builder
    public static class AlumnoResumenDTO {
        private String codigo;
        private String nombre;
        private String fechaInscripcion;
    }
}