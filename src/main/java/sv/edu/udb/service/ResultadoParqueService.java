package sv.edu.udb.service;

import sv.edu.udb.service.dto.ResultadoParqueResumen;

public interface ResultadoParqueService {
    ResultadoParqueResumen recalcular(Long parqueId, int anio);
}
