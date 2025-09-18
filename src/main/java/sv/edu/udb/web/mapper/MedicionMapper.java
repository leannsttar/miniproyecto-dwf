package sv.edu.udb.web.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.domain.Medicion;
import sv.edu.udb.web.dto.request.MedicionRequest;
import sv.edu.udb.web.dto.response.MedicionResponse;

@Mapper(componentModel = "spring")
public interface MedicionMapper {

    /* Request -> Entity */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "arbol", source = "arbolId", qualifiedByName = "idToArbol")
    })
    Medicion toEntity(MedicionRequest request);

    /* Update */
    @Mappings({
            @Mapping(target = "arbol", source = "arbolId", qualifiedByName = "idToArbolIfNotNull")
    })
    void update(@MappingTarget Medicion target, MedicionRequest request);

    /* Entity -> Response */
    @Mappings({
            @Mapping(target = "arbolId", source = "arbol.id")
    })
    MedicionResponse toResponse(Medicion entity);

    /* Helpers */
    @Named("idToArbol")
    default Arbol idToArbol(Long id) {
        if (id == null) return null;
        Arbol a = new Arbol(); a.setId(id); return a;
    }
    @Named("idToArbolIfNotNull")
    default Arbol idToArbolIfNotNull(Long id) { return idToArbol(id); }
}
