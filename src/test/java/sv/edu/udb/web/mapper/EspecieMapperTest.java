package sv.edu.udb.web.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.web.dto.request.EspecieRequest;
import sv.edu.udb.web.dto.response.EspecieResponse;

import static org.junit.jupiter.api.Assertions.*;

class EspecieMapperTest {
    private final EspecieMapper mapper = Mappers.getMapper(EspecieMapper.class);

    @Test
    void toEntity_ok_mapeaCampos() {
        // Arrange
        EspecieRequest req = new EspecieRequest();
        req.setNombreCientifico("Swietenia macrophylla");
        req.setNombreComun("Caoba");
        req.setDensidadMadera(0.59);
        req.setAreaHa(1.2);
        req.setLat(13.70);
        req.setLon(-89.20);

        // Act
        Especie e = mapper.toEntity(req);

        // Assert
        assertNull(e.getId());
        assertEquals("Swietenia macrophylla", e.getNombreCientifico());
        assertEquals("Caoba", e.getNombreComun());
        assertEquals(0.59, e.getDensidadMadera());
        assertEquals(1.2, e.getAreaHa());
        assertEquals(13.70, e.getLat());
        assertEquals(-89.20, e.getLon());
    }

    @Test
    void update_ok_actualizaCamposDesdeRequest() {
        // Arrange
        Especie target = new Especie();
        target.setId(7L);
        target.setNombreCientifico("Old");
        target.setNombreComun("OldC");
        target.setDensidadMadera(0.3);
        target.setAreaHa(0.5);
        target.setLat(1.0);
        target.setLon(2.0);

        EspecieRequest req = new EspecieRequest();
        req.setNombreCientifico("Nuevo");
        req.setNombreComun("NuevoC");
        req.setDensidadMadera(0.8);
        req.setAreaHa(2.5);
        req.setLat(13.70);
        req.setLon(-89.20);

        // Act
        mapper.update(target, req);

        // Assert
        assertEquals(7L, target.getId()); // no debería cambiar
        assertEquals("Nuevo", target.getNombreCientifico());
        assertEquals("NuevoC", target.getNombreComun());
        assertEquals(0.8, target.getDensidadMadera());
        assertEquals(2.5, target.getAreaHa());
        assertEquals(13.70, target.getLat());
        assertEquals(-89.20, target.getLon());
    }

    @Test
    void toResponse_ok_mapeaCampos() {
        // Arrange
        Especie e = new Especie();
        e.setId(9L);
        e.setNombreCientifico("Swietenia macrophylla");
        e.setNombreComun("Caoba");
        e.setDensidadMadera(0.59);
        e.setAreaHa(1.2);
        e.setLat(13.70);
        e.setLon(-89.20);

        // Act
        EspecieResponse r = mapper.toResponse(e);

        // Assert
        assertEquals(9L, r.getId());
        assertEquals("Swietenia macrophylla", r.getNombreCientifico());
        assertEquals("Caoba", r.getNombreComun());
        assertEquals(0.59, r.getDensidadMadera());
        assertEquals(1.2, r.getAreaHa());
        assertEquals(13.70, r.getLat());
        assertEquals(-89.20, r.getLon());
    }
}
