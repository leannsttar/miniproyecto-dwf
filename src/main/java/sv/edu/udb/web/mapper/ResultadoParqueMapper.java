package sv.edu.udb.web.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.domain.ResultadoParque;
import sv.edu.udb.service.dto.ResultadoParqueResumen;
import sv.edu.udb.web.dto.response.ResultadoParqueResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResultadoParqueMapper {

    // Ya lo tenías:
    ResultadoParqueResponse toResponse(ResultadoParqueResumen dto);

    // Nuevo: mapear directamente una entidad persistida a response
    ResultadoParqueResponse toResponse(ResultadoParque entity);

    // MapStruct genera estos automáticamente, pero los declaramos por claridad
    List<ResultadoParqueResponse> toResponse(List<ResultadoParque> entities);
    List<ResultadoParqueResponse> toResponseFromResumen(List<ResultadoParqueResumen> dtos);
}