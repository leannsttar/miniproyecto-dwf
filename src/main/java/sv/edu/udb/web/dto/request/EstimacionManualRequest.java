package sv.edu.udb.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EstimacionManualRequest {
    @NotNull @DecimalMin("0.0") private Long medicionId;
    @NotNull @DecimalMin("0.0") private Double biomasaKg;
    @NotNull @DecimalMin("0.0") private Double carbonoKg;
    @NotNull @DecimalMin("0.0") private Double co2eKg;
    @NotNull @DecimalMin("0.0") private Double fraccionCarbono;
    private Double incertidumbrePorc;
    private String notas;
}
