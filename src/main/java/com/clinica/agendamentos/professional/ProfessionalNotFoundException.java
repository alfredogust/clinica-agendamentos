package com.clinica.agendamentos.professional;

public class ProfessionalNotFoundException extends RuntimeException {
    
    public ProfessionalNotFoundException(Long id) {
        super("Professional not found with id: " + id);
    }
}
