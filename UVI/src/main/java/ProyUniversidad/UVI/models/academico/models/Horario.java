package ProyUniversidad.UVI.models.academico.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "horarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Horario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seccion_id", nullable = false)
  private Seccion seccion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "aula_id")
  private Aula aula;

  @Column(name = "dia_semana", nullable = false, length = 20)
  private String diaSemana; 

  @Column(name = "hora_inicio", nullable = false)
  private LocalTime horaInicio;

  @Column(name = "hora_fin", nullable = false)
  private LocalTime horaFin;

  @Column(length = 50)
  private String tipo; 
}