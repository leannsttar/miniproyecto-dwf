package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.controller.request.EstimacionDesdeMedicionRequest;
import sv.edu.udb.controller.response.EstimacionResponse;
import sv.edu.udb.repository.*;
import sv.edu.udb.repository.domain.*;
import sv.edu.udb.service.implementation.CalculoServiceImpl;
import sv.edu.udb.service.implementation.EstimacionServiceImpl;
import sv.edu.udb.service.mapper.EstimacionMapperImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Flujo createDesdeMedicion: usa ρ de la especie, D y H de la medición.
 */
@DataJpaTest
@Import({EstimacionServiceImpl.class, CalculoServiceImpl.class, EstimacionMapperImpl.class})
@ActiveProfiles("test")
class EstimacionServiceImplTest {

    @Autowired EstimacionService service;
    @Autowired ParqueRepository parqueRepo;
    @Autowired EspecieRepository especieRepo;
    @Autowired ArbolRepository arbolRepo;
    @Autowired MedicionRepository medicionRepo;

    @Test
    @DisplayName("createDesdeMedicion - calcula y persiste una estimación")
    void createDesdeMedicion_ok() {
        Parque p = new Parque(); p.setNombre("P"); p.setDistrito("D"); p.setAreaHa(1.0); parqueRepo.save(p);
        Especie e = new Especie(); e.setNombreCientifico("Cedrela"); e.setDensidadMaderaRho(new BigDecimal("0.60")); e.setFuenteRho("FAO"); e.setVersionRho("v1"); especieRepo.save(e);
        Arbol a = new Arbol(); a.setParque(p); a.setEspecie(e); arbolRepo.save(a);
        Medicion m = new Medicion(); m.setArbol(a); m.setFecha(LocalDate.of(2025,1,1)); m.setDbhCm(30.0); m.setAlturaM(15.0); medicionRepo.save(m);

        EstimacionDesdeMedicionRequest req = new EstimacionDesdeMedicionRequest();
        req.setMedicionId(m.getId());
        req.setFraccionCarbono(0.47);

        EstimacionResponse est = service.createDesdeMedicion(req);

        assertNotNull(est.getId());
        assertEquals(m.getId(), est.getMedicionId());
        assertTrue(est.getCarbonoKg() > 0);
        assertTrue(est.getCo2eKg() > 0);
        assertEquals(0.47, est.getFraccionCarbono());
    }

    @Test
    @DisplayName("createDesdeMedicion - lanza NOT FOUND si la medición no existe")
    void createDesdeMedicion_notFound() {
        EstimacionDesdeMedicionRequest req = new EstimacionDesdeMedicionRequest();
        req.setMedicionId(999L);
        req.setFraccionCarbono(0.47);
        assertThrows(EntityNotFoundException.class, () -> service.createDesdeMedicion(req));
    }
}
