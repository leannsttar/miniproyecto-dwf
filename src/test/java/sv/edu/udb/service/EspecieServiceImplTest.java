package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.service.impl.EspecieServiceImpl;
import sv.edu.udb.web.dto.request.EspecieRequest;
import sv.edu.udb.web.dto.response.EspecieResponse;
import sv.edu.udb.web.mapper.EspecieMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EspecieServiceImplTest {

    @Mock private EspecieRepository especieRepository;
    @Mock private EspecieMapper especieMapper;
    @Mock private ParqueRepository parqueRepository; // ← tu service lo usa en delete()

    @InjectMocks private EspecieServiceImpl service;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void findAll_ok_devuelveLista() {
        // Arrange
        Especie e = new Especie();
        when(especieRepository.findAll()).thenReturn(List.of(e));
        when(especieMapper.toEspecieResponseList(anyList()))
                .thenReturn(List.of(EspecieResponse.builder().id(1L).build()));

        // Act
        var out = service.findAll();

        // Assert
        assertEquals(1, out.size());
        verify(especieRepository).findAll();
        verify(especieMapper).toEspecieResponseList(anyList());
    }

    @Test
    void findById_ok_devuelveResponse() {
        // Arrange
        Especie e = new Especie(); e.setId(1L);
        when(especieRepository.findById(1L)).thenReturn(Optional.of(e));
        when(especieMapper.toResponse(e)).thenReturn(EspecieResponse.builder().id(1L).build());

        // Act
        var out = service.findById(1L);

        // Assert
        assertEquals(1L, out.getId());
        verify(especieRepository).findById(1L);
        verify(especieMapper).toResponse(e);
    }

    @Test
    void findById_notFound_lanzaEntityNotFound() {
        // Arrange
        when(especieRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () -> service.findById(99L));
        verify(especieRepository).findById(99L);
        verifyNoInteractions(especieMapper);
    }

    @Test
    void save_ok_guardaYDevuelve() {
        // Arrange
        EspecieRequest req = new EspecieRequest();
        Especie entity = new Especie();
        Especie saved = new Especie(); saved.setId(10L);
        EspecieResponse res = EspecieResponse.builder().id(10L).build();

        when(especieMapper.toEntity(req)).thenReturn(entity);
        when(especieRepository.save(entity)).thenReturn(saved);
        when(especieMapper.toResponse(saved)).thenReturn(res);

        // Act
        var out = service.save(req);

        // Assert
        assertNotNull(out);
        assertEquals(10L, out.getId());
        verify(especieMapper).toEntity(req);
        verify(especieRepository).save(entity);
        verify(especieMapper).toResponse(saved);
    }

    @Test
    void update_ok_actualizaExistente() {
        // Arrange
        Long id = 5L;
        Especie current = new Especie(); current.setId(id);
        EspecieRequest req = new EspecieRequest();
        EspecieResponse res = EspecieResponse.builder().id(id).build();

        when(especieRepository.findById(id)).thenReturn(Optional.of(current));
        // mapper.update(...) es void; solo verificamos la invocación
        when(especieRepository.save(current)).thenReturn(current);
        when(especieMapper.toResponse(current)).thenReturn(res);

        // Act
        var out = service.update(id, req);

        // Assert
        assertNotNull(out);
        assertEquals(id, out.getId());
        verify(especieRepository).findById(id);
        verify(especieMapper).update(current, req);
        verify(especieRepository).save(current);
        verify(especieMapper).toResponse(current);
    }

    @Test
    void delete_ok_invocaRepositorio() {
        // Arrange
        when(especieRepository.existsById(7L)).thenReturn(true);
        doNothing().when(especieRepository).deleteById(7L);

        // Act
        service.delete(7L);

        // Assert
        verify(especieRepository).existsById(7L);
        verify(especieRepository).deleteById(7L);
        verifyNoMoreInteractions(especieRepository);
        verifyNoInteractions(especieMapper);
    }

    @Test
    void delete_notFound_lanzaEntityNotFound() {
        // Arrange
        when(especieRepository.existsById(999L)).thenReturn(false);

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () -> service.delete(999L));

        verify(especieRepository).existsById(999L);
        verifyNoMoreInteractions(especieRepository);
        verifyNoInteractions(especieMapper);
    }

}
