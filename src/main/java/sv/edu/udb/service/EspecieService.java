package sv.edu.udb.service;

import sv.edu.udb.domain.Especie;
import sv.edu.udb.web.dto.request.EspecieRequest;
import sv.edu.udb.web.dto.response.EspecieResponse;

import java.util.List;

public interface EspecieService {
    List<EspecieResponse> findAll();
    EspecieResponse findById(Long id);
    EspecieResponse save(EspecieRequest e);
    EspecieResponse update(Long id, EspecieRequest e);
    void delete(Long id);
}
