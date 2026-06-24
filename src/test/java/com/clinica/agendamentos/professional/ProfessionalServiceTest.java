package com.clinica.agendamentos.professional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.clinica.agendamentos.professional.dto.CreateProfessionalRequest;
import com.clinica.agendamentos.professional.dto.ProfessionalResponse;
import com.clinica.agendamentos.user.EmailAlreadyExistsException;
import com.clinica.agendamentos.user.Role;
import com.clinica.agendamentos.user.User;
import com.clinica.agendamentos.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class ProfessionalServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfessionalService professionalService;

    @Test
    void shouldRegisterProfessionalWhenEmailIsNew() {
        // Implement test logic for registering a professional
        CreateProfessionalRequest request = new CreateProfessionalRequest("Dr. John Doe", "drjohndoe@gmail.com", "plainPassword", Specialty.GENERAL_SURGERY);
        User savedUser = new User("Dr. John Doe", "drjohndoe@gmail.com", "hashedPassword", Role.PROFESSIONAL);

        Professional savedProfessional = new Professional(savedUser, Specialty.GENERAL_SURGERY);

        when(userRepository.existsByEmail("drjohndoe@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(professionalRepository.save(any(Professional.class))).thenReturn(savedProfessional);

        ProfessionalResponse response = professionalService.register(request);

        assertEquals("Dr. John Doe", response.user().name());
        assertEquals(Specialty.GENERAL_SURGERY, response.specialty());
        assertEquals(Role.PROFESSIONAL, response.user().role());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        
        CreateProfessionalRequest request = new CreateProfessionalRequest("Dr. John Doe", "drjohndoe@gmail.com", "plainPassword", Specialty.GENERAL_SURGERY);
        
        when(userRepository.existsByEmail("drjohndoe@gmail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> professionalService.register(request));
    }
}
