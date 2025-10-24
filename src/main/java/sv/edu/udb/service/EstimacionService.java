package sv.edu.udb.service;

import sv.edu.udb.controller.request.EstimacionDesdeMedicionRequest;
import sv.edu.udb.controller.response.EstimacionResponse;

import java.util.List;

public interface EstimacionService {
    List<EstimacionResponse> findAll();
    EstimacionResponse findById(Long id);
    EstimacionResponse createDesdeMedicion(EstimacionDesdeMedicionRequest req);
    void delete(Long id);
}
