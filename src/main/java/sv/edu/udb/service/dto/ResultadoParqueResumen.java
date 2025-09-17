package sv.edu.udb.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultadoParqueResumen {
    private final Long parqueId;
    private final int anio;
    private final double stockCarbonoT;  // toneladas
    private final double capturaAnualT;  // toneladas
}
