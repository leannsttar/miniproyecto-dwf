package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.request.MedicionRequest;
import sv.edu.udb.controller.response.MedicionResponse;
import sv.edu.udb.service.MedicionService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mediciones")
public class MedicionController {

    private final MedicionService service;

    @GetMapping
    public List<MedicionResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public MedicionResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public MedicionResponse save(@Valid @RequestBody MedicionRequest req) {
        return service.save(req);
    }

    @PutMapping("{id}")
    public MedicionResponse update(@PathVariable Long id, @Valid @RequestBody MedicionRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
