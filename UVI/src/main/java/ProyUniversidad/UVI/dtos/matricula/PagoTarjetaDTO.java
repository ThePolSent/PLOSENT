package ProyUniversidad.UVI.dtos.matricula;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PagoTarjetaDTO {
    @NotBlank
    private String matriculaId;
    @NotBlank
    private String numeroTarjeta;
    @NotBlank
    private String caducidad;
    @NotBlank
    private String cvv;
    @NotBlank
    private String titular;
    @NotBlank
    private String email;
}