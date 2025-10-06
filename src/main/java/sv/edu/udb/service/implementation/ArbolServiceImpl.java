package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.udb.repository.domain.Arbol;
import sv.edu.udb.repository.domain.Especie;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.ArbolService;
import sv.edu.udb.controller.request.ArbolRequest;
import sv.edu.udb.controller.response.ArbolResponse;
import sv.edu.udb.service.mapper.ArbolMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArbolServiceImpl implements ArbolService {

    @NonNull private final ArbolRepository arbolRepository;
    @NonNull private final ParqueRepository parqueRepository;
    @NonNull private final EspecieRepository especieRepository;
    @NonNull private final ArbolMapper arbolMapper;

    @Override
    public List<ArbolResponse> findAll() {
        return arbolMapper.toArbolResponseList(arbolRepository.findAll());
    }

    @Override
    public ArbolResponse findById(Long id) {
        Arbol entity = arbolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Arbol no encontrado id " + id));
        return arbolMapper.toResponse(entity);
    }

    @Override
    public ArbolResponse save(ArbolRequest request) {
        Arbol entity = arbolMapper.toEntity(request);

        // Validar y adjuntar referenciasz
        Parque parque = parqueRepository.findById(request.getParqueId())
                .orElseThrow(() -> new EntityNotFoundException("Parque no encontrado id " + request.getParqueId()));
        Especie especie = especieRepository.findById(request.getEspecieId())
                .orElseThrow(() -> new EntityNotFoundException("Especie no encontrada id " + request.getEspecieId()));

        entity.setParque(parque);
        entity.setEspecie(especie);

        return arbolMapper.toResponse(arbolRepository.save(entity));
    }

    @Override
    public ArbolResponse update(Long id, ArbolRequest request) {
        Arbol current = arbolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Arbol no encontrado id " + id));

        // Actualiza campos simples (lat/lon)
        arbolMapper.update(current, request);

        // Si vienen ids de relaciones, validamos y reemplazamos por entidades gestionadas
        if (request.getParqueId() != null) {
            Parque parque = parqueRepository.findById(request.getParqueId())
                    .orElseThrow(() -> new EntityNotFoundException("Parque no encontrado id " + request.getParqueId()));
            current.setParque(parque);
        }
        if (request.getEspecieId() != null) {
            Especie especie = especieRepository.findById(request.getEspecieId())
                    .orElseThrow(() -> new EntityNotFoundException("Especie no encontrada id " + request.getEspecieId()));
            current.setEspecie(especie);
        }

        return arbolMapper.toResponse(arbolRepository.save(current));
    }

    @Override
    public void delete(Long id) {
        if (!arbolRepository.existsById(id)) {
            throw new EntityNotFoundException("Arbol no encontrado id " + id);
        }
        arbolRepository.deleteById(id);
    }
}
