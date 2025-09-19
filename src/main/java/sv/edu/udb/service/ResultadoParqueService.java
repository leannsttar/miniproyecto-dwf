package sv.edu.udb.service;

import sv.edu.udb.service.dto.ResultadoParqueResumen;

import java.util.List;

public interface ResultadoParqueService {

    ResultadoParqueResumen recalcular(Long parqueId, int anio);

    // ---- Lecturas:
    ResultadoParqueResumen getByParqueAndAnio(Long parqueId, int anio);

    List<ResultadoParqueResumen> listAll();

    List<ResultadoParqueResumen> listByParque(Long parqueId);
}
