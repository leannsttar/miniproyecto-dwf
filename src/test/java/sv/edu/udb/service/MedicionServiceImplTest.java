package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.controller.request.MedicionRequest;
import sv.edu.udb.controller.response.MedicionResponse;
import sv.edu.udb.repository.domain.Arbol;
import sv.edu.udb.repository.domain.Especie;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.MedicionRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.implementation.MedicionServiceImpl;
import sv.edu.udb.service.mapper.MedicionMapperImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({MedicionServiceImpl.class, MedicionMapperImpl.class})
@ActiveProfiles("test")
class MedicionServiceImplTest {

    @Autowired MedicionService medicionService;
    @Autowired ParqueRepository parqueRepo;
    @Autowired EspecieRepository especieRepo;
    @Autowired ArbolRepository arbolRepo;
    @Autowired MedicionRepository medicionRepo;

    private Arbol crearArbol() {
        Parque p = new Parque();
        p.setNombre("P"); p.setDistrito("D"); p.setAreaHa(1.0);
        parqueRepo.save(p);

        Especie e = new Especie();
        e.setNombreCientifico("Cedrela");
        e.setDensidadMaderaRho(new BigDecimal("0.52"));
        e.setFuenteRho("FAO");
        e.setVersionRho("v1");
        especieRepo.save(e);

        Arbol a = new Arbol();
        a.setParque(p); a.setEspecie(e);
        return arbolRepo.save(a);
    }

    @Test
    void save_ok() {
        Arbol a = crearArbol();
        MedicionRequest req = new MedicionRequest();
        req.setArbolId(a.getId());
        req.setFecha(LocalDate.now());
        req.setDbhCm(20.0);
        req.setAlturaM(10.0);
        req.setObservaciones("Prueba");

        MedicionResponse saved = medicionService.save(req);
        assertNotNull(saved.getId());
        assertEquals(a.getId(), saved.getArbolId());
    }

    @Test
    void findById_notFound() {
        assertThrows(EntityNotFoundException.class, () -> medicionService.findById(999L));
    }

    @Test
    void update_changesValues() {
        Arbol a = crearArbol();

        // Crear primero
        MedicionRequest req = new MedicionRequest();
        req.setArbolId(a.getId());
        req.setFecha(LocalDate.of(2024, 1, 1));
        req.setDbhCm(10.0);
        req.setAlturaM(5.0);
        req.setObservaciones("Inicial");
        MedicionResponse created = medicionService.save(req);

        // Actualizar
        MedicionRequest updateReq = new MedicionRequest();
        updateReq.setArbolId(a.getId());
        updateReq.setFecha(LocalDate.of(2024, 2, 1));
        updateReq.setDbhCm(12.0);
        updateReq.setAlturaM(6.0);
        updateReq.setObservaciones("Actualizada");

        MedicionResponse updated = medicionService.update(created.getId(), updateReq);

        assertEquals(12.0, updated.getDbhCm());
        assertEquals(6.0, updated.getAlturaM());
        assertEquals("Actualizada", updated.getObservaciones());
    }

    @Test
    void delete_ok_then_notFound() {
        Arbol a = crearArbol();

        MedicionRequest req = new MedicionRequest();
        req.setArbolId(a.getId());
        req.setFecha(LocalDate.now());
        req.setDbhCm(15.0);
        req.setAlturaM(8.0);
        MedicionResponse saved = medicionService.save(req);

        medicionService.delete(saved.getId());
        assertThrows(EntityNotFoundException.class, () -> medicionService.findById(saved.getId()));
    }
}
