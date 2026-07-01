package com.clinica.agendamentos.professional;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinica.agendamentos.professional.dto.CreateProfessionalRequest;
import com.clinica.agendamentos.professional.dto.ProfessionalResponse;
import com.clinica.agendamentos.user.EmailAlreadyExistsException;
import com.clinica.agendamentos.user.Role;
import com.clinica.agendamentos.user.User;
import com.clinica.agendamentos.user.UserRepository;
import com.clinica.agendamentos.user.dto.UserResponse;

@Service
public class ProfessionalService {

    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfessionalService(UserRepository userRepository, ProfessionalRepository professionalRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.professionalRepository = professionalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ProfessionalResponse register(CreateProfessionalRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                request.name(),
                request.email(),
                passwordHash,
                Role.PROFESSIONAL);

        User savedUser = userRepository.save(user);

        Professional professional = new Professional(
                savedUser,
                request.specialty());

        Professional savedProfessional = professionalRepository.save(professional);

        return toResponse(savedProfessional);
    }

    public ProfessionalResponse getById(Long id) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException(id));

        return toResponse(professional);
    }

    public List<ProfessionalResponse> findAll() {
        return professionalRepository.findAll()
        .stream()
        .map(professional -> toResponse(professional))
        .toList();
    }

    private ProfessionalResponse toResponse(Professional professional) {
        User user = professional.getUser();
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt());
        return new ProfessionalResponse(
                professional.getId(),
                userResponse,
                professional.getSpecialty(),
                professional.getCreatedAt());
    }
}