package sv.edu.udb.web.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.web.dto.request.ArbolRequest;
import sv.edu.udb.web.dto.response.ArbolResponse;

import static org.junit.jupiter.api.Assertions.*;

class ArbolMapperTest {
    private final ArbolMapper mapper = Mappers.getMapper(ArbolMapper.class);

    @Test
    void toEntity_ok_mapeaCamposIncluyendoRefs() {
        // Arrange
        ArbolRequest req = new ArbolRequest();
        req.setEspecieId(11L);
        req.setParqueId(22L);
        req.setLat(13.70);
        req.setLon(-89.20);
        req.setDbhCm(35.5);
        req.setH(12.3);

        // Act
        Arbol e = mapper.toEntity(req);

        // Assert
        assertNull(e.getId()); // id ignorado
        assertNotNull(e.getEspecie());
        assertEquals(11L, e.getEspecie().getId());
        assertNotNull(e.getParque());
        assertEquals(22L, e.getParque().getId());
        assertEquals(13.70, e.getLat());
        assertEquals(-89.20, e.getLon());
        assertEquals(35.5, e.getDbhCm());
        assertEquals(12.3, e.getH());
    }

    @Test
    void toResponse_ok_mapeaCamposBasicos() {
        // Arrange
        Arbol a = new Arbol();
        a.setId(5L);
        Especie esp = new Especie(); esp.setId(11L);
        Parque pq = new Parque(); pq.setId(22L);
        a.setEspecie(esp);
        a.setParque(pq);
        a.setLat(1.1);
        a.setLon(2.2);
        a.setDbhCm(10.0);
        a.setH(3.3);

        // Act
        ArbolResponse r = mapper.toResponse(a);

        // Assert
        assertEquals(5L, r.getId());
        assertEquals(11L, r.getEspecieId());
        assertEquals(22L, r.getParqueId());
        assertEquals(1.1, r.getLat());
        assertEquals(2.2, r.getLon());
        assertEquals(10.0, r.getDbhCm());
        assertEquals(3.3, r.getH());
    }
}
