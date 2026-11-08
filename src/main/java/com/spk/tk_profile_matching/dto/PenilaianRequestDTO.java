package com.spk.tk_profile_matching.dto;

import lombok.Data;

@Data
public class PenilaianRequestDTO {
    private Long siswaId;
    private Long subKriteriaId;
    private Double nilai;
}
