package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.request.RecalculoResultadoRequest;
import sv.edu.udb.controller.response.ResultadoParqueResponse;
import sv.edu.udb.service.ResultadoParqueService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resultados")
public class ResultadoParqueController {

    private final ResultadoParqueService service;

    /** POST: recalcular (upsert) y devolver el resultado recalculado */
    @PostMapping("/recalcular")
    public ResultadoParqueResponse recalcular(@Valid @RequestBody RecalculoResultadoRequest req) {
        return service.recalcular(req.getParqueId(), req.getAnio());
    }

    /** GET: todos los resultados */
    @GetMapping
    public List<ResultadoParqueResponse> listAll() {
        return service.findAll();
    }

    /** GET: resultados de un parque (todos los años) */
    @GetMapping("/parque/{parqueId}")
    public List<ResultadoParqueResponse> listByParque(@PathVariable Long parqueId) {
        return service.findByParque(parqueId);
    }

    /** GET: resultado puntual de (parque, año) */
    @GetMapping("/{parqueId}/{anio}")
    public ResultadoParqueResponse getByParqueAndAnio(@PathVariable Long parqueId, @PathVariable int anio) {
        return service.findByParqueAndAnio(parqueId, anio);
    }
}
