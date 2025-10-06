// src/main/java/sv/edu/udb/web/controller/ResultadoParqueController.java
package sv.edu.udb.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.service.ResultadoParqueService;
import sv.edu.udb.controller.request.RecalculoResultadoRequest;
import sv.edu.udb.controller.response.ResultadoParqueResponse;
import sv.edu.udb.service.mapper.ResultadoParqueMapper;

import java.util.List;

@RestController
@RequestMapping("/api/resultados")
public class ResultadoParqueController {

    private final ResultadoParqueService service;
    private final ResultadoParqueMapper mapper;

    public ResultadoParqueController(ResultadoParqueService service, ResultadoParqueMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // ------- POST: recalcular y guardar/upsert -------
    @PostMapping("/recalcular")
    public ResponseEntity<ResultadoParqueResponse> recalcular(@Valid @RequestBody RecalculoResultadoRequest req) {
        var resumen = service.recalcular(req.getParqueId(), req.getAnio());
        return ResponseEntity.ok(mapper.toResponse(resumen));
    }

    // ------- GETs de lectura -------

    /** Lista todos los resultados guardados */
    @GetMapping
    public List<ResultadoParqueResponse> listAll() {
        return mapper.toResponseFromResumen(service.listAll());
    }

    /** Lista todos los resultados de un parque */
    @GetMapping("/parque/{parqueId}")
    public List<ResultadoParqueResponse> listByParque(@PathVariable Long parqueId) {
        return mapper.toResponseFromResumen(service.listByParque(parqueId));
    }

    /** Obtiene el resultado de (parqueId, anio) */
    @GetMapping("/{parqueId}/{anio}")
    public ResultadoParqueResponse getByParqueAndAnio(@PathVariable Long parqueId, @PathVariable int anio) {
        return mapper.toResponse(service.getByParqueAndAnio(parqueId, anio));
    }
}
