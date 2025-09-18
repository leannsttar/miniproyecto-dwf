package sv.edu.udb.web.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParqueResponse {
    private Long id;
    private String nombre;
    private String distrito;
    private Double areaHa;
    private Double lat;
    private Double lon;
}
