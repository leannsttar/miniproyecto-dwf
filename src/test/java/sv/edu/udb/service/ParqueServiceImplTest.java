package sv.edu.udb.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.ParqueRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParqueServiceImplTest {
    @Mock private ParqueRepository repository;

    @InjectMocks private ParqueServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ok_devuelveLista() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(new Parque()));

        // Act
        var out = service.findAll();

        // Assert
        assertEquals(1, out.size());
        verify(repository).findAll();
    }

    @Test
    void findById_ok_devuelveEntidad() {
        // Arrange
        Parque p = new Parque();
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

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
        Parque p = new Parque();
        when(repository.save(p)).thenReturn(p);

        // Act
        var out = service.create(p);

        // Assert
        assertNotNull(out);
        verify(repository).save(p);
    }

    @Test
    void update_ok_actualizaExistente() {
        // Arrange
        Parque existente = new Parque();
        existente.setId(5L);
        when(repository.findById(5L)).thenReturn(Optional.of(existente));
        Parque cambios = new Parque();
        when(repository.save(any(Parque.class))).thenAnswer(a -> a.getArgument(0));

        // Act
        var out = service.update(5L, cambios);

        // Assert
        assertNotNull(out);
        verify(repository).findById(5L);
        verify(repository).save(any(Parque.class));
    }

    @Test
    void delete_ok_invocaRepositorio() {
        // Arrange
        doNothing().when(repository).deleteById(7L);

        // Act
        service.delete(7L);

        // Assert
        verify(repository).deleteById(7L);
    }
}
