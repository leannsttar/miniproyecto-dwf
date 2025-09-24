package sv.edu.udb.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.service.ParqueService;
import sv.edu.udb.web.dto.request.ParqueRequest;
import sv.edu.udb.web.dto.response.ParqueResponse;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parques")
public class ParqueController {

    private final ParqueService service;

    @GetMapping
    public List<ParqueResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public ParqueResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ParqueResponse save(@Valid @RequestBody ParqueRequest req) {
        return service.save(req);
    }

    @PutMapping("{id}")
    public ParqueResponse update(@PathVariable Long id, @Valid @RequestBody ParqueRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
