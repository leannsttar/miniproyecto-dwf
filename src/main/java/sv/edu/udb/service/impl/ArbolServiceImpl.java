package sv.edu.udb.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.ArbolService;

import java.util.List;

@Service @Transactional
public class ArbolServiceImpl implements ArbolService {
    private final ArbolRepository repo;
    private final ParqueRepository parqueRepo;
    private final EspecieRepository especieRepo;

    public ArbolServiceImpl(ArbolRepository repo, ParqueRepository parqueRepo, EspecieRepository especieRepo) {
        this.repo = repo; this.parqueRepo = parqueRepo; this.especieRepo = especieRepo;
    }

    @Override @Transactional(readOnly = true)
    public List<Arbol> findAll() { return repo.findAll(); }

    @Override @Transactional(readOnly = true)
    public Arbol findById(Long id) { return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Arbol no encontrado: " + id)); }

    @Override
    public Arbol create(Arbol a) {
        if (a.getParque()==null || a.getParque().getId()==null) throw new IllegalArgumentException("parque_id requerido");
        if (a.getEspecie()==null || a.getEspecie().getId()==null) throw new IllegalArgumentException("especie_id requerido");
        Parque p = parqueRepo.findById(a.getParque().getId()).orElseThrow(() -> new EntityNotFoundException("Parque no encontrado: " + a.getParque().getId()));
        Especie e = especieRepo.findById(a.getEspecie().getId()).orElseThrow(() -> new EntityNotFoundException("Especie no encontrada: " + a.getEspecie().getId()));
        a.setParque(p); a.setEspecie(e);
        return repo.save(a);
    }

    @Override
    public Arbol update(Long id, Arbol a) {
        Arbol current = findById(id);
        if (a.getParque()!=null && a.getParque().getId()!=null) {
            current.setParque(parqueRepo.findById(a.getParque().getId()).orElseThrow(() -> new EntityNotFoundException("Parque no encontrado: " + a.getParque().getId())));
        }
        if (a.getEspecie()!=null && a.getEspecie().getId()!=null) {
            current.setEspecie(especieRepo.findById(a.getEspecie().getId()).orElseThrow(() -> new EntityNotFoundException("Especie no encontrada: " + a.getEspecie().getId())));
        }
        current.setLat(a.getLat());
        current.setLon(a.getLon());
        return repo.save(current);
    }

    @Override public void delete(Long id) { if (!repo.existsById(id)) throw new EntityNotFoundException("Arbol no encontrado: " + id); repo.deleteById(id); }
}
