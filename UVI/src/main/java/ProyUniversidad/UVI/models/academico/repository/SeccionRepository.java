package ProyUniversidad.UVI.models.academico.repository;

import ProyUniversidad.UVI.models.academico.models.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeccionRepository extends JpaRepository<Seccion, Long> {
  List<Seccion> findByCicloAcademicoIdAndActivoTrue(Long cicloId);

  List<Seccion> findByCursoIdAndCicloAcademicoId(Long cursoId, Long cicloId);

  List<Seccion> findByDocenteIdAndCicloAcademicoId(Long docenteId, Long cicloId);

  @Query("SELECT s FROM Seccion s WHERE s.curso.id = :cursoId " +
      "AND s.cicloAcademico.id = :cicloId AND s.codigo = :codigo")
  Optional<Seccion> findByCursoAndCicloAndCodigo(
      @Param("cursoId") Long cursoId,
      @Param("cicloId") Long cicloId,
      @Param("codigo") String codigo);

  @Query("SELECT s FROM Seccion s WHERE s.vacantesOcupadas < s.vacantesTotales " +
      "AND s.cicloAcademico.id = :cicloId AND s.activo = true")
  List<Seccion> findSeccionesConVacantes(@Param("cicloId") Long cicloId);
}