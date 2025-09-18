package sv.edu.udb.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.service.EspecieService;
import sv.edu.udb.web.dto.request.EspecieRequest;
import sv.edu.udb.web.dto.response.EspecieResponse;
import sv.edu.udb.web.mapper.EspecieMapper;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/especies")
public class EspecieController {

    private final EspecieService service;
    private final EspecieMapper mapper;

    public EspecieController(EspecieService service, EspecieMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<EspecieResponse> list() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public EspecieResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<EspecieResponse> create(@Valid @RequestBody EspecieRequest req) {
        Especie toSave = mapper.toEntity(req);
        Especie saved = service.create(toSave);
        return ResponseEntity
                .created(URI.create("/api/especies/" + saved.getId()))
                .body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public EspecieResponse update(@PathVariable Long id, @Valid @RequestBody EspecieRequest req) {
        Especie current = service.findById(id);
        mapper.update(current, req);
        return mapper.toResponse(service.update(id, current));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
