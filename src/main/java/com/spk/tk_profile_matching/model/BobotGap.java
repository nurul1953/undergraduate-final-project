package com.spk.tk_profile_matching.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bobot_gap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BobotGap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double gap;        // Selisih (Nilai Siswa - Nilai Target)

    private Double bobot;      // Nilai Bobot Konversi (misal GAP 0 -> Bobot 5)

    private String keterangan; // Contoh: "Tidak ada selisih"
}
