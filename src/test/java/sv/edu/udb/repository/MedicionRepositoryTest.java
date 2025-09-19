package sv.edu.udb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.domain.Medicion;
import sv.edu.udb.domain.Parque;
import java.util.UUID;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MedicionRepositoryTest {

    @Autowired private MedicionRepository medicionRepository;
    @Autowired private ArbolRepository arbolRepository;
    @Autowired private ParqueRepository parqueRepository;
    @Autowired private EspecieRepository especieRepository;

    private Parque parqueValido() {
        Parque p = new Parque();
        p.setNombre("Parque Medicion");
        p.setDistrito("Distrito 2");
        p.setAreaHa(3.0);
        return p;
    }

    private Especie especieValida() {
        Especie e = new Especie();
        e.setNombreCientifico("Tabebuia rosea " + UUID.randomUUID()); // <- único
        e.setFuenteRho("db-default");
        e.setVersionRho("v1");
        e.setDensidadMaderaRho(new BigDecimal("0.72"));
        return e;
    }

    private Arbol arbolValido() {
        Arbol a = new Arbol();
        a.setParque(parqueRepository.save(parqueValido()));
        a.setEspecie(especieRepository.save(especieValida()));
        a.setLat(13.70);
        a.setLon(-89.19);
        return a;
    }

    private Medicion medicionValida() {
        Medicion m = new Medicion();
        m.setArbol(arbolRepository.save(arbolValido()));
        m.setFecha(LocalDate.now());
        m.setDbhCm(25.0);
        m.setAlturaM(12.0);
        m.setObservaciones("OK");
        return m;
    }

    @Test
    void save_ok_conArbolYMetricas() {
        // Arrange
        Medicion m = medicionValida();

        // Act
        Medicion saved = medicionRepository.save(m);

        // Assert
        assertNotNull(saved.getId());
        assertNotNull(saved.getArbol());
        assertEquals(25.0, saved.getDbhCm());
        assertTrue(saved.getAlturaM() >= 0.0);
    }

    @Test
    void findById_ok_encuentra() {
        // Arrange
        Medicion saved = medicionRepository.save(medicionValida());

        // Act
        var found = medicionRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getDbhCm(), found.get().getDbhCm());
    }
}
