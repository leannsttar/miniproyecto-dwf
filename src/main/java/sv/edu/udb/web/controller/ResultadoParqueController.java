package sv.edu.udb.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.service.ResultadoParqueService;
import sv.edu.udb.web.dto.request.RecalculoResultadoRequest;
import sv.edu.udb.web.dto.response.ResultadoParqueResponse;
import sv.edu.udb.web.mapper.ResultadoParqueMapper;

@RestController
@RequestMapping("/api/resultados")
public class ResultadoParqueController {

    private final ResultadoParqueService service;
    private final ResultadoParqueMapper mapper;

    public ResultadoParqueController(ResultadoParqueService service, ResultadoParqueMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/recalcular")
    public ResponseEntity<ResultadoParqueResponse> recalcular(@Valid @RequestBody RecalculoResultadoRequest req) {
        var resumen = service.recalcular(req.getParqueId(), req.getAnio());
        return ResponseEntity.ok(mapper.toResponse(resumen));
    }
}
