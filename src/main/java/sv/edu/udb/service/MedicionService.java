package sv.edu.udb.service;

import sv.edu.udb.controller.request.MedicionRequest;
import sv.edu.udb.controller.response.MedicionResponse;

import java.util.List;

public interface MedicionService {
    List<MedicionResponse> findAll();
    MedicionResponse findById(Long id);
    MedicionResponse save(MedicionRequest request);
    MedicionResponse update(Long id, MedicionRequest request);
    void delete(Long id);
}
