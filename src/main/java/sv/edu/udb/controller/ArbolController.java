package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.service.ArbolService;
import sv.edu.udb.controller.request.ArbolRequest;
import sv.edu.udb.controller.response.ArbolResponse;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/arboles")
public class ArbolController {

    private final ArbolService service;

    @GetMapping
    public List<ArbolResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public ArbolResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ArbolResponse save(@Valid @RequestBody ArbolRequest req) {
        return service.save(req);
    }

    @PutMapping("{id}")
    public ArbolResponse update(@PathVariable Long id, @Valid @RequestBody ArbolRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
