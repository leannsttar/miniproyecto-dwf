package sv.edu.udb.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.service.EspecieService;

import java.util.List;

@Service
@Transactional
public class EspecieServiceImpl implements EspecieService {

    private final EspecieRepository repo;

    public EspecieServiceImpl(EspecieRepository repo) {
        this.repo = repo;
    }

    @Override @Transactional(readOnly = true)
    public List<Especie> findAll() {
        return repo.findAll();
    }

    @Override @Transactional(readOnly = true)
    public Especie findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Especie no encontrada: " + id));
    }

    @Override
    public Especie create(Especie e) {
        if (e.getId()!=null) e.setId(null);
        return repo.save(e);
    }

    @Override
    public Especie update(Long id, Especie e) {
        Especie current = findById(id);
        current.setNombreCientifico(e.getNombreCientifico());
        current.setNombreComun(e.getNombreComun());
        current.setDensidadMaderaRho(e.getDensidadMaderaRho());
        current.setFuenteRho(e.getFuenteRho());
        current.setVersionRho(e.getVersionRho());
        return repo.save(current);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("Especie no encontrada: " + id);
        repo.deleteById(id);
    }
}
