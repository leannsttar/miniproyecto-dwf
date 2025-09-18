package sv.edu.udb.service;

import sv.edu.udb.domain.Arbol;
import java.util.List;

public interface ArbolService {
    List<Arbol> findAll();
    Arbol findById(Long id);
    Arbol create(Arbol a);
    Arbol update(Long id, Arbol a);
    void delete(Long id);
}
