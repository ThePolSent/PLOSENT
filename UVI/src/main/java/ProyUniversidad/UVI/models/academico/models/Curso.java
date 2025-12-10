package ProyUniversidad.UVI.models.academico.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 20)
  private String codigo;

  @Column(nullable = false, length = 150)
  private String nombre;

  @Column(columnDefinition = "TEXT")
  private String descripcion;

  @Column(nullable = false)
  private Integer creditos;

  @Column(nullable = false)
  private Integer ciclo; 

  @Column(name = "horas_teoricas")
  private Integer horasTeoricas;

  @Column(name = "horas_practicas")
  private Integer horasPracticas;

  @Column(length = 100)
  private String departamento;

  @Column(nullable = false)
  private Boolean activo = true;

  // --- RELACIONES CON @JsonIgnore (Para evitar errores) ---

  @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @JsonIgnore 
  private List<Seccion> secciones = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "curso_prerrequisitos",
      joinColumns = @JoinColumn(name = "curso_id"),
      inverseJoinColumns = @JoinColumn(name = "requisito_id")
  )
  @JsonIgnore 
  private List<Curso> prerrequisitos;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "curso_correquisitos",
      joinColumns = @JoinColumn(name = "curso_id"),
      inverseJoinColumns = @JoinColumn(name = "correquisito_id")
  )
  @JsonIgnore 
  private List<Curso> correquisitos;
}