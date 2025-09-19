package sv.edu.udb.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.domain.*;
import sv.edu.udb.repository.*;
import sv.edu.udb.service.dto.ResultadoParqueResumen;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ResultadoParqueServiceImplTest {

    @Autowired private ResultadoParqueService resultadoService;
    @Autowired private ParqueRepository parqueRepository;
    @Autowired private EspecieRepository especieRepository;
    @Autowired private ArbolRepository arbolRepository;
    @Autowired private MedicionRepository medicionRepository;
    @Autowired private EstimacionRepository estimacionRepository;
    @Autowired private ResultadoParqueRepository resultadoParqueRepository;

    // -------- Helpers ----------
    private Parque nuevoParque(double areaHa) {
        Parque p = new Parque();
        p.setNombre("Parque Test");
        p.setDistrito("Centro");
        p.setAreaHa(areaHa);
        return parqueRepository.save(p);
    }

    private Especie nuevaEspecie() {
        Especie e = new Especie();
        e.setNombreCientifico("Mi especia odorata");
        e.setNombreComun("Cedro");
        e.setDensidadMaderaRho(new BigDecimal("0.52"));
        e.setFuenteRho("Zanne 2009");
        e.setVersionRho("v1");
        return especieRepository.save(e);
    }

    private Arbol nuevoArbol(Parque p, Especie e) {
        Arbol a = new Arbol();
        a.setParque(p);
        a.setEspecie(e);
        return arbolRepository.save(a);
    }

    private Medicion nuevaMedicion(Arbol a, int anio, int mes, int dia, double dbh, double h) {
        Medicion m = new Medicion();
        m.setArbol(a);
        m.setFecha(LocalDate.of(anio, mes, dia));
        m.setDbhCm(dbh);
        m.setAlturaM(h);
        return medicionRepository.save(m);
    }

    private Estimacion nuevaEstimacion(Medicion m, double biomasaKg, double carbonoKg, double co2eKg) {
        Estimacion est = new Estimacion();
        est.setMedicion(m);
        est.setBiomasaKg(biomasaKg);
        est.setCarbonoKg(carbonoKg);
        est.setCo2eKg(co2eKg);
        est.setFraccionCarbono(0.47);
        return estimacionRepository.save(est);
    }

    // -------- Tests -----------

    @Test
    @Transactional
    @DisplayName("Suma solo estimaciones del año objetivo; captura correcta para 1 ha")
    void sumaSoloAnioObjetivoYCaptura1Ha() {
        Parque p = nuevoParque(1.0);
        Especie e = nuevaEspecie();
        Arbol a = nuevoArbol(p, e);

        // 2025 (deben contarse)
        var m1_2025 = nuevaMedicion(a, 2025, 2,  1, 30.0, 15.0);
        var m2_2025 = nuevaMedicion(a, 2025, 5, 10, 32.0, 16.0);
        nuevaEstimacion(m1_2025, 439.234, 206.440, 756.947);
        nuevaEstimacion(m2_2025, 470.000, 220.900, 809.967);

        // 2024 (NO debe contarse)
        var m3_2024 = nuevaMedicion(a, 2024, 11, 20, 28.0, 14.0);
        nuevaEstimacion(m3_2024, 380.0, 178.6, 654.2);

        ResultadoParqueResumen r = resultadoService.recalcular(p.getId(), 2025);

        // Captura para 1 ha: 0.28 kg/m²·año * 10,000 m² = 2800 kg = 2.8 t
        assertEquals(2.8, r.getCapturaAnualT(), 1e-9);

        // Stock: solo 2025 → (206.440 + 220.900) / 1000 t
        assertEquals((206.440 + 220.900)/1000.0, r.getStockCarbonoT(), 1e-9);
    }

    @Test
    @Transactional
    @DisplayName("Parque sin estimaciones: stock = 0, captura según área")
    void parqueSinEstimaciones() {
        Parque p = nuevoParque(2.0); // 2 ha ⇒ 20,000 m²
        ResultadoParqueResumen r = resultadoService.recalcular(p.getId(), 2025);
        assertEquals(0.0, r.getStockCarbonoT(), 1e-9);
        // 0.28 * 20,000 / 1000 = 5.6 t
        assertEquals(5.6, r.getCapturaAnualT(), 1e-9);
    }

    @Test
    @Transactional
    @DisplayName("Upsert: si ya existe resultado (parque, anio) lo actualiza y no duplica")
    void upsertNoDuplica() {
        Parque p = nuevoParque(1.0);
        Especie e = nuevaEspecie();
        Arbol a = nuevoArbol(p, e);

        var m1 = nuevaMedicion(a, 2025, 3, 5, 30.0, 15.0);
        nuevaEstimacion(m1, 439.234, 206.440, 756.947);

        // 1a recalculación
        var r1 = resultadoService.recalcular(p.getId(), 2025);
        long countAfterFirst = resultadoParqueRepository.count();

        // Agrego otra estimación en el mismo año
        var m2 = nuevaMedicion(a, 2025, 8, 12, 32.0, 16.0);
        nuevaEstimacion(m2, 470.000, 220.900, 809.967);

        // 2a recalculación (debe actualizar el mismo registro)
        var r2 = resultadoService.recalcular(p.getId(), 2025);
        long countAfterSecond = resultadoParqueRepository.count();

        assertEquals(r1.getParqueId(), r2.getParqueId());
        assertEquals(r1.getAnio(), r2.getAnio());
        assertEquals(countAfterFirst, countAfterSecond, "No debe crear duplicados");
    }

    @Test
    @DisplayName("Parque no existe ⇒ EntityNotFoundException")
    void parqueNoExiste() {
        assertThrows(jakarta.persistence.EntityNotFoundException.class,
                () -> resultadoService.recalcular(9999L, 2025));
    }

    @Test
    @DisplayName("Parámetros inválidos ⇒ IllegalArgumentException")
    void parametrosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> resultadoService.recalcular(0L, 2025));
        assertThrows(IllegalArgumentException.class, () -> resultadoService.recalcular(1L, 0));
    }

    @Test
    @Transactional
    @DisplayName("Captura correcta para diferentes áreas (0.5ha, 1ha, 3ha)")
    void capturaDiferentesAreas() {
        // 0.5 ha -> 5000 m2 -> 0.28*5000/1000 = 1.4 t
        assertEquals(1.4, resultadoService.recalcular(nuevoParque(0.5).getId(), 2025).getCapturaAnualT(), 1e-9);
        // 1 ha -> 10000 m2 -> 2.8 t
        assertEquals(2.8, resultadoService.recalcular(nuevoParque(1.0).getId(), 2025).getCapturaAnualT(), 1e-9);
        // 3 ha -> 30000 m2 -> 8.4 t
        assertEquals(8.4, resultadoService.recalcular(nuevoParque(3.0).getId(), 2025).getCapturaAnualT(), 1e-9);
    }
}
