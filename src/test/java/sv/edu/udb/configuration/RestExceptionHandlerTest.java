package sv.edu.udb.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;
import sv.edu.udb.configuration.RestExceptionHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Objects;

class RestExceptionHandlerTest {
    /** Subclase para exponer el método protegido handleExceptionInternal a la prueba */
    static class ExposedHandler extends RestExceptionHandler {
        @Override
        protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body,
                                                                 HttpHeaders headers, HttpStatusCode status, WebRequest request) {
            return super.handleExceptionInternal(ex, body, headers, status, request);
        }
    }

    @Test
    void handleExceptionInternal_sinBody_envuelveEnApiErrorWrapper() {
        // Arrange
        var handler = new ExposedHandler();
        var ex = new RuntimeException("boom");
        var headers = new HttpHeaders();
        var status = HttpStatus.BAD_REQUEST;
        var request = mock(WebRequest.class);

        // Act
        ResponseEntity<Object> resp = handler.handleExceptionInternal(ex, null, headers, status, request);

        // Assert
        assertNotNull(resp);
        assertEquals(status, resp.getStatusCode());
        assertNotNull(resp.getBody()); // debería construir ApiErrorWrapper internamente
    }

    @Test
    void handleExceptionInternal_conBody_loRespeta() {
        // Arrange
        var handler = new ExposedHandler();
        var ex = new RuntimeException("boom");
        var headers = new HttpHeaders();
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var request = mock(WebRequest.class);
        var body = new Object();

        // Act
        ResponseEntity<Object> resp = handler.handleExceptionInternal(ex, body, headers, status, request);

        // Assert
        assertNotNull(resp);
        assertEquals(status, resp.getStatusCode());
        assertSame(body, resp.getBody());
    }
}
