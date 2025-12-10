package ProyUniversidad.UVI.models.usuario.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@DiscriminatorValue("ALUMNO")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Alumno extends Usuario {

  @Column(name = "codigo_universitario", unique = true, length = 15)
  private String codigoUniversitario;


  @Column(length = 8)
  private String dni; 

  @Column(length = 100)
  private String emailPersonal;

  @Column(length = 100)
  private String carrera; 

  private Integer cicloActual;
  private Double promedioGeneral;

  @Column(name = "fecha_turno_matricula")
  private LocalDateTime fechaTurnoMatricula; 
}