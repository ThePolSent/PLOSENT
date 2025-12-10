package ProyUniversidad.UVI.models.matricula.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "matriculas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {

    @Id
    private String id;

    private Long alumnoId;
    private String codigoUniversitario; 
    private String nombreAlumno;        

    private Long cicloId;

    private List<DetalleCursoMatricula> cursos;

    private Integer totalCreditos;
    private Double costoTotal;
    
    private String estado;
    private String codigoComprobante;

    private LocalDateTime fechaMatricula;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleCursoMatricula {
        private String cursoCodigo;
        private String cursoNombre;
        private Integer creditos;
        private Long seccionId;
        private String seccionCodigo;
        private List<String> horariosTexto;
    }
}