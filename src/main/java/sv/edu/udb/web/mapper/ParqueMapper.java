package sv.edu.udb.web.mapper;

import org.mapstruct.*;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.web.dto.request.ParqueRequest;
import sv.edu.udb.web.dto.response.ParqueResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParqueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    Parque toEntity(ParqueRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    void update(@MappingTarget Parque target, ParqueRequest request);

    List<ParqueResponse> toParqueResponseList(final List<Parque> parqueList);

    ParqueResponse toResponse(Parque entity);
}

