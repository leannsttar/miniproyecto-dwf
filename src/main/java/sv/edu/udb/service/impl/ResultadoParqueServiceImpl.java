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

    private static final double TASA_C_KG_POR_M2_ANIO = 0.28;

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
        if (anio <= 0) throw new IllegalArgumentException("año inválido");

        Parque parque = parqueRepository.findById(parqueId)
                .orElseThrow(() -> new EntityNotFoundException("Parque no encontrado: " + parqueId));

        // Sumar carbono (kg) de estimaciones del año indicado y convertir a toneladas
        double stockKg = estimacionRepository.findByMedicion_Arbol_Parque_Id(parqueId).stream()
                .filter(e -> e.getMedicion() != null && e.getMedicion().getFecha() != null
                        && e.getMedicion().getFecha().getYear() == anio)
                .mapToDouble(e -> e.getCarbonoKg() == null ? 0.0 : e.getCarbonoKg())
                .sum();
        double stockT = stockKg / 1000.0;

        // Captura anual referencial por área del parque (ha -> m2)
        double m2 = parque.getAreaHa() * 10_000.0;
        double capturaCKg = TASA_C_KG_POR_M2_ANIO * m2;
        double capturaT = capturaCKg / 1000.0;

        // Upsert de resultado
        ResultadoParque entidad = resultadoParqueRepository.findByParque_IdAndAnio(parqueId, anio)
                .orElseGet(() -> {
                    ResultadoParque r = new ResultadoParque();
                    r.setParque(parque);
                    r.setAnio(anio);
                    return r;
                });
        entidad.setStockCarbonoT(stockT);
        entidad.setCapturaAnualT(capturaT);
        resultadoParqueRepository.save(entidad);

        return new ResultadoParqueResumen(parqueId, anio, stockT, capturaT);
    }
}
