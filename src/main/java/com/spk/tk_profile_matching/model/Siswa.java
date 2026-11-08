package com.spk.tk_profile_matching.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "siswa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Siswa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private String nisn;

    @Column(name = "tahun_ajaran", nullable = false)
    private String tahunAjaran; // Contoh: "2025/2026"
}