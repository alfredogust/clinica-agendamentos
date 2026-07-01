package com.clinica.agendamentos.professional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(professionalRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProfessionalNotFoundException.class, () -> professionalService.getById(999L));
}

    @Test
    void shouldReturnProfessionalWhenIdExists() {
        User user = new User("Dra. Ana", "ana@clinica.com", "hashedPassword", Role.PROFESSIONAL);
        Professional professional = new Professional(user, Specialty.GENERAL_SURGERY);

        when(professionalRepository.findById(888L)).thenReturn(Optional.of(professional));

        ProfessionalResponse response = professionalService.getById(888L);

        assertNotNull(response);
        assertEquals(Specialty.GENERAL_SURGERY, response.specialty());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        
        CreateProfessionalRequest request = new CreateProfessionalRequest("Dr. John Doe", "drjohndoe@gmail.com", "plainPassword", Specialty.GENERAL_SURGERY);
        
        when(userRepository.existsByEmail("drjohndoe@gmail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> professionalService.register(request));
    }

    @Test
    void shouldReturnAllProfessionals() {
        User user1 = new User("Dra. Ana", "ana@clinica.com", "hash", Role.PROFESSIONAL);
        Professional professional1 = new Professional(user1, Specialty.GENERAL_SURGERY);

        User user2 = new User("Dr. Joab", "joab@clinica.com", "hash", Role.PROFESSIONAL);
        Professional professional2 = new Professional(user2, Specialty.ANESTHESIOLOGY);

        List<Professional> professionals = List.of(professional1, professional2);

        when(professionalRepository.findAll()).thenReturn(professionals);

        List<ProfessionalResponse> response = professionalService.findAll();

        assertEquals(2, response.size());
        assertEquals("Dra. Ana", response.get(0).user().name());
        assertEquals("Dr. Joab", response.get(1).user().name());
    }

    @Test
    void shouldReturnEmptyListWhenNoProfessionals() {
        when(professionalRepository.findAll()).thenReturn(List.of());

        List<ProfessionalResponse> response = professionalService.findAll();

        assertTrue(response.isEmpty());
    }
}
