package sv.edu.udb.service.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.controller.response.EstimacionResponse;
import sv.edu.udb.repository.domain.Estimacion;
import sv.edu.udb.repository.domain.Medicion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstimacionMapperTest {

    private final EstimacionMapper mapper = Mappers.getMapper(EstimacionMapper.class);

    @Test
    void toResponse_ok_mapeaCamposBasicos() {
        Medicion m = new Medicion();
        m.setId(10L);

        Estimacion e = new Estimacion();
        e.setId(5L);
        e.setMedicion(m);
        e.setBiomasaKg(100.0);
        e.setCarbonoKg(47.0);
        e.setCo2eKg(172.33);
        e.setFraccionCarbono(0.47);
        e.setIncertidumbrePorc(2.5);
        e.setNotas("nota");

        EstimacionResponse r = mapper.toResponse(e);

        assertEquals(5L, r.getId());
        assertEquals(10L, r.getMedicionId());
        assertEquals(100.0, r.getBiomasaKg());
        assertEquals(47.0, r.getCarbonoKg());
        assertEquals(172.33, r.getCo2eKg());
        assertEquals(0.47, r.getFraccionCarbono());
        assertEquals(2.5, r.getIncertidumbrePorc());
        assertEquals("nota", r.getNotas());
    }

    @Test
    void toResponseList_ok() {
        Medicion m = new Medicion(); m.setId(7L);
        Estimacion e1 = new Estimacion(); e1.setId(1L); e1.setMedicion(m);
        Estimacion e2 = new Estimacion(); e2.setId(2L); e2.setMedicion(m);

        List<EstimacionResponse> list = mapper.toResponseList(List.of(e1, e2));
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(2L, list.get(1).getId());
        assertEquals(7L, list.get(0).getMedicionId());
        assertEquals(7L, list.get(1).getMedicionId());
    }
}
