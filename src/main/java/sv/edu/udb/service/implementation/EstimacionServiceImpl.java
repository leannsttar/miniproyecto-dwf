package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.repository.domain.Medicion;
import sv.edu.udb.repository.domain.Estimacion;
import sv.edu.udb.repository.EstimacionRepository;
import sv.edu.udb.repository.MedicionRepository;
import sv.edu.udb.service.CalculoService;
import sv.edu.udb.service.EstimacionService;

import java.util.List;

@Service @Transactional
public class EstimacionServiceImpl implements EstimacionService {

    private final EstimacionRepository repo;
    private final MedicionRepository medicionRepo;
    private final CalculoService calculoService;

    public EstimacionServiceImpl(EstimacionRepository repo, MedicionRepository medicionRepo, CalculoService calculoService) {
        this.repo = repo; this.medicionRepo = medicionRepo; this.calculoService = calculoService;
    }

    @Override @Transactional(readOnly = true)
    public List<Estimacion> findAll() { return repo.findAll(); }

    @Override @Transactional(readOnly = true)
    public Estimacion findById(Long id) { return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Estimacion no encontrada: " + id)); }

    @Override
    public Estimacion create(Estimacion e) { if (e.getId()!=null) e.setId(null); return repo.save(e); }

    @Override
    public Estimacion createFromMedicion(Long medicionId, Double fraccionCarbono) {
        Medicion m = medicionRepo.findById(medicionId).orElseThrow(() -> new EntityNotFoundException("Medicion no encontrada: " + medicionId));
        if (m.getArbol()==null || m.getArbol().getEspecie()==null || m.getArbol().getEspecie().getDensidadMaderaRho()==null) {
            throw new IllegalStateException("No se puede determinar ρ desde la medición → árbol → especie");
        }
        double rho = m.getArbol().getEspecie().getDensidadMaderaRho().doubleValue();
        double dCm = m.getDbhCm();
        double hM = m.getAlturaM();
        var r = calculoService.calcular(rho, dCm, hM, fraccionCarbono);
        Estimacion e = new Estimacion();
        e.setMedicion(m);
        e.setBiomasaKg(r.getAgbKg());
        e.setCarbonoKg(r.getCarbonoKg());
        e.setCo2eKg(r.getCo2eKg());
        e.setFraccionCarbono(fraccionCarbono != null ? fraccionCarbono : 0.47);
        return repo.save(e);
    }

    @Override public void delete(Long id) { if (!repo.existsById(id)) throw new EntityNotFoundException("Estimacion no encontrada: " + id); repo.deleteById(id); }
}
