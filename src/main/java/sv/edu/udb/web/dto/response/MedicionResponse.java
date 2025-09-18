package sv.edu.udb.web.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MedicionResponse {
    private Long id;
    private Long arbolId;
    private LocalDate fecha;
    private Double dbhCm;
    private Double alturaM;
    private String observaciones;
}
