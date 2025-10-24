package sv.edu.udb.service;

import sv.edu.udb.service.dto.CalculoResultado;

public interface CalculoService {

    /**
     * Calcula biomasa aérea, carbono y CO2e usando la fórmula de Chave 2014 (con altura).
     * @param rho densidad de la madera (g/cm3), 0.1 ≤ rho ≤ 1.5
     * @param dCm diámetro a la altura del pecho (cm) > 0
     * @param hM  altura total (m) > 0
     * @param fraccionCarbono fracción de carbono (si es null se usa 0.47)
     *  Salidas:
     *  - AGB (kg), C (kg), CO₂e (kg)
     */
    CalculoResultado calcular(double rho, double dCm, double hM, Double fraccionCarbono);
}
