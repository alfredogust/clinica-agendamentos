package com.clinica.agendamentos.appointment;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.clinica.agendamentos.professional.Professional;
import com.clinica.agendamentos.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    public Appointment (Professional professional, User patient, OffsetDateTime startTime) {
        this.professional = professional;
        this.patient = patient;
        this.startTime = startTime;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
    }
}
