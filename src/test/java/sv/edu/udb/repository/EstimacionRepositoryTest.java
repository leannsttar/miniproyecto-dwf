package sv.edu.udb.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica la consulta con navegación de propiedades:
 * findByMedicion_Arbol_Parque_Id(parqueId)
 */
@DataJpaTest
@ActiveProfiles("test")
class EstimacionRepositoryTest {

    @Autowired EspecieRepository especieRepo;
    @Autowired ParqueRepository parqueRepo;
    @Autowired ArbolRepository arbolRepo;
    @Autowired MedicionRepository medicionRepo;
    @Autowired EstimacionRepository estimacionRepo;

    @Test
    @DisplayName("Devuelve únicamente estimaciones del parque indicado")
    void findByParqueId_onlyThatPark() {
        // Arrange: especie
        Especie esp = new Especie();
        esp.setNombreCientifico("Cedrela odorata");
        esp.setDensidadMaderaRho(new BigDecimal("0.52"));
        esp.setFuenteRho("FAO 2022");
        esp.setVersionRho("v1");
        especieRepo.save(esp);

        // Parques
        Parque p1 = new Parque(); p1.setNombre("P1"); p1.setDistrito("D1"); p1.setAreaHa(1.0); parqueRepo.save(p1);
        Parque p2 = new Parque(); p2.setNombre("P2"); p2.setDistrito("D2"); p2.setAreaHa(1.5); parqueRepo.save(p2);

        // Arboles y mediciones
        Arbol a1 = new Arbol(); a1.setParque(p1); a1.setEspecie(esp); arbolRepo.save(a1);
        Arbol a2 = new Arbol(); a2.setParque(p2); a2.setEspecie(esp); arbolRepo.save(a2);

        Medicion m1 = new Medicion(); m1.setArbol(a1); m1.setFecha(LocalDate.of(2024,1,1)); m1.setDbhCm(30.0); m1.setAlturaM(15.0); medicionRepo.save(m1);
        Medicion m2 = new Medicion(); m2.setArbol(a2); m2.setFecha(LocalDate.of(2024,1,1)); m2.setDbhCm(25.0); m2.setAlturaM(12.0); medicionRepo.save(m2);

        // Estimaciones
        Estimacion e1 = new Estimacion(); e1.setMedicion(m1); e1.setBiomasaKg(100.0); e1.setCarbonoKg(50.0); e1.setCo2eKg(183.3); e1.setFraccionCarbono(0.5); estimacionRepo.save(e1);
        Estimacion e2 = new Estimacion(); e2.setMedicion(m2); e2.setBiomasaKg(90.0);  e2.setCarbonoKg(40.0); e2.setCo2eKg(146.7); e2.setFraccionCarbono(0.5); estimacionRepo.save(e2);

        // Act
        List<Estimacion> delP1 = estimacionRepo.findByMedicion_Arbol_Parque_Id(p1.getId());

        // Assert
        assertEquals(1, delP1.size());
        assertEquals(50.0, delP1.get(0).getCarbonoKg());
    }
}
