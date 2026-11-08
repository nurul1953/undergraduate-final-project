package com.spk.tk_profile_matching.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "penilaian")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Penilaian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "siswa_id")
    private Siswa siswa;

    @ManyToOne
    @JoinColumn(name = "sub_kriteria_id")
    private SubKriteria subKriteria;

    private Double nilai; // Nilai riil siswa (1 - 5)
}
