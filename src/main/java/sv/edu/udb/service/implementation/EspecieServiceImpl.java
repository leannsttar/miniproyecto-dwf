package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.udb.repository.domain.Especie;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.EspecieService;
import sv.edu.udb.controller.request.EspecieRequest;
import sv.edu.udb.controller.response.EspecieResponse;
import sv.edu.udb.service.mapper.EspecieMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecieServiceImpl implements EspecieService {

    @NonNull
    private final EspecieRepository especieRepository;

    @NonNull
    private final EspecieMapper especieMapper;
    private final ParqueRepository parqueRepository;


    @Override
    public List<EspecieResponse> findAll() {
        return especieMapper.toEspecieResponseList(especieRepository.findAll());
    }

    @Override
    public EspecieResponse findById(Long id) {
        final Especie entity = especieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Especie no encontrada id " + id));
        return especieMapper.toResponse(entity);
    }

    @Override
    public EspecieResponse save(EspecieRequest request) {
        final Especie entity = especieMapper.toEntity(request);
        return especieMapper.toResponse(especieRepository.save(entity));
    }

    @Override
    public EspecieResponse update(Long id, EspecieRequest request) {
        final Especie current = especieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Especie no encontrada id" + id));

        especieMapper.update(current, request);

        return especieMapper.toResponse(especieRepository.save(current));
    }

    @Override
    public void delete(Long id) {
        if (!especieRepository.existsById(id)) {
            throw new EntityNotFoundException("Especie no encontrada id " + id);
        }
        especieRepository.deleteById(id);
    }
}
