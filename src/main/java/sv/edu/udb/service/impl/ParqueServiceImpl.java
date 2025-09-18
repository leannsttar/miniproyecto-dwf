package sv.edu.udb.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.ParqueService;

import java.util.List;

@Service @Transactional
public class ParqueServiceImpl implements ParqueService {
    private final ParqueRepository repo;
    public ParqueServiceImpl(ParqueRepository repo) { this.repo = repo; }

    @Override @Transactional(readOnly = true)
    public List<Parque> findAll() { return repo.findAll(); }

    @Override @Transactional(readOnly = true)
    public Parque findById(Long id) { return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Parque no encontrado: " + id)); }

    @Override public Parque create(Parque p) { if (p.getId()!=null) p.setId(null); return repo.save(p); }
    @Override public Parque update(Long id, Parque p) {
        Parque current = findById(id);
        current.setNombre(p.getNombre());
        current.setDistrito(p.getDistrito());
        current.setAreaHa(p.getAreaHa());
        current.setLat(p.getLat());
        current.setLon(p.getLon());
        return repo.save(current);
    }
    @Override public void delete(Long id) { if (!repo.existsById(id)) throw new EntityNotFoundException("Parque no encontrado: " + id); repo.deleteById(id); }
}
