package com.clinica.agendamentos.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void shouldSetNameAndRoleWhenCreatingUser() {
        
        User user = new User("Maria Silva", "maria@email.com", "secretHash", Role.PATIENT);

        assertEquals("Maria Silva", user.getName());
        assertEquals(Role.PATIENT, user.getRole());
    }

    @Test
    void shouldSetEmailAndPasswordHashWhenCreatingUser() {

        User user = new User("Maria Silva", "maria@gmail.com", "secretHash", Role.PATIENT);

        assertEquals("maria@gmail.com", user.getEmail());
        assertEquals("secretHash", user.getPasswordHash());
    }

}