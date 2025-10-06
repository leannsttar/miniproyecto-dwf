package sv.edu.udb.controller.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstimacionDesdeMedicionRequest {
    @NotNull
    private Long medicionId;

    // opcional; si null usa 0.47
    @DecimalMin("0.0")
    private Double fraccionCarbono;
}
