package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sv.edu.udb.controller.response.EstimacionResponse;
import sv.edu.udb.repository.domain.Estimacion;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EstimacionMapper {

    @Mapping(target = "medicionId", source = "medicion.id")
    EstimacionResponse toResponse(Estimacion entity);

    List<EstimacionResponse> toResponseList(List<Estimacion> list);
}
