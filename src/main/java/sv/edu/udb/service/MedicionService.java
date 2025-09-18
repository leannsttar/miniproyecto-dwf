package sv.edu.udb.service;

import sv.edu.udb.domain.Medicion;
import java.util.List;

public interface MedicionService {
    List<Medicion> findAll();
    Medicion findById(Long id);
    Medicion create(Medicion m);
    Medicion update(Long id, Medicion m);
    void delete(Long id);
}
