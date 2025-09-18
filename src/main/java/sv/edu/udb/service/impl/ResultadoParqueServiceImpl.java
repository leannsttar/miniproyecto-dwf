package sv.edu.udb.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.domain.ResultadoParque;
import sv.edu.udb.repository.EstimacionRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.repository.ResultadoParqueRepository;
import sv.edu.udb.service.ResultadoParqueService;
import sv.edu.udb.service.dto.ResultadoParqueResumen;

@Service
public class ResultadoParqueServiceImpl implements ResultadoParqueService {

    private static final double TASA_CAPTURA_CARBONO_KG_POR_M2_POR_ANIO = 0.28;
    private static final double KG_POR_TONELADA = 1000.0;

    private final ParqueRepository parqueRepository;
    private final EstimacionRepository estimacionRepository;
    private final ResultadoParqueRepository resultadoParqueRepository;

    public ResultadoParqueServiceImpl(ParqueRepository parqueRepository,
                                      EstimacionRepository estimacionRepository,
                                      ResultadoParqueRepository resultadoParqueRepository) {
        this.parqueRepository = parqueRepository;
        this.estimacionRepository = estimacionRepository;
        this.resultadoParqueRepository = resultadoParqueRepository;
    }

    @Override
    @Transactional
    public ResultadoParqueResumen recalcular(Long parqueId, int anio) {
        if (parqueId == null || parqueId <= 0) throw new IllegalArgumentException("parqueId inválido");
        if (anio <= 0) throw new IllegalArgumentException("anio inválido");

        Parque parque = parqueRepository.findById(parqueId)
                .orElseThrow(() -> new EntityNotFoundException("Parque no encontrado: " + parqueId));

        // Sumar C (kg) solo del año objetivo
        double stockCarbonoKg = estimacionRepository.findByMedicion_Arbol_Parque_Id(parqueId).stream()
                .filter(estimacion -> estimacion.getMedicion() != null
                        && estimacion.getMedicion().getFecha() != null
                        && estimacion.getMedicion().getFecha().getYear() == anio)
                .mapToDouble(estimacion -> estimacion.getCarbonoKg() == null ? 0.0 : estimacion.getCarbonoKg())
                .sum();
        double stockCarbonoT = stockCarbonoKg / KG_POR_TONELADA;

        // Captura anual por área (ha → m2)
        double areaM2 = parque.getAreaHa() * 10_000.0;
        double capturaCarbonoKg = TASA_CAPTURA_CARBONO_KG_POR_M2_POR_ANIO * areaM2;
        double capturaCarbonoT = capturaCarbonoKg / KG_POR_TONELADA;

        // Upsert
        ResultadoParque resultadoParque = resultadoParqueRepository.findByParque_IdAndAnio(parqueId, anio)
                .orElseGet(() -> {
                    ResultadoParque resultado = new ResultadoParque();
                    resultado.setParque(parque);
                    resultado.setAnio(anio);
                    return resultado;
                });

        resultadoParque.setStockCarbonoT(stockCarbonoT);
        resultadoParque.setCapturaAnualT(capturaCarbonoT);
        resultadoParqueRepository.save(resultadoParque);

        return new ResultadoParqueResumen(parqueId, anio, stockCarbonoT, capturaCarbonoT);
    }
}
