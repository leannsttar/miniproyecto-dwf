package sv.edu.udb.service.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.controller.response.ResultadoParqueResponse;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.domain.ResultadoParque;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoParqueMapperTest {

    private final ResultadoParqueMapper mapper = Mappers.getMapper(ResultadoParqueMapper.class);

    @Test
    void toResponse_ok_mapeaCamposBasicos() {
        Parque p = new Parque();
        p.setId(7L);

        ResultadoParque entity = new ResultadoParque();
        entity.setId(100L);
        entity.setParque(p);
        entity.setAnio(2025);
        entity.setStockCarbonoT(0.3);
        entity.setCapturaAnualT(5.6);

        ResultadoParqueResponse r = mapper.toResponse(entity);

        assertEquals(7L, r.getParqueId());
        assertEquals(2025, r.getAnio());
        assertEquals(0.3, r.getStockCarbonoT());
        assertEquals(5.6, r.getCapturaAnualT());
    }

    @Test
    void toResponseList_ok() {
        Parque p = new Parque(); p.setId(9L);

        ResultadoParque e1 = new ResultadoParque();
        e1.setParque(p); e1.setAnio(2024); e1.setStockCarbonoT(1.1); e1.setCapturaAnualT(2.2);

        ResultadoParque e2 = new ResultadoParque();
        e2.setParque(p); e2.setAnio(2025); e2.setStockCarbonoT(3.3); e2.setCapturaAnualT(4.4);

        List<ResultadoParqueResponse> list = mapper.toResponseList(List.of(e1, e2));
        assertEquals(2, list.size());

        assertEquals(9L, list.get(0).getParqueId());
        assertEquals(2024, list.get(0).getAnio());
        assertEquals(1.1, list.get(0).getStockCarbonoT());
        assertEquals(2.2, list.get(0).getCapturaAnualT());

        assertEquals(9L, list.get(1).getParqueId());
        assertEquals(2025, list.get(1).getAnio());
        assertEquals(3.3, list.get(1).getStockCarbonoT());
        assertEquals(4.4, list.get(1).getCapturaAnualT());
    }
}
