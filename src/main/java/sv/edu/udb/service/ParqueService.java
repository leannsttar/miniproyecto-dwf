package sv.edu.udb.service;

import sv.edu.udb.domain.Parque;
import java.util.List;

public interface ParqueService {
    List<Parque> findAll();
    Parque findById(Long id);
    Parque create(Parque p);
    Parque update(Long id, Parque p);
    void delete(Long id);
}
