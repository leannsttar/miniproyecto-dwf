package sv.edu.udb.service;

import sv.edu.udb.web.dto.request.ParqueRequest;
import sv.edu.udb.web.dto.response.ParqueResponse;

import java.util.List;

public interface ParqueService {
    List<ParqueResponse> findAll();
    ParqueResponse findById(Long id);
    ParqueResponse save(ParqueRequest request);
    ParqueResponse update(Long id, ParqueRequest request);
    void delete(Long id);
}
