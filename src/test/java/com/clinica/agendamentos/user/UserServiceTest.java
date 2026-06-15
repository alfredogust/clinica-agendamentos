package com.clinica.agendamentos.user;

import com.clinica.agendamentos.user.dto.CreateUserRequest;
import com.clinica.agendamentos.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserWhenEmailIsNew() {
        CreateUserRequest request = new CreateUserRequest("Maria Silva", "maria@gmail.com", "plainPassword");
        User savedUser = new User("Maria Silva", "maria@gmail.com", "hashedPassword", Role.PATIENT);

        when(userRepository.existsByEmail("maria@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("hashedPassword", capturedUser.getPasswordHash());
        assertNotEquals("plainPassword", capturedUser.getPasswordHash());

        assertEquals("Maria Silva", response.name());
        assertEquals("maria@gmail.com", response.email());
        assertEquals(Role.PATIENT, response.role());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        
        CreateUserRequest request = new CreateUserRequest("Maria Silva", "maria@gmail.com", "plainPassword");

        when(userRepository.existsByEmail("maria@gmail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(request));
    }
}