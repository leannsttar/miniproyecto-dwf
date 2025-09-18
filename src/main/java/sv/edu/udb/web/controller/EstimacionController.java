package sv.edu.udb.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.domain.Estimacion;
import sv.edu.udb.service.EstimacionService;
import sv.edu.udb.web.dto.request.EstimacionDesdeMedicionRequest;
import sv.edu.udb.web.dto.request.EstimacionManualRequest;
import sv.edu.udb.web.dto.response.EstimacionResponse;
import sv.edu.udb.web.mapper.EstimacionMapper;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/estimaciones")
public class EstimacionController {

    private final EstimacionService service;
    private final EstimacionMapper mapper;

    public EstimacionController(EstimacionService service, EstimacionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<EstimacionResponse> list() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public EstimacionResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    /** Crear manual con todos los valores ya calculados */
    @PostMapping
    public ResponseEntity<EstimacionResponse> createManual(@Valid @RequestBody EstimacionManualRequest req) {
        Estimacion saved = service.create(mapper.toEntity(req));
        return ResponseEntity
                .created(URI.create("/api/estimaciones/" + saved.getId()))
                .body(mapper.toResponse(saved));
    }

    /** Crear desde medición: usa CalculoService internamente */
    @PostMapping("/desde-medicion")
    public ResponseEntity<EstimacionResponse> createDesdeMedicion(@Valid @RequestBody EstimacionDesdeMedicionRequest req) {
        Estimacion saved = service.createFromMedicion(req.getMedicionId(), req.getFraccionCarbono());
        return ResponseEntity
                .created(URI.create("/api/estimaciones/" + saved.getId()))
                .body(mapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
