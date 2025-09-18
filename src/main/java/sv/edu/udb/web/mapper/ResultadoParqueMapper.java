package sv.edu.udb.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.service.dto.ResultadoParqueResumen;
import sv.edu.udb.web.dto.response.ResultadoParqueResponse;

@Mapper(componentModel = "spring")
public interface ResultadoParqueMapper {
    ResultadoParqueMapper INSTANCE = Mappers.getMapper(ResultadoParqueMapper.class);

    ResultadoParqueResponse toResponse(ResultadoParqueResumen dto);
}
