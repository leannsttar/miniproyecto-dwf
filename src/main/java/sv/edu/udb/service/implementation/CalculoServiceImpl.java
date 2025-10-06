package sv.edu.udb.service.implementation;

import org.springframework.stereotype.Service;
import sv.edu.udb.service.CalculoService;
import sv.edu.udb.service.dto.CalculoResultado;

@Service
public class CalculoServiceImpl implements CalculoService {

    // IPCC: fracción de carbono promedio
    private static final double CF_DEFAULT = 0.47;

    // Estequiometría: (masa molar CO2) / (masa molar C) = 44/12
    private static final double CO2_OVER_C = 44.0 / 12.0;

    @Override
    public CalculoResultado calcular(double rho, double dCm, double hM, Double fraccionCarbono) {

        // Validaciones de rango y unidades:
        // - rho: g/cm³
        // - dCm: cm
        // - hM : m
        if (rho < 0.1 || rho > 1.5) {
            throw new IllegalArgumentException("ρ fuera de rango (0.1 ≤ ρ ≤ 1.5 g/cm³)");
        }
        if (dCm <= 0) {
            throw new IllegalArgumentException("D debe ser > 0 cm");
        }
        if (hM <= 0) {
            throw new IllegalArgumentException("H debe ser > 0 m");
        }

        // Fórmula Chave 2014 (pantropical con altura): AGB = 0.0673 × (ρ · D² · H)^0.976
        double agbKg = 0.0673 * Math.pow(rho * Math.pow(dCm, 2) * hM, 0.976);

        // Si fraccionCarbono es null, usar valor por defecto (IPCC 0.47)
        double cf = (fraccionCarbono != null ? fraccionCarbono : CF_DEFAULT);

        // Conversiones:
        // C (kg)    = AGB (kg) × cf
        // CO2e (kg) = C (kg) × (44/12)
        double carbonoKg = agbKg * cf;
        double co2eKg = carbonoKg * CO2_OVER_C;

        return new CalculoResultado(agbKg, carbonoKg, co2eKg);
    }
}
