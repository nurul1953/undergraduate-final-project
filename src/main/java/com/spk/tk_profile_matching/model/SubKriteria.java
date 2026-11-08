package com.spk.tk_profile_matching.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sub_kriteria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubKriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaKriteria; // Contoh: Motorik, Kognitif, Bahasa

    private String namaSubKriteria; // Contoh: Mewarnai, Menghitung

    private Double nilaiTarget; // Nilai standar yang diharapkan (misal: 4)

    @Enumerated(EnumType.STRING)
    private JenisFaktor jenisFaktor; // CORE_FACTOR atau SECONDARY_FACTOR

    public enum JenisFaktor {
        CORE_FACTOR,
        SECONDARY_FACTOR
    }
}
