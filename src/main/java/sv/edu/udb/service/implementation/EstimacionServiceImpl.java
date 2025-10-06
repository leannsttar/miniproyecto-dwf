package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.request.EstimacionDesdeMedicionRequest;
import sv.edu.udb.controller.response.EstimacionResponse;
import sv.edu.udb.repository.EstimacionRepository;
import sv.edu.udb.repository.MedicionRepository;
import sv.edu.udb.repository.domain.Estimacion;
import sv.edu.udb.repository.domain.Medicion;
import sv.edu.udb.service.CalculoService;
import sv.edu.udb.service.EstimacionService;
import sv.edu.udb.service.mapper.EstimacionMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EstimacionServiceImpl implements EstimacionService {

    @NonNull private final EstimacionRepository estimacionRepository;
    @NonNull private final MedicionRepository medicionRepository;
    @NonNull private final CalculoService calculoService;
    @NonNull private final EstimacionMapper estimacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EstimacionResponse> findAll() {
        return estimacionMapper.toResponseList(estimacionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public EstimacionResponse findById(Long id) {
        Estimacion e = estimacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estimacion no encontrada id " + id));
        return estimacionMapper.toResponse(e);
    }

    @Override
    public EstimacionResponse createDesdeMedicion(EstimacionDesdeMedicionRequest req) {
        Medicion m = medicionRepository.findById(req.getMedicionId())
                .orElseThrow(() -> new EntityNotFoundException("Medicion no encontrada id " + req.getMedicionId()));

        if (m.getArbol() == null || m.getArbol().getEspecie() == null
                || m.getArbol().getEspecie().getDensidadMaderaRho() == null) {
            throw new IllegalStateException("No se puede determinar ρ desde la medición → árbol → especie");
        }

        double rho = m.getArbol().getEspecie().getDensidadMaderaRho().doubleValue();
        double dCm = m.getDbhCm();
        double hM  = m.getAlturaM();
        double cf  = (req.getFraccionCarbono() != null ? req.getFraccionCarbono() : 0.47);

        var r = calculoService.calcular(rho, dCm, hM, cf);

        Estimacion e = new Estimacion();
        e.setMedicion(m);
        e.setBiomasaKg(r.getAgbKg());
        e.setCarbonoKg(r.getCarbonoKg());
        e.setCo2eKg(r.getCo2eKg());
        e.setFraccionCarbono(cf);
        e.setIncertidumbrePorc(null);
        e.setNotas(null);

        return estimacionMapper.toResponse(estimacionRepository.save(e));
    }

    @Override
    public void delete(Long id) {
        if (!estimacionRepository.existsById(id)) {
            throw new EntityNotFoundException("Estimacion no encontrada id " + id);
        }
        estimacionRepository.deleteById(id);
    }
}
