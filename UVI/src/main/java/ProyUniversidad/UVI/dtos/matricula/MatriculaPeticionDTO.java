package ProyUniversidad.UVI.dtos.matricula;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaPeticionDTO {

    @NotNull(message = "El ID del alumno es obligatorio")
    private Long alumnoId;

    @NotNull(message = "El ID del ciclo académico es obligatorio")
    private Long cicloId;

    @NotEmpty(message = "Debe seleccionar al menos un curso/sección")
    private List<Long> seccionesIds; 
}