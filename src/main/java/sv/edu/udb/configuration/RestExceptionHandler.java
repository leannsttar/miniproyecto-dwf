//package sv.edu.udb.configuration;
//
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.validation.ValidationException;
//import org.springframework.dao.DataAccessException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.lang.Nullable;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//import org.springframework.web.util.WebUtils;
//import sv.edu.udb.configuration.web.ApiError;
//import sv.edu.udb.configuration.web.ApiErrorWrapper;
//
//import java.nio.file.AccessDeniedException;
//import java.util.List;
//import java.util.Objects;
//
//@ControllerAdvice
//public class RestExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            final MethodArgumentNotValidException ex, final HttpHeaders headers,
//            final HttpStatusCode status, final WebRequest request) {
//        final ApiErrorWrapper apiErrorWrapper = processErrors(ex.getBindingResult().getAllErrors());
//        return handleExceptionInternal(ex, apiErrorWrapper, headers, HttpStatus.BAD_REQUEST, request);
//    }
//
//    @ExceptionHandler({ValidationException.class})
//    protected ResponseEntity<Object> handleValidation(final ValidationException ex, final WebRequest request) {
//        final ApiErrorWrapper api = message(HttpStatus.BAD_REQUEST, ex);
//        return handleExceptionInternal(ex, api, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//    }
//
//    @ExceptionHandler({EntityNotFoundException.class})
//    protected ResponseEntity<Object> handleNotFound(final EntityNotFoundException ex, final WebRequest request) {
//        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
//    }
//
//    @ExceptionHandler({DataAccessException.class})
//    protected ResponseEntity<Object> handleDataAccess(final DataAccessException ex, final WebRequest request) {
//        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }
//
//    @ExceptionHandler({IllegalArgumentException.class})
//    protected ResponseEntity<Object> handleBadRequest(final IllegalArgumentException ex, final WebRequest request) {
//        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//    }
//
//    @ExceptionHandler({AccessDeniedException.class})
//    protected ResponseEntity<Object> handleForbidden(final AccessDeniedException ex, final WebRequest request) {
//        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
//    }
//
//    @ExceptionHandler({Exception.class})
//    protected ResponseEntity<Object> handle500(final Exception ex, final WebRequest request) {
//        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
//    }
//
//    protected ApiErrorWrapper processErrors(final List<ObjectError> errors) {
//        final ApiErrorWrapper dto = new ApiErrorWrapper();
//        errors.forEach(objError -> {
//            if (objError instanceof FieldError field) {
//                dto.addFieldError("validation", "Bad Request", field.getField(), field.getDefaultMessage());
//            } else {
//                dto.addFieldError("validation", "Bad Request", "unknown", objError.getDefaultMessage());
//            }
//        });
//        return dto;
//    }
//
//    protected ApiError buildApiError(final HttpStatus status, final Exception ex) {
//        return ApiError.builder()
//                .status(status.value())
//                .title(status.getReasonPhrase())
//                .type(ex.getClass().getSimpleName())
//                .description(ex.getMessage())
//                .build();
//    }
//
//    protected ApiErrorWrapper message(final HttpStatus status, final Exception ex) {
//        final ApiErrorWrapper w = new ApiErrorWrapper();
//        w.addApiError(buildApiError(status, ex));
//        return w;
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body,
//                                                             HttpHeaders headers,
//                                                             HttpStatusCode status, WebRequest request) {
//        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
//            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
//        }
//        if (Objects.isNull(body)) {
//            final ApiErrorWrapper api = message((HttpStatus) status, ex);
//            return new ResponseEntity<>(api, headers, status);
//        }
//        return new ResponseEntity<>(body, headers, status);
//    }
//}
package sv.edu.udb.configuration;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;
import sv.edu.udb.configuration.web.ApiError;
import sv.edu.udb.configuration.web.ApiErrorWrapper;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, final HttpHeaders headers,
            final HttpStatusCode status, final WebRequest request) {
        final ApiErrorWrapper apiErrorWrapper = processErrors(ex.getBindingResult().getAllErrors());
        return handleExceptionInternal(ex, apiErrorWrapper, headers, HttpStatus.BAD_REQUEST, request);
    }

    // -------------------------------
    // Helper para evitar repetición
    // -------------------------------
    private ResponseEntity<Object> buildResponse(Exception ex, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, message(status, ex), new HttpHeaders(), status, request);
    }

    // -------------------------------
    // Exception handlers
    // -------------------------------
    @ExceptionHandler({ValidationException.class})
    protected ResponseEntity<Object> handleValidation(final ValidationException ex, final WebRequest request) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(final EntityNotFoundException ex, final WebRequest request) {
        return buildResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({DataAccessException.class})
    protected ResponseEntity<Object> handleDataAccess(final DataAccessException ex, final WebRequest request) {
        return buildResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequest(final IllegalArgumentException ex, final WebRequest request) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleForbidden(final AccessDeniedException ex, final WebRequest request) {
        return buildResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handle500(final Exception ex, final WebRequest request) {
        return buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // -------------------------------
    // Métodos auxiliares
    // -------------------------------
    protected ApiErrorWrapper processErrors(final List<ObjectError> errors) {
        final ApiErrorWrapper dto = new ApiErrorWrapper();
        errors.forEach(objError -> {
            if (objError instanceof FieldError field) {
                dto.addFieldError("validation", "Bad Request", field.getField(), field.getDefaultMessage());
            } else {
                dto.addFieldError("validation", "Bad Request", "unknown", objError.getDefaultMessage());
            }
        });
        return dto;
    }

    protected ApiError buildApiError(final HttpStatus status, final Exception ex) {
        return ApiError.builder()
                .status(status.value())
                .title(status.getReasonPhrase())
                .type(ex.getClass().getSimpleName())
                .description(ex.getMessage())
                .build();
    }

    protected ApiErrorWrapper message(final HttpStatus status, final Exception ex) {
        final ApiErrorWrapper w = new ApiErrorWrapper();
        w.addApiError(buildApiError(status, ex));
        return w;
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        if (Objects.isNull(body)) {
            final ApiErrorWrapper api = message((HttpStatus) status, ex);
            return new ResponseEntity<>(api, headers, status);
        }
        return new ResponseEntity<>(body, headers, status);
    }
}
