package sv.edu.udb.web.mapper;

import org.mapstruct.*;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.web.dto.request.EspecieRequest;
import sv.edu.udb.web.dto.response.EspecieResponse;


import java.util.List;

@Mapper(componentModel = "spring")
public interface EspecieMapper {

    @Mapping(target = "id", ignore = true)
    Especie toEntity(EspecieRequest request);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Especie target, EspecieRequest request);

    List<EspecieResponse> toEspecieResponseList(final List<Especie> especieList);

    EspecieResponse toResponse(Especie entity);
}
