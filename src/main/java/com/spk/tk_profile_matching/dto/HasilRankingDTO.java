package com.spk.tk_profile_matching.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HasilRankingDTO implements Comparable<HasilRankingDTO> {

    private Long siswaId;
    private String namaSiswa;
    private Double nilaiCoreFactor;
    private Double nilaiSecondaryFactor;
    private Double nilaiTotal;
    private String statusKelulusan; // Contoh: "LULUS / SIAP" atau "DITUNDA"
    private Integer ranking;

    @Override
    public int compareTo(HasilRankingDTO o) {
        // Urutkan dari nilai tertinggi ke terendah (Descending)
        return Double.compare(o.getNilaiTotal(), this.getNilaiTotal());
    }
}
