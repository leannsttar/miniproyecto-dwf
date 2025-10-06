package sv.edu.udb.service.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.repository.domain.Arbol;
import sv.edu.udb.repository.domain.Especie;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.service.mapper.ArbolMapper;
import sv.edu.udb.controller.request.ArbolRequest;
import sv.edu.udb.controller.response.ArbolResponse;

import static org.junit.jupiter.api.Assertions.*;

class ArbolMapperTest {

    private final ArbolMapper mapper = Mappers.getMapper(ArbolMapper.class);

    @Test
    void toEntity_ok_mapeaCamposIncluyendoRefs() {
        ArbolRequest req = new ArbolRequest();
        req.setEspecieId(11L);
        req.setParqueId(22L);
        req.setLat(13.70);
        req.setLon(-89.20);

        Arbol e = mapper.toEntity(req);

        assertNull(e.getId());
        assertNotNull(e.getEspecie());
        assertEquals(11L, e.getEspecie().getId());
        assertNotNull(e.getParque());
        assertEquals(22L, e.getParque().getId());
        assertEquals(13.70, e.getLat());
        assertEquals(-89.20, e.getLon());
    }

    @Test
    void update_ok_actualizaRelacionesYCoords() {
        // target existente
        Arbol target = new Arbol();
        Especie esp = new Especie(); esp.setId(10L);
        Parque pq = new Parque(); pq.setId(20L);
        target.setEspecie(esp);
        target.setParque(pq);
        target.setLat(1.0);
        target.setLon(2.0);

        // request con NUEVOS ids (no null) y coords nuevas
        ArbolRequest req = new ArbolRequest();
        req.setParqueId(30L);
        req.setEspecieId(40L);
        req.setLat(9.0);
        req.setLon(8.0);

        // act
        mapper.update(target, req);

        // assert: se actualizan relaciones y coords
        assertEquals(30L, target.getParque().getId());
        assertEquals(40L, target.getEspecie().getId());
        assertEquals(9.0, target.getLat());
        assertEquals(8.0, target.getLon());
    }


    @Test
    void toResponse_ok_mapeaCamposBasicos() {
        Arbol a = new Arbol();
        a.setId(5L);
        Especie esp = new Especie(); esp.setId(11L);
        Parque pq = new Parque(); pq.setId(22L);
        a.setEspecie(esp);
        a.setParque(pq);
        a.setLat(1.1);
        a.setLon(2.2);

        ArbolResponse r = mapper.toResponse(a);

        assertEquals(5L, r.getId());
        assertEquals(11L, r.getEspecieId());
        assertEquals(22L, r.getParqueId());
        assertEquals(1.1, r.getLat());
        assertEquals(2.2, r.getLon());
    }
}
