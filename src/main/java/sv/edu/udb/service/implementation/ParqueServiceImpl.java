package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.ParqueService;
import sv.edu.udb.controller.request.ParqueRequest;
import sv.edu.udb.controller.response.ParqueResponse;
import sv.edu.udb.service.mapper.ParqueMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParqueServiceImpl implements ParqueService {

    @NonNull
    private final ParqueRepository parqueRepository;

    @NonNull
    private final ParqueMapper parqueMapper;


    @Override
    public List<ParqueResponse> findAll() {
        return parqueMapper.toParqueResponseList(parqueRepository.findAll());
    }

    @Override
    public ParqueResponse findById(final Long id) {
        final Parque entity = parqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(("Parque no encontrado id " + id)));
        return parqueMapper.toResponse(entity);
    }

    @Override
    public ParqueResponse save(final ParqueRequest request) {
        final Parque entity = parqueMapper.toEntity(request);
        return parqueMapper.toResponse(parqueRepository.save(entity));
    }

    @Override
    public ParqueResponse update(final Long id, final ParqueRequest request) {
        final Parque current = parqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parque no encontrado id " + id));

        parqueMapper.update(current, request);

        return parqueMapper.toResponse(parqueRepository.save(current));
    }

    @Override
    public void delete(final Long id) {
        if (!parqueRepository.existsById(id)) {
            throw new EntityNotFoundException("Especie no encontrada id " + id);
        }
        parqueRepository.deleteById(id);
    }
}
