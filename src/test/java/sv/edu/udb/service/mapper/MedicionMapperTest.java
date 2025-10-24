package sv.edu.udb.service.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sv.edu.udb.controller.request.MedicionRequest;
import sv.edu.udb.controller.response.MedicionResponse;
import sv.edu.udb.repository.domain.Arbol;
import sv.edu.udb.repository.domain.Medicion;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MedicionMapperTest {

    private final MedicionMapper mapper = Mappers.getMapper(MedicionMapper.class);

    @Test
    void toEntity_ok_mapeaCampos() {
        // Arrange
        MedicionRequest req = new MedicionRequest();
        req.setArbolId(5L);
        req.setFecha(LocalDate.of(2024, 5, 10));
        req.setDbhCm(15.5);
        req.setAlturaM(7.2);
        req.setObservaciones("Primera medición");

        // Act
        Medicion m = mapper.toEntity(req);

        // Assert
        assertNull(m.getId());
        assertNotNull(m.getArbol());
        assertEquals(5L, m.getArbol().getId());
        assertEquals(LocalDate.of(2024, 5, 10), m.getFecha());
        assertEquals(15.5, m.getDbhCm());
        assertEquals(7.2, m.getAlturaM());
        assertEquals("Primera medición", m.getObservaciones());
    }

    @Test
    void toResponse_ok_mapeaCampos() {
        // Arrange
        Arbol a = new Arbol();
        a.setId(8L);

        Medicion m = new Medicion();
        m.setId(3L);
        m.setArbol(a);
        m.setFecha(LocalDate.of(2023, 1, 1));
        m.setDbhCm(20.0);
        m.setAlturaM(10.0);
        m.setObservaciones("Medición base");

        // Act
        MedicionResponse r = mapper.toResponse(m);

        // Assert
        assertEquals(3L, r.getId());
        assertEquals(8L, r.getArbolId());
        assertEquals(LocalDate.of(2023, 1, 1), r.getFecha());
        assertEquals(20.0, r.getDbhCm());
        assertEquals(10.0, r.getAlturaM());
        assertEquals("Medición base", r.getObservaciones());
    }

    @Test
    void update_ok_actualizaCamposDesdeRequest() {
        // Arrange
        Arbol a = new Arbol(); a.setId(1L);
        Medicion target = new Medicion();
        target.setId(9L);
        target.setArbol(a);
        target.setFecha(LocalDate.of(2022, 1, 1));
        target.setDbhCm(10.0);
        target.setAlturaM(5.0);
        target.setObservaciones("Viejo");

        MedicionRequest req = new MedicionRequest();
        req.setArbolId(2L); // solo cambiar id
        req.setFecha(LocalDate.of(2023, 3, 15));
        req.setDbhCm(12.0);
        req.setAlturaM(6.0);
        req.setObservaciones("Nuevo");

        // Act
        mapper.update(target, req);

        // Assert
        assertEquals(9L, target.getId()); // no cambia
        assertEquals(2L, target.getArbol().getId());
        assertEquals(LocalDate.of(2023, 3, 15), target.getFecha());
        assertEquals(12.0, target.getDbhCm());
        assertEquals(6.0, target.getAlturaM());
        assertEquals("Nuevo", target.getObservaciones());
    }
}
