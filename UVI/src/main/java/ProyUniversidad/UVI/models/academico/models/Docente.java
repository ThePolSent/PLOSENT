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
@Table(name = "docentes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Docente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "usuario_id", nullable = false, unique = true)
  private Long usuarioId;

  @Column(name = "codigo_docente", unique = true, length = 15)
  private String codigoDocente;

  @Column(name = "nombre_completo", nullable = false, length = 150)
  private String nombreCompleto;

  @Column(length = 100)
  private String especialidad;

  @Column(length = 50)
  private String categoria;

  @Column(name = "grado_academico", length = 100)
  private String gradoAcademico;

  @Column(length = 100)
  private String departamento;

  @Column(columnDefinition = "TEXT")
  private String curriculum;

  @Column(nullable = false)
  private Boolean activo = true;

  
  @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @JsonIgnore 
  private List<Seccion> secciones = new ArrayList<>();
}