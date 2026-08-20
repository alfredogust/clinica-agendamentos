package com.clinica.agendamentos.appointment;

import java.time.OffsetDateTime;

public class AppointmentConflictException extends RuntimeException {
    
    public AppointmentConflictException(OffsetDateTime startTime) {
        super("Professional already has an appointment at: " + startTime);
    }
}
