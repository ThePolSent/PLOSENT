package ProyUniversidad.UVI.models.historial.services;

import ProyUniversidad.UVI.models.academico.models.Curso;
import ProyUniversidad.UVI.models.historial.repositorys.HistorialAcademicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistorialService {

    private final HistorialAcademicoRepository historialRepository;

    public boolean cumplePrerrequisitos(Long alumnoId, Curso curso) {
        if (curso.getPrerrequisitos() == null || curso.getPrerrequisitos().isEmpty()) {
            return true;
        }

        for (Curso prerrequisito : curso.getPrerrequisitos()) {
            boolean aprobado = historialRepository.haAprobadoCurso(alumnoId, prerrequisito.getId());
            if (!aprobado) {
                return false;
            }
        }
        return true;
    }

    public boolean cursoYaAprobado(Long alumnoId, Long cursoId) {
        return historialRepository.haAprobadoCurso(alumnoId, cursoId);
    }
}