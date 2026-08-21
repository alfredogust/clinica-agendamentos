package com.clinica.agendamentos.appointment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinica.agendamentos.appointment.dto.AppointmentResponse;
import com.clinica.agendamentos.appointment.dto.CreateAppointmentRequest;
import com.clinica.agendamentos.professional.Professional;
import com.clinica.agendamentos.professional.ProfessionalNotFoundException;
import com.clinica.agendamentos.professional.ProfessionalRepository;
import com.clinica.agendamentos.professional.dto.ProfessionalResponse;
import com.clinica.agendamentos.user.User;
import com.clinica.agendamentos.user.UserNotFoundException;
import com.clinica.agendamentos.user.UserRepository;
import com.clinica.agendamentos.user.dto.UserResponse;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ProfessionalRepository professionalRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
            ProfessionalRepository professionalRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.professionalRepository = professionalRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AppointmentResponse register(CreateAppointmentRequest request) {

        Professional professional = professionalRepository.findById(request.professionalId())
                .orElseThrow(() -> new ProfessionalNotFoundException(request.professionalId()));

        User patient = userRepository.findById(request.patientId())
                .orElseThrow(() -> new UserNotFoundException(request.patientId()));

        if (appointmentRepository.existsByProfessionalAndStartTime(professional, request.startTime())) {
            throw new AppointmentConflictException(request.startTime());
        }

        Appointment appointment = new Appointment(professional, patient, request.startTime());
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return toResponse(savedAppointment);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        Professional professional = appointment.getProfessional();
        User patient = appointment.getPatient();

        ProfessionalResponse professionalResponse = new ProfessionalResponse(
                professional.getId(),
                new UserResponse(
                        professional.getUser().getId(),
                        professional.getUser().getName(),
                        professional.getUser().getEmail(),
                        professional.getUser().getRole(),
                        professional.getUser().getCreatedAt()),
                professional.getSpecialty(),
                professional.getCreatedAt());

        UserResponse patientResponse = new UserResponse(
                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                patient.getRole(),
                patient.getCreatedAt());

        return new AppointmentResponse(
                appointment.getId(),
                professionalResponse,
                patientResponse,
                appointment.getStartTime(),
                appointment.getCreatedAt(),
                appointment.getStatus());
    }

    @Transactional
    public AppointmentResponse cancel(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new AppointmentAlreadyCancelledException(id);
        }

        appointment.cancel();
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return toResponse(savedAppointment);
    }
}
