package com.clinica.agendamentos.appointment;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinica.agendamentos.professional.Professional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByProfessionalAndStartTime(Professional professional, OffsetDateTime startTime);
}