package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sv.edu.udb.controller.response.ResultadoParqueResponse;
import sv.edu.udb.repository.domain.ResultadoParque;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResultadoParqueMapper {

    @Mapping(target = "parqueId", source = "parque.id")
    @Mapping(target = "anio", source = "anio")
    @Mapping(target = "stockCarbonoT", source = "stockCarbonoT")
    @Mapping(target = "capturaAnualT", source = "capturaAnualT")
    ResultadoParqueResponse toResponse(ResultadoParque entity);

    List<ResultadoParqueResponse> toResponseList(List<ResultadoParque> entities);
}
