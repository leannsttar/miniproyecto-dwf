package sv.edu.udb.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.service.EspecieService;
import sv.edu.udb.web.dto.request.EspecieRequest;
import sv.edu.udb.web.dto.response.EspecieResponse;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/especies")
public class EspecieController {

    private final EspecieService service;

    @GetMapping
    public List<EspecieResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EspecieResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public EspecieResponse create(@Valid @RequestBody EspecieRequest req) {
        return service.save(req);
    }

    @PutMapping("{id}")
    public EspecieResponse update(@PathVariable Long id, @Valid @RequestBody EspecieRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
