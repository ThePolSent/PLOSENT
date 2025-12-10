package ProyUniversidad.UVI.models.academico.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "secciones", uniqueConstraints = @UniqueConstraint(columnNames = { "curso_id", "ciclo_academico_id",
    "codigo" }))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seccion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 10)
  private String codigo; 

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "curso_id", nullable = false)
  private Curso curso;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ciclo_academico_id", nullable = false)
  private CicloAcademico cicloAcademico;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "docente_id", nullable = false)
  private Docente docente;

  @Column(name = "vacantes_totales", nullable = false)
  private Integer vacantesTotales;

  @Column(name = "vacantes_ocupadas", nullable = false)
  private Integer vacantesOcupadas = 0;

  @Version
  @Column(name = "version")
  private Long version;

  @Column(length = 50)
  private String modalidad; 

  @Column(nullable = false)
  private Boolean activo = true;

  @OneToMany(mappedBy = "seccion", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Horario> horarios = new ArrayList<>();
}