package com.clinica.agendamentos.appointment.dto;

import java.time.OffsetDateTime;

import com.clinica.agendamentos.professional.dto.ProfessionalResponse;
import com.clinica.agendamentos.user.dto.UserResponse;

public record AppointmentResponse (
    Long id,
    ProfessionalResponse professional,
    UserResponse patient,
    OffsetDateTime startTime,
    OffsetDateTime createdAt
) {}
