package com.clinica.agendamentos.professional;

import com.clinica.agendamentos.user.User;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "professionals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialty specialty;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public Professional(User user, Specialty specialty) {
        this.user = user;
        this.specialty = specialty;
    }
}
