package com.clinica.agendamentos.professional;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.clinica.agendamentos.professional.dto.CreateProfessionalRequest;
import com.clinica.agendamentos.professional.dto.ProfessionalResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/professionals")
public class ProfessionalController {
    
    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @PostMapping
    public ResponseEntity<ProfessionalResponse> register(@Valid @RequestBody CreateProfessionalRequest request) {
        ProfessionalResponse response = professionalService.register(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalResponse> getById(@PathVariable Long id) {
        ProfessionalResponse response = professionalService.getById(id);

        return ResponseEntity.ok(response);
    }
}
