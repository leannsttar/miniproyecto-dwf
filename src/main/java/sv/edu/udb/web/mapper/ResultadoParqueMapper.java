package sv.edu.udb.web.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.service.dto.ResultadoParqueResumen;
import sv.edu.udb.web.dto.response.ResultadoParqueResponse;

@Mapper(componentModel = "spring")
public interface ResultadoParqueMapper {

    ResultadoParqueResponse toResponse(ResultadoParqueResumen dto);
}
