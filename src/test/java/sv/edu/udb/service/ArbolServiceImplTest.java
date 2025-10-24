package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.repository.domain.Especie;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.implementation.ArbolServiceImpl;
import sv.edu.udb.controller.request.ArbolRequest;
import sv.edu.udb.controller.response.ArbolResponse;
import sv.edu.udb.service.mapper.ArbolMapperImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ArbolServiceImpl.class, ArbolMapperImpl.class})
@ActiveProfiles("test")
class ArbolServiceImplTest {

    @Autowired ArbolService arbolService;
    @Autowired ParqueRepository parqueRepo;
    @Autowired EspecieRepository especieRepo;
    @Autowired ArbolRepository arbolRepo;

    private Parque crearParque() {
        Parque p = new Parque();
        p.setNombre("P");
        p.setDistrito("D");
        p.setAreaHa(1.0);
        return parqueRepo.save(p);
    }

    private Especie crearEspecie(String nc, String rho) {
        Especie e = new Especie();
        e.setNombreCientifico(nc);
        e.setDensidadMaderaRho(new BigDecimal(rho));
        e.setFuenteRho("FAO");
        e.setVersionRho("v1");
        return especieRepo.save(e);
    }

    @Test
    @DisplayName("save: crea arbol con refs válidas y devuelve DTO")
    void save_ok() {
        Parque p = crearParque();
        Especie e = crearEspecie("Cedrela odorata", "0.52");

        ArbolRequest req = new ArbolRequest();
        req.setParqueId(p.getId());
        req.setEspecieId(e.getId());
        req.setLat(13.0);
        req.setLon(-89.0);

        ArbolResponse out = arbolService.save(req);

        assertNotNull(out.getId());
        assertEquals(p.getId(), out.getParqueId());
        assertEquals(e.getId(), out.getEspecieId());
        assertEquals(13.0, out.getLat());
        assertEquals(-89.0, out.getLon());
    }

    @Test
    @DisplayName("findById: 404 si no existe")
    void findById_404() {
        assertThrows(EntityNotFoundException.class, () -> arbolService.findById(999L));
    }

    @Test
    @DisplayName("update: cambia especie y coords, mantiene parque")
    void update_ok() {
        Parque p = crearParque();
        Especie e1 = crearEspecie("Cedrela odorata", "0.52");

        // crear
        ArbolRequest create = new ArbolRequest();
        create.setParqueId(p.getId());
        create.setEspecieId(e1.getId());
        create.setLat(1.0);
        create.setLon(2.0);
        ArbolResponse created = arbolService.save(create);

        // actualizar
        Especie e2 = crearEspecie("Tabebuia rosea", "0.65");

        ArbolRequest upd = new ArbolRequest();
        upd.setParqueId(p.getId()); // sigue siendo el mismo parque (DTO tiene @NotNull)
        upd.setEspecieId(e2.getId());
        upd.setLat(9.0);
        upd.setLon(8.0);

        ArbolResponse updated = arbolService.update(created.getId(), upd);

        assertEquals(e2.getId(), updated.getEspecieId());
        assertEquals(p.getId(), updated.getParqueId());
        assertEquals(9.0, updated.getLat());
        assertEquals(8.0, updated.getLon());
    }

    @Test
    @DisplayName("delete: 404 si no existe")
    void delete_404() {
        assertThrows(EntityNotFoundException.class, () -> arbolService.delete(999L));
    }
}
