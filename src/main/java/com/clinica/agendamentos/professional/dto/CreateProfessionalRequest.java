package com.clinica.agendamentos.professional.dto;

import com.clinica.agendamentos.professional.Specialty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProfessionalRequest(
    
    @NotBlank
    @Size(max = 150)
    String name,
    
    @NotBlank
    @Email
    @Size(max = 255)
    String email,

    @NotBlank
    @Size(min = 8, max = 72)
    String password,

    @NotNull
    Specialty specialty
) {}