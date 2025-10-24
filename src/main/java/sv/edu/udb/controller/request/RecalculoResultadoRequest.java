package sv.edu.udb.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecalculoResultadoRequest {
    @NotNull private Long parqueId;
    @NotNull private Integer anio;
}
