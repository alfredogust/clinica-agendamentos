package com.clinica.agendamentos;

import com.clinica.agendamentos.user.EmailAlreadyExistsException;
import com.clinica.agendamentos.user.UserService;
import com.clinica.agendamentos.user.dto.CreateUserRequest;
import com.clinica.agendamentos.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldPersistUserAgainstRealDatabase() {
        CreateUserRequest request =
                new CreateUserRequest("Maria Silva", "maria@email.com", "plainPassword");

        UserResponse response = userService.register(request);

        assertNotNull(response.id());
        assertEquals("maria@email.com", response.email());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists () {
        CreateUserRequest request = 
                new CreateUserRequest("João Pedro", "joao@gmail.com", "plainPassword");

        userService.register(request);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(new CreateUserRequest("Jonas", "joao@gmail.com", "pl4inPassword")));
    }
}