package ProyUniversidad.UVI.utils;

import ProyUniversidad.UVI.dtos.academico.CursoDTO;
import ProyUniversidad.UVI.models.academico.models.Curso;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public CursoDTO toCursoDTO(Curso curso) {
        if (curso == null) return null;
        
        return CursoDTO.builder()
                .id(curso.getId())
                .codigo(curso.getCodigo())
                .nombre(curso.getNombre())
                .creditos(curso.getCreditos())
                .departamento(curso.getDepartamento())
                .activo(curso.getActivo())
                .descripcion(curso.getDescripcion())
                .horasTeoricas(curso.getHorasTeoricas())
                .horasPracticas(curso.getHorasPracticas())
                .numeroSecciones(curso.getSecciones() != null ? curso.getSecciones().size() : 0)
                .build();
    }

}