package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.controller.response.ResultadoParqueResponse;
import sv.edu.udb.repository.EstimacionRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.repository.ResultadoParqueRepository;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.domain.ResultadoParque;
import sv.edu.udb.service.ResultadoParqueService;
import sv.edu.udb.service.mapper.ResultadoParqueMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResultadoParqueServiceImpl implements ResultadoParqueService {

    // === Parámetros del modelo (pueden moverse a config si lo deseas) ===
    private static final double TASA_CAPTURA_CARBONO_KG_POR_M2_POR_ANIO = 0.28;
    private static final double KG_POR_TONELADA = 1000.0;

    @NonNull private final ParqueRepository parqueRepository;
    @NonNull private final EstimacionRepository estimacionRepository;
    @NonNull private final ResultadoParqueRepository resultadoParqueRepository;
    @NonNull private final ResultadoParqueMapper mapper;

    @Override
    public ResultadoParqueResponse recalcular(Long parqueId, int anio) {
        if (parqueId == null || parqueId <= 0) throw new IllegalArgumentException("parqueId inválido");
        if (anio <= 0) throw new IllegalArgumentException("anio inválido");

        Parque parque = parqueRepository.findById(parqueId)
                .orElseThrow(() -> new EntityNotFoundException("Parque no encontrado id " + parqueId));

        // Sumar carbono (kg) de estimaciones cuyo año de medición == anio
        double stockCarbonoKg = estimacionRepository.findByMedicion_Arbol_Parque_Id(parqueId).stream()
                .filter(est -> est.getMedicion() != null
                        && est.getMedicion().getFecha() != null
                        && est.getMedicion().getFecha().getYear() == anio)
                .mapToDouble(est -> est.getCarbonoKg() == null ? 0.0 : est.getCarbonoKg())
                .sum();
        double stockCarbonoT = stockCarbonoKg / KG_POR_TONELADA;

        // Captura anual por área: ha → m2
        double areaM2 = parque.getAreaHa() * 10_000.0;
        double capturaCarbonoKg = TASA_CAPTURA_CARBONO_KG_POR_M2_POR_ANIO * areaM2;
        double capturaCarbonoT = capturaCarbonoKg / KG_POR_TONELADA;

        // Upsert (único por parqueId + anio)
        ResultadoParque entity = resultadoParqueRepository.findByParque_IdAndAnio(parqueId, anio)
                .orElseGet(() -> {
                    ResultadoParque r = new ResultadoParque();
                    r.setParque(parque);
                    r.setAnio(anio);
                    return r;
                });

        entity.setStockCarbonoT(stockCarbonoT);
        entity.setCapturaAnualT(capturaCarbonoT);

        ResultadoParque saved = resultadoParqueRepository.save(entity);
        return mapper.toResponse(saved);
    }

    // ===== Lecturas (sin recalcular) =====

    @Override
    @Transactional(readOnly = true)
    public ResultadoParqueResponse findByParqueAndAnio(Long parqueId, int anio) {
        ResultadoParque r = resultadoParqueRepository.findByParque_IdAndAnio(parqueId, anio)
                .orElseThrow(() -> new EntityNotFoundException("Resultado no encontrado: parque=" + parqueId + ", anio=" + anio));
        return mapper.toResponse(r);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoParqueResponse> findAll() {
        return mapper.toResponseList(resultadoParqueRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoParqueResponse> findByParque(Long parqueId) {
        return mapper.toResponseList(resultadoParqueRepository.findByParque_Id(parqueId));
    }
}
