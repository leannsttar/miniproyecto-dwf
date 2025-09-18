package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.domain.*;
import sv.edu.udb.repository.*;
import sv.edu.udb.service.impl.CalculoServiceImpl;
import sv.edu.udb.service.impl.EstimacionServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test del flujo createFromMedicion: usa ρ de la especie, D y H de la medición.
 */
@DataJpaTest
@Import({EstimacionServiceImpl.class, CalculoServiceImpl.class})
@ActiveProfiles("test")
class EstimacionServiceImplTest {

    @Autowired EstimacionService service;
    @Autowired ParqueRepository parqueRepo;
    @Autowired EspecieRepository especieRepo;
    @Autowired ArbolRepository arbolRepo;
    @Autowired MedicionRepository medicionRepo;

    @Test @DisplayName("createFromMedicion - calcula y persiste una estimación")
    void createFromMedicion_ok() {
        Parque p = new Parque(); p.setNombre("P"); p.setDistrito("D"); p.setAreaHa(1.0); parqueRepo.save(p);
        Especie e = new Especie(); e.setNombreCientifico("Cedrela"); e.setDensidadMaderaRho(new BigDecimal("0.60")); e.setFuenteRho("FAO"); e.setVersionRho("v1"); especieRepo.save(e);
        Arbol a = new Arbol(); a.setParque(p); a.setEspecie(e); arbolRepo.save(a);
        Medicion m = new Medicion(); m.setArbol(a); m.setFecha(LocalDate.of(2025,1,1)); m.setDbhCm(30.0); m.setAlturaM(15.0); medicionRepo.save(m);

        Estimacion est = service.createFromMedicion(m.getId(), 0.47);
        assertNotNull(est.getId());
        assertEquals(m.getId(), est.getMedicion().getId());
        assertTrue(est.getCarbonoKg() > 0);
        assertTrue(est.getCo2eKg() > 0);
    }

    @Test @DisplayName("createFromMedicion - lanza NOT FOUND si la medición no existe")
    void createFromMedicion_notFound() {
        assertThrows(EntityNotFoundException.class, () -> service.createFromMedicion(999L, 0.47));
    }
}
