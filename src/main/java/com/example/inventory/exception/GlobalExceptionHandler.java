package com.example.inventory.exception;

import com.example.inventory.dto.ApiErrorDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(NotFoundException ex, ServletWebRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    // 400
    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiErrorDTO> handleBadRequest(Throwable ex, ServletWebRequest req) {
        if (ex instanceof MethodArgumentNotValidException manve) {
            List<String> details = manve.getBindingResult().getFieldErrors()
                    .stream().map(this::formatFieldError).collect(Collectors.toList());
            return build(HttpStatus.BAD_REQUEST, "Errores de validación", req, details);
        }
        if (ex instanceof ConstraintViolationException cve) {
            List<String> details = cve.getConstraintViolations()
                    .stream().map(this::formatConstraintViolation).collect(Collectors.toList());
            return build(HttpStatus.BAD_REQUEST, "Parámetros inválidos", req, details);
        }
        // IllegalArgument, NotReadable, MissingParam
        return build(HttpStatus.BAD_REQUEST, ex.getMessage() != null ? ex.getMessage()
                : "Solicitud inválida", req, null);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleUnexpected(Exception ex, ServletWebRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", req, List.of(ex.getMessage()));
    }

    // Helpers
    private ResponseEntity<ApiErrorDTO> build(HttpStatus status, String message, ServletWebRequest req, List<String> details) {
        String path = req.getRequest().getRequestURI();
        ApiErrorDTO body = new ApiErrorDTO(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                details
        );
        return ResponseEntity.status(status).body(body);
    }

    private String formatFieldError(FieldError fe) {
        return "%s: %s (rechazado: %s)".formatted(fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue());
    }

    private String formatConstraintViolation(ConstraintViolation<?> cv) {
        return "%s: %s (valor: %s)".formatted(cv.getPropertyPath(), cv.getMessage(), cv.getInvalidValue());
    }
}
