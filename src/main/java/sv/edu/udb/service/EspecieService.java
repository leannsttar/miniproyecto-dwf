package sv.edu.udb.service;

import sv.edu.udb.controller.request.EspecieRequest;
import sv.edu.udb.controller.response.EspecieResponse;

import java.util.List;

public interface EspecieService {
    List<EspecieResponse> findAll();
    EspecieResponse findById(Long id);
    EspecieResponse save(EspecieRequest e);
    EspecieResponse update(Long id, EspecieRequest e);
    void delete(Long id);
}
