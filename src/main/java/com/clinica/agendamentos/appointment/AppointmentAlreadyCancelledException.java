package com.clinica.agendamentos.appointment;

public class AppointmentAlreadyCancelledException extends RuntimeException {
    
    public AppointmentAlreadyCancelledException(Long id) {
        super("Appointment is already cancelled: " + id);
    }
}
