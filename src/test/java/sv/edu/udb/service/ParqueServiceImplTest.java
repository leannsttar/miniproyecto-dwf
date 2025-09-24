package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.impl.ParqueServiceImpl;
import sv.edu.udb.web.dto.request.ParqueRequest;
import sv.edu.udb.web.dto.response.ParqueResponse;
import sv.edu.udb.web.mapper.ParqueMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParqueServiceImplTest {

    @Mock private ParqueRepository repository;
    @Mock private ParqueMapper mapper;

    @InjectMocks private ParqueServiceImpl service;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void findAll_ok_devuelveLista() {
        // Arrange
        Parque e = new Parque();
        when(repository.findAll()).thenReturn(List.of(e));
        when(mapper.toParqueResponseList(anyList()))
                .thenReturn(List.of(ParqueResponse.builder().id(1L).build()));


        // Act
        var out = service.findAll();

        // Assert
        assertEquals(1, out.size());
        verify(repository).findAll();
    }

    @Test
    void findById_ok_devuelveResponse() {
        // Arrange
        Parque e = new Parque();
        e.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(e));
        when(mapper.toResponse(e)).thenReturn(ParqueResponse.builder().id(1L).build());

        // Act
        var out = service.findById(1L);

        // Assert
        assertEquals(1L, out.getId());
        verify(repository).findById(1L);
        verify(mapper).toResponse(e);
    }

    @Test
    void findById_notFound_lanzaEntityNotFound() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () -> service.findById(99L));
        verify(repository).findById(99L);
        verifyNoInteractions(mapper);
    }

    @Test
    void save_ok_guardaYDevuelve() {
        // Arrange
        ParqueRequest req = new ParqueRequest();
        Parque entity = new Parque();
        Parque saved = new Parque();
        saved.setId(10L);
        ParqueResponse res = ParqueResponse.builder().id(10L).build();

        when(mapper.toEntity(req)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(res);

        // Act
        var out = service.save(req);

        // Assert
        assertNotNull(out);
        assertEquals(10L, out.getId());
        verify(mapper).toEntity(req);
        verify(repository).save(entity);
        verify(mapper).toResponse(saved);
    }

    @Test
    void update_ok_actualizaExistente() {
        // Arrange
        Long id = 5L;
        Parque current = new Parque();
        current.setId(id);
        ParqueRequest req = new ParqueRequest();
        ParqueResponse res = ParqueResponse.builder().id(id).build();

        when(repository.findById(id)).thenReturn(Optional.of(current));
        when(repository.save(current)).thenReturn(current);
        when(mapper.toResponse(current)).thenReturn(res);

        // Act
        var out = service.update(id, req);

        // Assert
        assertNotNull(out);
        assertEquals(id, out.getId());
        verify(repository).findById(id);
        verify(mapper).update(current, req);
        verify(repository).save(current);
        verify(mapper).toResponse(current);
    }

    @Test
    void delete_ok_invocaRepositorio() {
        // Arrange
        doNothing().when(repository).deleteById(7L);

        // Act
        service.delete(7L);

        // Assert
        verify(repository).deleteById(7L);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }
}
