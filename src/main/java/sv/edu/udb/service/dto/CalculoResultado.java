package sv.edu.udb.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalculoResultado {
    private final double agbKg;     // biomasa aérea (kg)
    private final double carbonoKg; // carbono (kg)
    private final double co2eKg;    // CO2 equivalente (kg)
}
