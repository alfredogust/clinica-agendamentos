package com.clinica.agendamentos.professional.dto;

import com.clinica.agendamentos.professional.Specialty;
import com.clinica.agendamentos.user.dto.UserResponse;
import java.time.OffsetDateTime;

public record ProfessionalResponse (
   Long id,
   UserResponse user,
   Specialty specialty,
   OffsetDateTime createdAt
) {}
