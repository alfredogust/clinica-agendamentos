package com.clinica.agendamentos.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.clinica.agendamentos.appointment.dto.AppointmentResponse;
import com.clinica.agendamentos.appointment.dto.CreateAppointmentRequest;
import com.clinica.agendamentos.professional.Professional;
import com.clinica.agendamentos.professional.ProfessionalNotFoundException;
import com.clinica.agendamentos.professional.ProfessionalRepository;
import com.clinica.agendamentos.professional.Specialty;
import com.clinica.agendamentos.user.Role;
import com.clinica.agendamentos.user.User;
import com.clinica.agendamentos.user.UserNotFoundException;
import com.clinica.agendamentos.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldRegisterAppointmentWhenValid() {

        CreateAppointmentRequest request = new CreateAppointmentRequest(
                1L,
                2L,
                OffsetDateTime.parse("2026-08-15T14:00:00Z"));

        User professionalUser = new User("Dra. Ana", "ana@clinica.com", "hash", Role.PROFESSIONAL);
        Professional professional = new Professional(professionalUser, Specialty.GENERAL_SURGERY);

        User patient = new User("Carlos", "carlos@paciente.com", "hash", Role.PATIENT);

        Appointment savedAppointment = new Appointment(professional, patient, request.startTime());

        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(userRepository.findById(2L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.existsByProfessionalAndStartTime(professional, request.startTime()))
                .thenReturn(false);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        AppointmentResponse response = appointmentService.register(request);

        assertNotNull(response);
        assertEquals("Dra. Ana", response.professional().user().name());
        assertEquals("Carlos", response.patient().name());
    }

    @Test
    void shouldThrowWhenProfessionalNotFound() {

        CreateAppointmentRequest request = new CreateAppointmentRequest(
            1L,
            2L,
            OffsetDateTime.parse("2026-08-15T14:00:00Z"));

        when(professionalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProfessionalNotFoundException.class,
                () -> appointmentService.register(request));
    }

    @Test
    void shouldThrowWhenPatientNotFound() {

        CreateAppointmentRequest request = new CreateAppointmentRequest(
            1L,
            2L,
            OffsetDateTime.parse("2026-08-15T14:00:00Z"));

        User professionalUser = new User("Dra. Ana", "ana@clinica.com", "hash", Role.PROFESSIONAL);
        Professional professional = new Professional(professionalUser, Specialty.GENERAL_SURGERY);

        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> appointmentService.register(request));
    }

    @Test
    void shouldThrowWhenTimeConflict() {

        CreateAppointmentRequest request = new CreateAppointmentRequest(
            1L,
            2L,
            OffsetDateTime.parse("2026-08-15T14:00:00Z"));

        User professionalUser = new User("Dra. Ana", "ana@clinica.com", "hash", Role.PROFESSIONAL);
        Professional professional = new Professional(professionalUser, Specialty.GENERAL_SURGERY);
        User patient = new User("Carlos", "carlos@paciente.com", "hash", Role.PATIENT);

        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(userRepository.findById(2L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.existsByProfessionalAndStartTime(professional, request.startTime()))
                .thenReturn(true);

        assertThrows(AppointmentConflictException.class,
             () -> appointmentService.register(request));
    }
}
