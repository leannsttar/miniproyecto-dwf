package sv.edu.udb.web.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.web.dto.request.EspecieRequest;
import sv.edu.udb.web.dto.response.EspecieResponse;

@Mapper(componentModel = "spring")
public interface EspecieMapper {

    /* Request -> Entity */
    @Mapping(target = "id", ignore = true)
    Especie toEntity(EspecieRequest request);

    /* Update */
    void update(@MappingTarget Especie target, EspecieRequest request);

    /* Entity -> Response */
    EspecieResponse toResponse(Especie entity);
}
