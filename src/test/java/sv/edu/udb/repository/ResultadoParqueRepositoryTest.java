package sv.edu.udb.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.domain.ResultadoParque;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica búsqueda por clave compuesta lógica (parque_id + anio).
 */
@DataJpaTest
@ActiveProfiles("test")
class ResultadoParqueRepositoryTest {

    @Autowired ParqueRepository parqueRepo;
    @Autowired ResultadoParqueRepository resultadoRepo;

    @Test
    @DisplayName("findByParque_IdAndAnio - encuentra el registro correcto")
    void findByParqueAndYear() {
        Parque p = new Parque(); p.setNombre("P"); p.setDistrito("D"); p.setAreaHa(1.0); parqueRepo.save(p);

        ResultadoParque r = new ResultadoParque();
        r.setParque(p); r.setAnio(2025); r.setStockCarbonoT(1.23); r.setCapturaAnualT(2.34);
        resultadoRepo.save(r);

        Optional<ResultadoParque> opt = resultadoRepo.findByParque_IdAndAnio(p.getId(), 2025);
        assertTrue(opt.isPresent());
        assertEquals(1.23, opt.get().getStockCarbonoT());
    }
}
