package com.clinica.agendamentos.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(
        Instant timestamp,
        int status,
        String message,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {}
}