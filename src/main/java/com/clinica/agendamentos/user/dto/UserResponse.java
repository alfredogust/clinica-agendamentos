package com.clinica.agendamentos.user.dto;

import com.clinica.agendamentos.user.Role;
import java.time.OffsetDateTime;

public record UserResponse(
    Long id,
    String name,
    String email,
    Role role,
    OffsetDateTime createdAt
) {}
