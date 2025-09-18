package sv.edu.udb.service;

import sv.edu.udb.domain.Especie;
import java.util.List;

public interface EspecieService {
    List<Especie> findAll();
    Especie findById(Long id);
    Especie create(Especie e);
    Especie update(Long id, Especie e);
    void delete(Long id);
}
