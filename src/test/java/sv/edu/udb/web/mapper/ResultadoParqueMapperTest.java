package sv.edu.udb.web.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.service.dto.ResultadoParqueResumen;
import sv.edu.udb.web.dto.response.ResultadoParqueResponse;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoParqueMapperTest {
    private final ResultadoParqueMapper mapper = Mappers.getMapper(ResultadoParqueMapper.class);

    @Test
    void toResponse_ok_mapeaCampos() {
        // Arrange
        ResultadoParqueResumen dto = new ResultadoParqueResumen(
                22L, // parqueId
                2024, // anio
                123.45, // stockCarbonoT
                67.89   // capturaAnualT
        );

        // Act
        ResultadoParqueResponse r = mapper.toResponse(dto);

        // Assert
        assertEquals(22L, r.getParqueId());
        assertEquals(2024, r.getAnio());
        assertEquals(123.45, r.getStockCarbonoT());
        assertEquals(67.89, r.getCapturaAnualT());
    }
}
