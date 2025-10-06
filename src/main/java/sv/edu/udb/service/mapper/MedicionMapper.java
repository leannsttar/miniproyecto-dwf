package sv.edu.udb.service.mapper;

import org.mapstruct.*;
import sv.edu.udb.repository.domain.Medicion;
import sv.edu.udb.controller.request.MedicionRequest;
import sv.edu.udb.controller.response.MedicionResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicionMapper {

    // Request -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "arbol.id", source = "arbolId")
    Medicion toEntity(MedicionRequest request);

    // Update existente
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "arbol.id", source = "arbolId")
    void update(@MappingTarget Medicion target, MedicionRequest request);

    // Entity -> Response
    @Mapping(target = "arbolId", source = "arbol.id")
    MedicionResponse toResponse(Medicion entity);

    List<MedicionResponse> toResponseList(List<Medicion> entities);
}
