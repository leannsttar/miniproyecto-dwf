package sv.edu.udb.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.service.impl.EspecieServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EspecieServiceImplTest {
    @Mock private EspecieRepository repository;

    @InjectMocks private EspecieServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ok_devuelveLista() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(new Especie()));

        // Act
        var out = service.findAll();

        // Assert
        assertEquals(1, out.size());
        verify(repository).findAll();
    }

    @Test
    void findById_ok_devuelveEntidad() {
        // Arrange
        Especie e = new Especie();
        e.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(e));

        // Act
        var out = service.findById(1L);

        // Assert
        assertEquals(1L, out.getId());
        verify(repository).findById(1L);
    }

    @Test
    void findById_notFound_lanzaEntityNotFound() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () -> service.findById(99L));
        verify(repository).findById(99L);
    }

    @Test
    void create_ok_guardaYDevuelve() {
        // Arrange
        Especie e = new Especie();
        when(repository.save(e)).thenReturn(e);

        // Act
        var out = service.create(e);

        // Assert
        assertNotNull(out);
        verify(repository).save(e);
    }

    @Test
    void update_ok_actualizaExistente() {
        // Arrange
        Especie existente = new Especie();
        existente.setId(5L);
        when(repository.findById(5L)).thenReturn(Optional.of(existente));
        Especie cambios = new Especie();
        when(repository.save(any(Especie.class))).thenAnswer(a -> a.getArgument(0));

        // Act
        var out = service.update(5L, cambios);

        // Assert
        assertNotNull(out);
        verify(repository).findById(5L);
        verify(repository).save(any(Especie.class));
    }

    @Test
    void delete_ok_invocaRepositorio() {
        // Arrange
        when(repository.existsById(7L)).thenReturn(true);
        doNothing().when(repository).deleteById(7L);

        // Act
        service.delete(7L);

        // Assert
        verify(repository).deleteById(7L);
    }
}
