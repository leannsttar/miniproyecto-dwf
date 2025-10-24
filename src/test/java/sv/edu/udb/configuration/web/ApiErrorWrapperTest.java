package sv.edu.udb.configuration.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorWrapperTest {
    @Test
    void addApiError_ok_agregaErrorALista() {
        // Arrange
        ApiErrorWrapper w = new ApiErrorWrapper();
        ApiError error = ApiError.builder()
                .status(404)
                .type("not_found")
                .title("Not Found")
                .source("api")
                .description("no existe")
                .build();

        // Act
        w.addApiError(error);

        // Assert
        assertNotNull(w.getErrors());
        assertEquals(1, w.getErrors().size());
        assertEquals("not_found", w.getErrors().get(0).getType());
    }

    @Test
    void addFieldError_ok_construyeErrorBadRequest() {
        // Arrange
        ApiErrorWrapper w = new ApiErrorWrapper();

        // Act
        w.addFieldError("validation", "Bad Request", "campoX", "mensaje");

        // Assert
        assertEquals(1, w.getErrors().size());
        ApiError e = w.getErrors().get(0);
        assertEquals(HttpStatus.BAD_REQUEST.value(), e.getStatus());
        assertEquals("validation", e.getType());
        assertEquals("Bad Request", e.getTitle());
        assertEquals("campoX", e.getSource());
        assertEquals("mensaje", e.getDescription());
    }
}
