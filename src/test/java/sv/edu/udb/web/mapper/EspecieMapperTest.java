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


        // Act
        Especie e = mapper.toEntity(req);
        // Assert
        assertNull(e.getId());
        assertEquals("Swietenia macrophylla", e.getNombreCientifico());
        assertEquals("Caoba", e.getNombreComun());

    }

    @Test
    void update_ok_actualizaCamposDesdeRequest() {
        // Arrange
        Especie target = new Especie();
        target.setId(7L);
        target.setNombreCientifico("Old");
        target.setNombreComun("OldC");


        EspecieRequest req = new EspecieRequest();
        req.setNombreCientifico("Nuevo");
        req.setNombreComun("NuevoC");

        // Act
        mapper.update(target, req);

        // Assert
        assertEquals(7L, target.getId()); // no debería cambiar
        assertEquals("Nuevo", target.getNombreCientifico());
        assertEquals("NuevoC", target.getNombreComun());

    }

    @Test
    void toResponse_ok_mapeaCampos() {
        // Arrange
        Especie e = new Especie();
        e.setId(9L);
        e.setNombreCientifico("Swietenia macrophylla");
        e.setNombreComun("Caoba");


        // Act
        EspecieResponse r = mapper.toResponse(e);

        // Assert
        assertEquals(9L, r.getId());
        assertEquals("Swietenia macrophylla", r.getNombreCientifico());
        assertEquals("Caoba", r.getNombreComun());

    }
}
