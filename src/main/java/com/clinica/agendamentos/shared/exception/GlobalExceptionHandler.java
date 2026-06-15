package com.clinica.agendamentos.shared.exception;

import com.clinica.agendamentos.user.EmailAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return conflict(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
        return conflict("The request conflicts with existing data.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiError.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        ApiError error = new ApiError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "Validation failed", fieldErrors);
        return ResponseEntity.badRequest().body(error);
    }

    private ResponseEntity<ApiError> conflict(String message) {
        ApiError error = new ApiError(Instant.now(), HttpStatus.CONFLICT.value(), message, List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}