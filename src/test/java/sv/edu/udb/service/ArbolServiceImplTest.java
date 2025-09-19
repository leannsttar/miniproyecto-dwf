package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.impl.ArbolServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Service test con JPA real (slice).
 * Importa solo la implementación del servicio bajo prueba.
 */
@DataJpaTest
@Import(ArbolServiceImpl.class)
@ActiveProfiles("test")
class ArbolServiceImplTest {

    @Autowired ArbolService arbolService;
    @Autowired ParqueRepository parqueRepo;
    @Autowired EspecieRepository especieRepo;
    @Autowired
    ArbolRepository arbolRepo;

    private Parque crearParque() {
        Parque p = new Parque();
        p.setNombre("P"); p.setDistrito("D"); p.setAreaHa(1.0);
        return parqueRepo.save(p);
    }
    private Especie crearEspecie() {
        Especie e = new Especie();
        e.setNombreCientifico("Cedrela odorata"); e.setDensidadMaderaRho(new BigDecimal("0.52"));
        e.setFuenteRho("FAO"); e.setVersionRho("v1");
        return especieRepo.save(e);
    }

    @Test @DisplayName("create - valida requeridos y persiste correctamente")
    void create_ok() {
        Parque p = crearParque();
        Especie e = crearEspecie();

        Arbol a = new Arbol();
        a.setParque(p);
        a.setEspecie(e);
        a.setLat(13.0);
        a.setLon(-89.0);

        Arbol saved = arbolService.create(a);
        assertNotNull(saved.getId());
        assertEquals(p.getId(), saved.getParque().getId());
        assertEquals(e.getId(), saved.getEspecie().getId());
    }

    @Test @DisplayName("create - falla si falta parque_id o especie_id")
    void create_failsOnMissingRefs() {
        Arbol sinParque = new Arbol();
        sinParque.setEspecie(crearEspecie());
        assertThrows(IllegalArgumentException.class, () -> arbolService.create(sinParque));

        Arbol sinEspecie = new Arbol();
        sinEspecie.setParque(crearParque());
        assertThrows(IllegalArgumentException.class, () -> arbolService.create(sinEspecie));
    }

    @Test @DisplayName("update - cambia especie y lat/lon")
    void update_ok() {
        Parque p = crearParque();
        Especie e1 = crearEspecie();
        Arbol a = new Arbol(); a.setParque(p); a.setEspecie(e1); a.setLat(1.0); a.setLon(2.0);
        a = arbolRepo.save(a);

        Especie e2 = new Especie();
        e2.setNombreCientifico("Tabebuia rosea"); e2.setDensidadMaderaRho(new BigDecimal("0.65"));
        e2.setFuenteRho("FAO"); e2.setVersionRho("v2");
        e2 = especieRepo.save(e2);

        Arbol patch = new Arbol(); patch.setEspecie(e2); patch.setLat(9.0); patch.setLon(8.0);
        Arbol updated = arbolService.update(a.getId(), patch);

        assertEquals(e2.getId(), updated.getEspecie().getId());
        assertEquals(9.0, updated.getLat());
        assertEquals(8.0, updated.getLon());
    }

    @Test @DisplayName("delete - lanza NOT FOUND si no existe")
    void delete_notFound() {
        assertThrows(EntityNotFoundException.class, () -> arbolService.delete(999L));
    }
}
