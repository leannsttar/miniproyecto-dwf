package sv.edu.udb.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.service.ParqueService;
import sv.edu.udb.web.dto.request.ParqueRequest;
import sv.edu.udb.web.dto.response.ParqueResponse;
import sv.edu.udb.web.mapper.ParqueMapper;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/parques")
public class ParqueController {

    private final ParqueService service;
    private final ParqueMapper mapper;

    public ParqueController(ParqueService service, ParqueMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ParqueResponse> list() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ParqueResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ParqueResponse> create(@Valid @RequestBody ParqueRequest req) {
        Parque saved = service.create(mapper.toEntity(req));
        return ResponseEntity
                .created(URI.create("/api/parques/" + saved.getId()))
                .body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ParqueResponse update(@PathVariable Long id, @Valid @RequestBody ParqueRequest req) {
        Parque current = service.findById(id);
        mapper.update(current, req);
        return mapper.toResponse(service.update(id, current));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
