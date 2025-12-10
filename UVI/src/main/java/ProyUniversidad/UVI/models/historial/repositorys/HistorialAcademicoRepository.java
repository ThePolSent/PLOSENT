package ProyUniversidad.UVI.models.historial.repositorys;

import ProyUniversidad.UVI.models.historial.models.HistorialAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialAcademicoRepository extends JpaRepository<HistorialAcademico, Long> {

    List<HistorialAcademico> findByAlumnoId(Long alumnoId);

    @Query("SELECT COUNT(h) > 0 FROM HistorialAcademico h WHERE h.alumnoId = :alumnoId AND h.cursoId = :cursoId AND h.estado = 'APROBADO'")
    boolean haAprobadoCurso(@Param("alumnoId") Long alumnoId, @Param("cursoId") Long cursoId);

    @Query("SELECT h FROM HistorialAcademico h WHERE h.alumnoId = :alumnoId AND h.cicloId = :cicloId AND h.estado = 'MATRICULADO'")
    List<HistorialAcademico> findCursosMatriculadosEnCiclo(@Param("alumnoId") Long alumnoId,
            @Param("cicloId") Long cicloId);
}