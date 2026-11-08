package com.spk.tk_profile_matching.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ADMIN atau GURU

    @Builder.Default
    private Integer failedAttempt = 0; // Menghitung gagal login

    @Builder.Default
    private Boolean isLocked = false; // Status akun (true = nonaktif/terkunci)

    public enum Role {
        ADMIN,
        GURU
    }
}
