package sv.edu.udb.service.mapper;

import org.mapstruct.*;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.controller.request.ParqueRequest;
import sv.edu.udb.controller.response.ParqueResponse;

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

