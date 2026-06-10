package com.clinica.agendamentos.dto;

import com.clinica.agendamentos.user.Role;
import java.time.OffsetDateTime;

public record UserResponse(
    Long id,
    String name,
    String email,
    Role role,
    OffsetDateTime createdAt
) {}
