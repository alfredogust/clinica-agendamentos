package com.clinica.agendamentos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.clinica.agendamentos.appointment.AppointmentConflictException;
import com.clinica.agendamentos.appointment.AppointmentService;
import com.clinica.agendamentos.appointment.dto.AppointmentResponse;
import com.clinica.agendamentos.appointment.dto.CreateAppointmentRequest;
import com.clinica.agendamentos.professional.ProfessionalService;
import com.clinica.agendamentos.professional.Specialty;
import com.clinica.agendamentos.professional.dto.CreateProfessionalRequest;
import com.clinica.agendamentos.professional.dto.ProfessionalResponse;
import com.clinica.agendamentos.user.UserService;
import com.clinica.agendamentos.user.dto.CreateUserRequest;
import com.clinica.agendamentos.user.dto.UserResponse;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class AppointmentIntegrationTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ProfessionalService professionalService;

    @Autowired
    private UserService userService;

    @Test
    void shouldRegisterAppointmentWhenValid() {

        CreateProfessionalRequest professionalRequest = new CreateProfessionalRequest("Maria Silva", "maria@gmail.com",
                "plainPassword", Specialty.GENERAL_SURGERY);

        ProfessionalResponse professional = professionalService.register(professionalRequest);

        CreateUserRequest patientRequest = new CreateUserRequest("Carlos Lotti", "carloslotti@gmail.com",
                "plainPassword");

        UserResponse patient = userService.register(patientRequest);

        CreateAppointmentRequest appointmentRequest = new CreateAppointmentRequest(
                professional.id(), patient.id(), OffsetDateTime.parse("2026-08-15T14:00:00Z"));

        AppointmentResponse response = appointmentService.register(appointmentRequest);

        assertNotNull(response.id());
        assertEquals("Carlos Lotti", response.patient().name());
        assertEquals("Maria Silva", response.professional().user().name());
    }

    @Test
    void shouldThrowWhenTimeConflict() {
        CreateProfessionalRequest professionalRequest = new CreateProfessionalRequest("Maria Silva", "maria@gmail.com",
                "plainPassword", Specialty.GENERAL_SURGERY);

        ProfessionalResponse professional = professionalService.register(professionalRequest);

        CreateUserRequest patientRequest = new CreateUserRequest("Carlos Lotti", "carloslotti@gmail.com",
                "plainPassword");

        UserResponse patient = userService.register(patientRequest);

        CreateAppointmentRequest firstRequest = new CreateAppointmentRequest(professional.id(), patient.id(),
                OffsetDateTime.parse("2026-08-15T14:00:00Z"));

        appointmentService.register(firstRequest);

        CreateAppointmentRequest secondRequest = new CreateAppointmentRequest(professional.id(), patient.id(),
                OffsetDateTime.parse("2026-08-15T14:00:00Z"));

        assertThrows(AppointmentConflictException.class, () -> appointmentService.register(secondRequest));
    }
}
