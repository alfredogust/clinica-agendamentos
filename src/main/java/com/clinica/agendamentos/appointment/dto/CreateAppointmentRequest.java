package com.clinica.agendamentos.appointment.dto;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequest (
    @NotNull
    Long professionalId,

    @NotNull
    Long patientId,

    @NotNull
    OffsetDateTime startTime
) {}
