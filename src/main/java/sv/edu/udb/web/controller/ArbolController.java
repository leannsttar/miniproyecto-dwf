package sv.edu.udb.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.service.ArbolService;
import sv.edu.udb.web.dto.request.ArbolRequest;
import sv.edu.udb.web.dto.response.ArbolResponse;
import sv.edu.udb.web.mapper.ArbolMapper;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/arboles")
public class ArbolController {

    private final ArbolService service;
    private final ArbolMapper mapper;

    public ArbolController(ArbolService service, ArbolMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ArbolResponse> list() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ArbolResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ArbolResponse> create(@Valid @RequestBody ArbolRequest req) {
        Arbol saved = service.create(mapper.toEntity(req));
        return ResponseEntity
                .created(URI.create("/api/arboles/" + saved.getId()))
                .body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ArbolResponse update(@PathVariable Long id, @Valid @RequestBody ArbolRequest req) {
        Arbol current = service.findById(id);
        mapper.update(current, req);
        return mapper.toResponse(service.update(id, current));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
