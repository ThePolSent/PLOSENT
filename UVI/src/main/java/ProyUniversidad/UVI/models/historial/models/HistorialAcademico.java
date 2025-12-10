package ProyUniversidad.UVI.models.historial.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_academico", indexes = {
    @Index(name = "idx_alumno_curso", columnList = "alumno_id, curso_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alumno_id", nullable = false)
    private Long alumnoId;

    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

    @Column(name = "ciclo_id", nullable = false)
    private Long cicloId; 

    @Column(nullable = false)
    private Double nota; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCurso estado; 

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    public enum EstadoCurso {
        APROBADO,
        DESAPROBADO,
        RETIRADO,
        MATRICULADO 
    }
}