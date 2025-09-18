package sv.edu.udb.web.mapper;

import org.mapstruct.*;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.web.dto.request.ArbolRequest;
import sv.edu.udb.web.dto.response.ArbolResponse;

@Mapper(componentModel = "spring")
public interface ArbolMapper {

    /* Request -> Entity */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parque",  expression = "java(parqueFromId(request.getParqueId()))")
    @Mapping(target = "especie", expression = "java(especieFromId(request.getEspecieId()))")
    // creadoEn lo deja por defecto (Instant.now()) -> no lo toques
    Arbol toEntity(ArbolRequest request);

    /* Update (PUT/PATCH): ignora nulls */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    default void update(@MappingTarget Arbol entity, ArbolRequest request) {
        if (request.getParqueId() != null)  entity.setParque(parqueFromId(request.getParqueId()));
        if (request.getEspecieId() != null) entity.setEspecie(especieFromId(request.getEspecieId()));
        if (request.getLat() != null)       entity.setLat(request.getLat());
        if (request.getLon() != null)       entity.setLon(request.getLon());
    }

    /* Entity -> Response */
    @Mapping(target = "parqueId",  source = "parque.id")
    @Mapping(target = "especieId", source = "especie.id")
    ArbolResponse toResponse(Arbol entity);

    /* Helpers (no consultan BD; solo setean id) */
    default Parque parqueFromId(Long id) { if (id == null) return null; Parque p = new Parque(); p.setId(id); return p; }
    default Especie especieFromId(Long id) { if (id == null) return null; Especie e = new Especie(); e.setId(id); return e; }
}
