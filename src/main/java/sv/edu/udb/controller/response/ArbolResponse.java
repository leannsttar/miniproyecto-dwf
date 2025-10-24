package sv.edu.udb.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArbolResponse {
    private Long id;
    private Long parqueId;
    private Long especieId;
    private Double lat;
    private Double lon;
}
