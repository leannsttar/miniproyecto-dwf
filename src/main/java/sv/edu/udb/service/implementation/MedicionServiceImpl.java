package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.udb.repository.domain.Arbol;
import sv.edu.udb.repository.domain.Medicion;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.MedicionRepository;
import sv.edu.udb.controller.request.MedicionRequest;
import sv.edu.udb.controller.response.MedicionResponse;
import sv.edu.udb.service.MedicionService;
import sv.edu.udb.service.mapper.MedicionMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicionServiceImpl implements MedicionService {

    @NonNull private final MedicionRepository medicionRepository;
    @NonNull private final ArbolRepository arbolRepository;
    @NonNull private final MedicionMapper medicionMapper;

    @Override
    public List<MedicionResponse> findAll() {
        return medicionMapper.toResponseList(medicionRepository.findAll());
    }

    @Override
    public MedicionResponse findById(Long id) {
        Medicion entity = medicionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medición no encontrada id " + id));
        return medicionMapper.toResponse(entity);
    }

    @Override
    public MedicionResponse save(MedicionRequest request) {
        Medicion entity = medicionMapper.toEntity(request);

        Arbol arbol = arbolRepository.findById(request.getArbolId())
                .orElseThrow(() -> new EntityNotFoundException("Árbol no encontrado id " + request.getArbolId()));
        entity.setArbol(arbol);

        return medicionMapper.toResponse(medicionRepository.save(entity));
    }

    @Override
    public MedicionResponse update(Long id, MedicionRequest request) {
        Medicion current = medicionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medición no encontrada id " + id));

        medicionMapper.update(current, request);

        if (request.getArbolId() != null) {
            Arbol arbol = arbolRepository.findById(request.getArbolId())
                    .orElseThrow(() -> new EntityNotFoundException("Árbol no encontrado id " + request.getArbolId()));
            current.setArbol(arbol);
        }

        return medicionMapper.toResponse(medicionRepository.save(current));
    }

    @Override
    public void delete(Long id) {
        if (!medicionRepository.existsById(id)) {
            throw new EntityNotFoundException("Medición no encontrada id " + id);
        }
        medicionRepository.deleteById(id);
    }
}
