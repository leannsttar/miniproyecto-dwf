package sv.edu.udb.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ArbolRequest {
    @NotNull
    private Long parqueId;

    @NotNull
    private Long especieId;

    private Double lat;
    private Double lon;
}
