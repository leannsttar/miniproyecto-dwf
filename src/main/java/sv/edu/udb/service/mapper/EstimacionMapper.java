package sv.edu.udb.service.mapper;

import org.mapstruct.*;
import sv.edu.udb.repository.domain.Estimacion;
import sv.edu.udb.repository.domain.Medicion;
import sv.edu.udb.controller.request.EstimacionManualRequest;
import sv.edu.udb.controller.response.EstimacionResponse;

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
