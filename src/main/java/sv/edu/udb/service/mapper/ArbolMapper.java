package sv.edu.udb.service.mapper;

import org.mapstruct.*;
import sv.edu.udb.repository.domain.Arbol;
import sv.edu.udb.controller.request.ArbolRequest;
import sv.edu.udb.controller.response.ArbolResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArbolMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parque.id", source = "parqueId")
    @Mapping(target = "especie.id", source = "especieId")
    @Mapping(target = "creadoEn", ignore = true)
    Arbol toEntity(ArbolRequest request);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parque.id", source = "parqueId")
    @Mapping(target = "especie.id", source = "especieId")
    @Mapping(target = "creadoEn", ignore = true)
    void update(@MappingTarget Arbol target, ArbolRequest request);

    List<ArbolResponse> toArbolResponseList(List<Arbol> arbolList);

    @Mapping(target = "parqueId", source = "parque.id")
    @Mapping(target = "especieId", source = "especie.id")
    ArbolResponse toResponse(Arbol entity);
}
