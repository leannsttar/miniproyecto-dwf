package sv.edu.udb.configuration.web;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTest {
    @Test
    void builder_ok_seteaCamposYValoresPorDefecto() {
        // Arrange + Act
        ApiError e = ApiError.builder()
                .status(400)
                .type("validation")
                .title("Bad Request")
                .description("faltan campos")
                // no seteamos source para comprobar el @Builder.Default
                .build();

        // Assert
        assertEquals(400, e.getStatus());
        assertEquals("validation", e.getType());
        assertEquals("Bad Request", e.getTitle());
        assertEquals("faltan campos", e.getDescription());
        assertEquals("api", e.getSource()); // por defecto
    }

    @Test
    void setters_ok_permitenActualizarValores() {
        // Arrange
        ApiError e = ApiError.builder().build();

        // Act
        e.setStatus(500);
        e.setType("error");
        e.setTitle("Internal Error");
        e.setSource("controller");
        e.setDescription("boom");

        // Assert
        assertEquals(500, e.getStatus());
        assertEquals("error", e.getType());
        assertEquals("Internal Error", e.getTitle());
        assertEquals("controller", e.getSource());
        assertEquals("boom", e.getDescription());
    }
}
