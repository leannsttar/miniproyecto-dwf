package sv.edu.udb.web.mapper;

import org.mapstruct.*;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.web.dto.request.ParqueRequest;
import sv.edu.udb.web.dto.response.ParqueResponse;

@Mapper(componentModel = "spring")
public interface ParqueMapper {

    /* Request -> Entity */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            // lo ignoramos al crear para que use el default Instant.now()
            @Mapping(target = "creadoEn", ignore = true)
    })
    Parque toEntity(ParqueRequest request);

    /* Update */
    void update(@MappingTarget Parque target, ParqueRequest request);

    /* Entity -> Response */
    ParqueResponse toResponse(Parque entity);
}
