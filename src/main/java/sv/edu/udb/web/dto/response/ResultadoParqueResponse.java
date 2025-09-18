package sv.edu.udb.web.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultadoParqueResponse {
    private Long parqueId;
    private int anio;
    private Double stockCarbonoT;
    private Double capturaAnualT;
}
