package sv.edu.udb.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RecalculoResultadoRequest {
    @NotNull private Long parqueId;
    @NotNull private Integer anio;
}
