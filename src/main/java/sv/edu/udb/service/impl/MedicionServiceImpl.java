package sv.edu.udb.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.domain.Medicion;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.MedicionRepository;
import sv.edu.udb.service.MedicionService;

import java.util.List;

@Service @Transactional
public class MedicionServiceImpl implements MedicionService {
    private final MedicionRepository repo;
    private final ArbolRepository arbolRepo;

    public MedicionServiceImpl(MedicionRepository repo, ArbolRepository arbolRepo) {
        this.repo = repo; this.arbolRepo = arbolRepo;
    }

    @Override @Transactional(readOnly = true)
    public List<Medicion> findAll() { return repo.findAll(); }

    @Override @Transactional(readOnly = true)
    public Medicion findById(Long id) { return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Medicion no encontrada: " + id)); }

    @Override
    public Medicion create(Medicion m) {
        if (m.getArbol()==null || m.getArbol().getId()==null) throw new IllegalArgumentException("arbol_id requerido");
        Arbol a = arbolRepo.findById(m.getArbol().getId()).orElseThrow(() -> new EntityNotFoundException("Arbol no encontrado: " + m.getArbol().getId()));
        m.setArbol(a);
        return repo.save(m);
    }

    @Override
    public Medicion update(Long id, Medicion m) {
        Medicion current = findById(id);
        if (m.getArbol()!=null && m.getArbol().getId()!=null) {
            current.setArbol(arbolRepo.findById(m.getArbol().getId()).orElseThrow(() -> new EntityNotFoundException("Arbol no encontrado: " + m.getArbol().getId())));
        }
        current.setFecha(m.getFecha());
        current.setDbhCm(m.getDbhCm());
        current.setAlturaM(m.getAlturaM());
        current.setObservaciones(m.getObservaciones());
        return repo.save(current);
    }

    @Override public void delete(Long id) { if (!repo.existsById(id)) throw new EntityNotFoundException("Medicion no encontrada: " + id); repo.deleteById(id); }
}
