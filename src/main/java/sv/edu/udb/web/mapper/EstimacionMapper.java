package sv.edu.udb.web.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.domain.Estimacion;
import sv.edu.udb.domain.Medicion;
import sv.edu.udb.web.dto.request.EstimacionManualRequest;
import sv.edu.udb.web.dto.response.EstimacionResponse;

@Mapper(componentModel = "spring")
public interface EstimacionMapper {

    /* Request -> Entity (creación manual) */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "medicion", source = "medicionId", qualifiedByName = "idToMedicion")
    })
    Estimacion toEntity(EstimacionManualRequest request);

    /* Entity -> Response */
    @Mappings({
            @Mapping(target = "medicionId", source = "medicion.id")
    })
    EstimacionResponse toResponse(Estimacion entity);

    /* Helpers */
    @Named("idToMedicion")
    default Medicion idToMedicion(Long id) {
        if (id == null) return null;
        Medicion m = new Medicion(); m.setId(id); return m;
    }
}
