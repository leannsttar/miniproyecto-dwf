package sv.edu.udb.web.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimacionResponse {
    private Long id;
    private Long medicionId;
    private Double biomasaKg;
    private Double carbonoKg;
    private Double co2eKg;
    private Double fraccionCarbono;
    private Double incertidumbrePorc;
    private String notas;
}
