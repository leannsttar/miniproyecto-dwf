package sv.edu.udb.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.service.mapper.ParqueMapper;
import sv.edu.udb.controller.request.ParqueRequest;
import sv.edu.udb.controller.response.ParqueResponse;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParqueMapperTest {

    private ParqueMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ParqueMapper.class);
        assertNotNull(mapper, "Mapper no debe ser null");
    }

    @Test
    void toEntity_ignoraIdYCreadoEn_yMapeaCampos() {
        // Arrange
        ParqueRequest req = new ParqueRequest();
        req.setNombre("Central");
        req.setDistrito("D1");
        req.setAreaHa(2.5);
        req.setLat(10.1);
        req.setLon(20.2);

        // Act
        Parque out = mapper.toEntity(req);

        // Assert
        assertNotNull(out);
        assertNull(out.getId(), "id debe quedar null (ignore=true)");
        assertEquals("Central", out.getNombre());
        assertEquals("D1", out.getDistrito());
        assertEquals(2.5, out.getAreaHa());
        assertEquals(10.1, out.getLat());
        assertEquals(20.2, out.getLon());
        // creadoEn lo ignora el mapper; en tu entidad se inicializa con Instant.now()
        assertNotNull(out.getCreadoEn(), "creadoEn debería tener un valor por defecto de la entidad");
    }

    @Test
    void update_putReemplazoTotal_nullPisaTarget() {
        // Arrange (target con valores iniciales)
        Parque target = new Parque();
        target.setId(99L);
        target.setNombre("Viejo");
        target.setDistrito("D0");
        target.setAreaHa(1.0);
        target.setLat(5.0);
        target.setLon(6.0);
        Instant creado = target.getCreadoEn(); // debe preservarse, el mapper no lo toca

        // Request con algunos nulls (PUT estricto: SET_TO_NULL → deben volverse null en target)
        ParqueRequest req = new ParqueRequest();
        req.setNombre("Nuevo");
        req.setDistrito(null);      // → debe quedar null
        req.setAreaHa(7.7);
        req.setLat(null);           // → debe quedar null
        req.setLon(8.8);

        // Act
        mapper.update(target, req);

        // Assert
        assertEquals(99L, target.getId(), "id no lo toca el mapper update");
        assertEquals("Nuevo", target.getNombre());
        assertNull(target.getDistrito(), "PUT: null del request debe pisar a null");
        assertEquals(7.7, target.getAreaHa());
        assertNull(target.getLat(), "PUT: null del request debe pisar a null");
        assertEquals(8.8, target.getLon());
        assertEquals(creado, target.getCreadoEn(), "creadoEn no debe ser modificado por el mapper");
    }

    @Test
    void toResponse_mapeaCamposBasicos() {
        // Arrange
        Parque entity = new Parque();
        entity.setId(1L);
        entity.setNombre("Parque X");
        entity.setDistrito("D9");
        entity.setAreaHa(9.9);
        entity.setLat(1.1);
        entity.setLon(2.2);

        // Act
        ParqueResponse out = mapper.toResponse(entity);

        // Assert
        assertNotNull(out);
        assertEquals(1L, out.getId());
        assertEquals("Parque X", out.getNombre());
        assertEquals("D9", out.getDistrito());
        assertEquals(9.9, out.getAreaHa());
        assertEquals(1.1, out.getLat());
        assertEquals(2.2, out.getLon());
    }

    @Test
    void toParqueResponseList_mapeaLista() {
        // Arrange
        Parque p1 = new Parque(); p1.setId(1L); p1.setNombre("A"); p1.setDistrito("D1"); p1.setAreaHa(1.0);
        Parque p2 = new Parque(); p2.setId(2L); p2.setNombre("B"); p2.setDistrito("D2"); p2.setAreaHa(2.0);

        // Act
        List<ParqueResponse> out = mapper.toParqueResponseList(List.of(p1, p2));

        // Assert
        assertNotNull(out);
        assertEquals(2, out.size());
        assertEquals(1L, out.get(0).getId());
        assertEquals("A", out.get(0).getNombre());
        assertEquals(2L, out.get(1).getId());
        assertEquals("B", out.get(1).getNombre());
    }
}
