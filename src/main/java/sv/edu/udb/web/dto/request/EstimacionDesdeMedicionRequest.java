package sv.edu.udb.web.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EstimacionDesdeMedicionRequest {
    private Long medicionId;       // requerido en controller con @NotNull si prefieres
    private Double fraccionCarbono; // opcional; si null usa 0.47
}
