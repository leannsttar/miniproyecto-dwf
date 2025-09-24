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
    void toEspecieResponseList_ok_mapeaLista() {
        Especie e1 = new Especie(); e1.setId(1L); e1.setNombreCientifico("A"); e1.setNombreComun("CA");
        Especie e2 = new Especie(); e2.setId(2L); e2.setNombreCientifico("B"); e2.setNombreComun("CB");

        var out = mapper.toEspecieResponseList(java.util.List.of(e1, e2));

        assertNotNull(out);
        assertEquals(2, out.size());
        assertEquals(1L, out.get(0).getId());
        assertEquals("A", out.get(0).getNombreCientifico());
        assertEquals(2L, out.get(1).getId());
        assertEquals("B", out.get(1).getNombreCientifico());
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

    @Test
    void update_put_nullDebePisarValor() {
        // Arrange
        Especie target = new Especie();
        target.setId(7L);
        target.setNombreCientifico("Viejo");
        target.setNombreComun("ViejoC");

        EspecieRequest req = new EspecieRequest();
        req.setNombreCientifico(null); // ← debe volverse null en target
        req.setNombreComun("NuevoC");

        // Act
        mapper.update(target, req);

        // Assert
        assertEquals(7L, target.getId());
        assertNull(target.getNombreCientifico(), "PUT: null del request debe pisar el valor");
        assertEquals("NuevoC", target.getNombreComun());
    }

}
