package sv.edu.udb.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.domain.Medicion;
import sv.edu.udb.service.MedicionService;
import sv.edu.udb.web.dto.request.MedicionRequest;
import sv.edu.udb.web.dto.response.MedicionResponse;
import sv.edu.udb.web.mapper.MedicionMapper;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/mediciones")
public class MedicionController {

    private final MedicionService service;
    private final MedicionMapper mapper;

    public MedicionController(MedicionService service, MedicionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<MedicionResponse> list() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public MedicionResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<MedicionResponse> create(@Valid @RequestBody MedicionRequest req) {
        Medicion saved = service.create(mapper.toEntity(req));
        return ResponseEntity
                .created(URI.create("/api/mediciones/" + saved.getId()))
                .body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public MedicionResponse update(@PathVariable Long id, @Valid @RequestBody MedicionRequest req) {
        Medicion current = service.findById(id);
        mapper.update(current, req);
        return mapper.toResponse(service.update(id, current));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
