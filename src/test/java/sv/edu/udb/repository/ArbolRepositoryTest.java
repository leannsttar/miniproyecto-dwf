package sv.edu.udb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sv.edu.udb.repository.domain.Arbol;
import sv.edu.udb.repository.domain.Especie;
import sv.edu.udb.repository.domain.Parque;
import java.util.UUID;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ArbolRepositoryTest {

    @Autowired private ArbolRepository arbolRepository;
    @Autowired private ParqueRepository parqueRepository;
    @Autowired private EspecieRepository especieRepository;

    private Parque parqueValido() {
        Parque p = new Parque();
        p.setNombre("Parque Arboles");
        p.setDistrito("Distrito 1");
        p.setAreaHa(2.0);
        return p;
    }

    private Especie especieValida() {
        Especie e = new Especie();
        e.setNombreCientifico("Swietenia macrophylla" + UUID.randomUUID());
        e.setFuenteRho("db-default");
        e.setVersionRho("v1");
        e.setDensidadMaderaRho(new BigDecimal("0.59"));
        return e;
    }

    private Arbol arbolValido() {
        Arbol a = new Arbol();
        a.setParque(parqueRepository.save(parqueValido()));
        a.setEspecie(especieRepository.save(especieValida()));
        a.setLat(13.70);
        a.setLon(-89.20);
        return a;
    }

    @Test
    void save_ok_conRelaciones() {
        // Arrange
        Arbol a = arbolValido();

        // Act
        Arbol saved = arbolRepository.save(a);

        // Assert
        assertNotNull(saved.getId());
        assertNotNull(saved.getParque());
        assertNotNull(saved.getEspecie());
        assertEquals(13.70, saved.getLat());
    }

    @Test
    void delete_ok_elimina() {
        // Arrange
        Arbol saved = arbolRepository.save(arbolValido());

        // Act
        arbolRepository.deleteById(saved.getId());

        // Assert
        assertFalse(arbolRepository.findById(saved.getId()).isPresent());
    }
}
