package sv.edu.udb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sv.edu.udb.repository.domain.Parque;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ParqueRepositoryTest {

    @Autowired
    private ParqueRepository repository;

    private Parque parqueValido() {
        Parque p = new Parque();
        p.setNombre("Parque Central");
        p.setDistrito("Soyapango");
        p.setAreaHa(1.5);
        // lat/lon opcionales
        return p;
    }

    @Test
    void save_ok_generaId() {
        // Arrange
        Parque p = parqueValido();

        // Act
        Parque saved = repository.save(p);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("Parque Central", saved.getNombre());
    }

    @Test
    void findById_ok_encuentra() {
        // Arrange
        Parque saved = repository.save(parqueValido());

        // Act
        var found = repository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getNombre(), found.get().getNombre());
    }

    @Test
    void deleteById_ok_elimina() {
        // Arrange
        Parque saved = repository.save(parqueValido());

        // Act
        repository.deleteById(saved.getId());

        // Assert
        assertFalse(repository.findById(saved.getId()).isPresent());
    }
}
