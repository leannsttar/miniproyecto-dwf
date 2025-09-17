package sv.edu.udb.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sv.edu.udb.service.dto.CalculoResultado;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalculoServiceImplTest {

    @Autowired
    private CalculoService service;

    @Test
    @DisplayName("Calcula valores numéricos conocidos (ρ=0.6, D=30, H=15, CF=0.47)")
    void calculaValoresConocidos() {
        CalculoResultado r = service.calcular(0.6, 30.0, 15.0, 0.47);
        // Valores de referencia (tolerancia 1e-3)
        assertEquals(439.234, r.getAgbKg(), 1e-3);
        assertEquals(206.440, r.getCarbonoKg(), 1e-3);
        assertEquals(756.947, r.getCo2eKg(), 1e-3);
    }

    @Test
    @DisplayName("Usa CF por defecto (0.47) cuando fraccionCarbono es null")
    void usaFraccionPorDefecto() {
        CalculoResultado rNull = service.calcular(0.6, 30.0, 15.0, null);
        CalculoResultado r047  = service.calcular(0.6, 30.0, 15.0, 0.47);
        assertEquals(r047.getCarbonoKg(), rNull.getCarbonoKg(), 1e-9);
        assertEquals(r047.getCo2eKg(), rNull.getCo2eKg(), 1e-9);
    }

    @Test
    @DisplayName("Usa CF personalizada cuando se proporciona")
    void usaFraccionPersonalizada() {
        CalculoResultado r = service.calcular(0.6, 30.0, 15.0, 0.5);
        // Carbono = AGB * 0.5; CO2e = Carbono * (44/12)
        assertEquals(r.getAgbKg() * 0.5, r.getCarbonoKg(), 1e-9);
        assertEquals(r.getCarbonoKg() * (44.0/12.0), r.getCo2eKg(), 1e-9);
    }

    @Test
    @DisplayName("Monotonicidad: más D ⇒ más AGB (manteniendo ρ y H)")
    void monotonicidadDiametro() {
        var r1 = service.calcular(0.6, 20.0, 15.0, 0.47);
        var r2 = service.calcular(0.6, 30.0, 15.0, 0.47);
        var r3 = service.calcular(0.6, 40.0, 15.0, 0.47);
        assertTrue(r1.getAgbKg() < r2.getAgbKg() && r2.getAgbKg() < r3.getAgbKg());
    }

    @Test
    @DisplayName("Monotonicidad: más H ⇒ más AGB (manteniendo ρ y D)")
    void monotonicidadAltura() {
        var r1 = service.calcular(0.6, 30.0, 10.0, 0.47);
        var r2 = service.calcular(0.6, 30.0, 15.0, 0.47);
        var r3 = service.calcular(0.6, 30.0, 20.0, 0.47);
        assertTrue(r1.getAgbKg() < r2.getAgbKg() && r2.getAgbKg() < r3.getAgbKg());
    }

    @Test
    @DisplayName("Monotonicidad: más ρ ⇒ más AGB (manteniendo D y H)")
    void monotonicidadDensidad() {
        var r1 = service.calcular(0.4, 30.0, 15.0, 0.47);
        var r2 = service.calcular(0.6, 30.0, 15.0, 0.47);
        var r3 = service.calcular(0.9, 30.0, 15.0, 0.47);
        assertTrue(r1.getAgbKg() < r2.getAgbKg() && r2.getAgbKg() < r3.getAgbKg());
    }

    @Test
    @DisplayName("Valida rangos: ρ fuera de [0.1,1.5], D<=0, H<=0 ⇒ IAE")
    void validaRangos() {
        assertThrows(IllegalArgumentException.class, () -> service.calcular(0.05, 30.0, 15.0, 0.47));
        assertThrows(IllegalArgumentException.class, () -> service.calcular(1.6,  30.0, 15.0, 0.47));
        assertThrows(IllegalArgumentException.class, () -> service.calcular(0.6,   0.0, 15.0, 0.47));
        assertThrows(IllegalArgumentException.class, () -> service.calcular(0.6,  30.0,  0.0, 0.47));
    }

    @Test
    @DisplayName("Valores grandes siguen siendo finitos (sin NaN/Infinity)")
    void valoresGrandesSonFinitos() {
        var r = service.calcular(1.5, 200.0, 50.0, 0.47); // grande pero razonable
        assertTrue(Double.isFinite(r.getAgbKg()));
        assertTrue(Double.isFinite(r.getCarbonoKg()));
        assertTrue(Double.isFinite(r.getCo2eKg()));
    }

    @Test
    @DisplayName("Determinismo: misma entrada ⇒ mismo resultado")
    void determinismo() {
        var r1 = service.calcular(0.7, 33.0, 18.0, 0.47);
        var r2 = service.calcular(0.7, 33.0, 18.0, 0.47);
        assertEquals(r1.getAgbKg(), r2.getAgbKg(), 0.0);
        assertEquals(r1.getCarbonoKg(), r2.getCarbonoKg(), 0.0);
        assertEquals(r1.getCo2eKg(), r2.getCo2eKg(), 0.0);
    }
}
