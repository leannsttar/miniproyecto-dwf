package sv.edu.udb.controller.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EspecieResponse {
    private Long id;
    private String nombreCientifico;
    private String nombreComun;
    private BigDecimal densidadMaderaRho;
    private String fuenteRho;
    private String versionRho;
}
