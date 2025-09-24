package sv.edu.udb.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class EspecieRequest {

    @NotBlank
    private String nombreCientifico;
    private String nombreComun;

    @NotNull
    @DecimalMin("0.10")
    @DecimalMax("1.50")
    private BigDecimal densidadMaderaRho;

    @NotBlank
    private String fuenteRho;

    @NotBlank
    private String versionRho;
}
