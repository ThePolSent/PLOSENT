package ProyUniversidad.UVI.models.usuario.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@DiscriminatorValue("DOCENTE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DocenteUsuario extends Usuario {

  @Column(name = "codigo_docente", unique = true, length = 15)
  private String codigoDocente;

  @Column(length = 8)
  private String dni; 

  @Column(length = 100)
  private String emailPersonal;

  @Column(length = 100)
  private String departamento; 

  @Column(length = 50)
  private String tituloAcademico; 
}