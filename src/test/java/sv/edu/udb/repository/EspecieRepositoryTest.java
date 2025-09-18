package sv.edu.udb.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.domain.Especie;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de JPA repository para consultas derivadas.
 * Usa H2 en memoria con @DataJpaTest.
 */
@DataJpaTest
@ActiveProfiles("test")
class EspecieRepositoryTest {

    @Autowired
    private EspecieRepository especieRepository;

    @Test
    @DisplayName("findByNombreCientificoIgnoreCase - encuentra coincidencia insensible a mayúsculas")
    void findByNombreCientificoIgnoreCase_ok() {
        // Arrange: guardar una especie
        Especie e = new Especie();
        e.setNombreCientifico("Tabebuia rosea");
        e.setNombreComun("Roble de sabana");
        e.setDensidadMaderaRho(new BigDecimal("0.65"));
        e.setFuenteRho("FAO 2022");
        e.setVersionRho("v1");
        especieRepository.save(e);

        // Act: buscar con mayúsculas distintas
        Optional<Especie> opt = especieRepository.findByNombreCientificoIgnoreCase("TABEBUIA ROSEA");

        // Assert
        assertTrue(opt.isPresent());
        assertEquals("Tabebuia rosea", opt.get().getNombreCientifico());
    }

    @Test
    @DisplayName("findByNombreCientificoIgnoreCase - retorna vacío si no existe")
    void findByNombreCientificoIgnoreCase_notFound() {
        assertTrue(especieRepository.findByNombreCientificoIgnoreCase("inexistente").isEmpty());
    }
}
