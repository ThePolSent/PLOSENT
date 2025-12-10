package ProyUniversidad.UVI.models.academico.models;

import com.fasterxml.jackson.annotation.JsonIgnore; 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ciclos_academicos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CicloAcademico {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 20)
  private String codigo;

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(name = "fecha_inicio", nullable = false)
  private LocalDate fechaInicio;

  @Column(name = "fecha_fin", nullable = false)
  private LocalDate fechaFin;

  @Column(name = "fecha_inicio_matricula")
  private LocalDate fechaInicioMatricula;

  @Column(name = "fecha_fin_matricula")
  private LocalDate fechaFinMatricula;

  @Column(nullable = false)
  private Boolean activo = true;

  @OneToMany(mappedBy = "cicloAcademico", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @JsonIgnore 
  private List<Seccion> secciones = new ArrayList<>();
}