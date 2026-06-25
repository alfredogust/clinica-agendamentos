package com.clinica.agendamentos.shared.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.clinica.agendamentos.professional.ProfessionalNotFoundException;
import com.clinica.agendamentos.user.EmailAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return conflict(ex.getMessage());
    }

    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<ApiError> handleProfessionalNotFound(ProfessionalNotFoundException ex) {
        return notFound(ex.getMessage());
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

    private ResponseEntity<ApiError> notFound(String message) {
        ApiError error = new ApiError(Instant.now(), HttpStatus.NOT_FOUND.value(), message, List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}