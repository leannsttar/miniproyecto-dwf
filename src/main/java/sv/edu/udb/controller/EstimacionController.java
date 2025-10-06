package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.request.EstimacionDesdeMedicionRequest;
import sv.edu.udb.controller.response.EstimacionResponse;
import sv.edu.udb.service.EstimacionService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estimaciones")
public class EstimacionController {

    private final EstimacionService service;

    @GetMapping
    public List<EstimacionResponse> list() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public EstimacionResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    /** Crear desde medición (usa CalculoService) */
    @PostMapping("/desde-medicion")
    @ResponseStatus(CREATED)
    public EstimacionResponse createDesdeMedicion(@Valid @RequestBody EstimacionDesdeMedicionRequest req) {
        return service.createDesdeMedicion(req);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
