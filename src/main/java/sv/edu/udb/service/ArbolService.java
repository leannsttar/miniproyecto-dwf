package sv.edu.udb.service;

import sv.edu.udb.controller.request.ArbolRequest;
import sv.edu.udb.controller.response.ArbolResponse;

import java.util.List;

public interface ArbolService {
    List<ArbolResponse> findAll();
    ArbolResponse findById(Long id);
    ArbolResponse save(ArbolRequest a);
    ArbolResponse update(Long id, ArbolRequest a);
    void delete(Long id);
}
