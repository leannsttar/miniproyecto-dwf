package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.domain.Medicion;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.MedicionRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.impl.MedicionServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica validaciones de referencia (arbol_id) y actualización de campos.
 */
@DataJpaTest
@Import(MedicionServiceImpl.class)
@ActiveProfiles("test")
class MedicionServiceImplTest {

    @Autowired MedicionService medicionService;
    @Autowired ParqueRepository parqueRepo;
    @Autowired EspecieRepository especieRepo;
    @Autowired
    ArbolRepository arbolRepo;
    @Autowired MedicionRepository medicionRepo;

    private Arbol crearArbol() {
        Parque p = new Parque(); p.setNombre("P"); p.setDistrito("D"); p.setAreaHa(1.0); parqueRepo.save(p);
        Especie e = new Especie(); e.setNombreCientifico("Cedrela"); e.setDensidadMaderaRho(new BigDecimal("0.52")); e.setFuenteRho("FAO"); e.setVersionRho("v1"); especieRepo.save(e);
        Arbol a = new Arbol(); a.setParque(p); a.setEspecie(e);
        return arbolRepo.save(a);
    }

    @Test
    void create_requiresArbolId() {
        Medicion m = new Medicion();
        m.setFecha(LocalDate.now());
        m.setDbhCm(20.0);
        m.setAlturaM(10.0);
        assertThrows(IllegalArgumentException.class, () -> medicionService.create(m));
    }

    @Test
    void create_ok() {
        Arbol a = crearArbol();
        Medicion m = new Medicion();
        m.setArbol(a); m.setFecha(LocalDate.now()); m.setDbhCm(20.0); m.setAlturaM(10.0);
        Medicion saved = medicionService.create(m);
        assertNotNull(saved.getId());
        assertEquals(a.getId(), saved.getArbol().getId());
    }

    @Test
    void update_changesValues() {
        Arbol a = crearArbol();
        Medicion m = new Medicion(); m.setArbol(a); m.setFecha(LocalDate.of(2024,1,1)); m.setDbhCm(10.0); m.setAlturaM(5.0);
        m = medicionRepo.save(m);
        m.setDbhCm(12.0); m.setAlturaM(6.0);
        Medicion updated = medicionService.update(m.getId(), m);
        assertEquals(12.0, updated.getDbhCm());
        assertEquals(6.0, updated.getAlturaM());
    }

    @Test
    void findById_notFound() {
        assertThrows(EntityNotFoundException.class, () -> medicionService.findById(999L));
    }
}
