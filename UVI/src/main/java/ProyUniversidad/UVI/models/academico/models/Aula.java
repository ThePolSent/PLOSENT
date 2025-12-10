package ProyUniversidad.UVI.models.academico.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aulas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aula {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 20)
  private String codigo; 

  @Column(nullable = false, length = 100)
  private String nombre; 

  @Column(length = 50)
  private String edificio; 

  @Column(length = 10)
  private String piso; 

  private Integer capacidad;

  @Column(length = 50)
  private String tipo; 

  @Column(columnDefinition = "TEXT")
  private String equipamiento; 

  @Column(nullable = false)
  private Boolean activo = true;
}