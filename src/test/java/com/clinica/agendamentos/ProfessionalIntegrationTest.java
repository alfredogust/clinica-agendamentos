package com.clinica.agendamentos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.clinica.agendamentos.professional.ProfessionalNotFoundException;
import com.clinica.agendamentos.professional.ProfessionalService;
import com.clinica.agendamentos.professional.Specialty;
import com.clinica.agendamentos.professional.dto.CreateProfessionalRequest;
import com.clinica.agendamentos.professional.dto.ProfessionalResponse;
import com.clinica.agendamentos.user.EmailAlreadyExistsException;
import com.clinica.agendamentos.user.Role;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class ProfessionalIntegrationTest {

    @Autowired
    private ProfessionalService professionalService;

    @Test
    void shouldRegisterProfessionalWhenEmailIsNew() {
        CreateProfessionalRequest request = new CreateProfessionalRequest("Maria Silva", "maria@gmail.com",
                "plainPassword", Specialty.GENERAL_SURGERY);

        ProfessionalResponse response = professionalService.register(request);

        assertNotNull(response.id());
        assertEquals("Maria Silva", response.user().name());
        assertEquals("maria@gmail.com", response.user().email());
        assertEquals(Role.PROFESSIONAL, response.user().role());
        assertEquals(Specialty.GENERAL_SURGERY, response.specialty());

    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        CreateProfessionalRequest request = new CreateProfessionalRequest("Carlos Guimas", "carlostis@gmail.com",
                "plainPassword", Specialty.GENERAL_SURGERY);

        professionalService.register(request);

        CreateProfessionalRequest secondRequest = new CreateProfessionalRequest("Carlos Guimas", "carlostis@gmail.com",
                "plainPassword", Specialty.GENERAL_SURGERY);

        assertThrows(EmailAlreadyExistsException.class, () -> professionalService.register(secondRequest));
    }

    @Test
    void shouldReturnProfessionalWhenIdExists() {
        CreateProfessionalRequest request = new CreateProfessionalRequest("Carlos Guimas", "carlostis@gmail.com",
                "plainPassword", Specialty.GENERAL_SURGERY);

        ProfessionalResponse created = professionalService.register(request);

        ProfessionalResponse found = professionalService.getById(created.id());

        assertEquals(created.id(), found.id());
        assertEquals("Carlos Guimas", found.user().name());
        assertEquals(Specialty.GENERAL_SURGERY, found.specialty());
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        assertThrows(ProfessionalNotFoundException.class, () -> professionalService.getById(999L));
    }

    @Test
    void shouldReturnAllProfessionals() {
        CreateProfessionalRequest request1 = new CreateProfessionalRequest("Carlos Guimas", "carlostis@gmail.com",
                "plainPassword", Specialty.GENERAL_SURGERY);

        CreateProfessionalRequest request2 = new CreateProfessionalRequest("Miguel Braz", "brazmuig@gmail.com",
                "plainPassword", Specialty.OBSTETRICS_AND_GYNECOLOGY);

        professionalService.register(request1);
        professionalService.register(request2);

        List<ProfessionalResponse> response = professionalService.findAll();

        assertEquals(2, response.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoProfessionals() {
        List<ProfessionalResponse> response = professionalService.findAll();

        assertTrue(response.isEmpty());
    }
}
