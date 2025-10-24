package sv.edu.udb.service;

import sv.edu.udb.controller.response.ResultadoParqueResponse;

import java.util.List;

public interface ResultadoParqueService {

    /** Calcula y guarda el resultado de un parque para un año. */
    ResultadoParqueResponse recalcular(Long parqueId, int anio);

    /** Obtiene el resultado de un parque para un año */
    ResultadoParqueResponse findByParqueAndAnio(Long parqueId, int anio);

    /** Lista todos los resultados */
    List<ResultadoParqueResponse> findAll();

    /** Lista los resultados de un parque por año */
    List<ResultadoParqueResponse> findByParque(Long parqueId);
}
