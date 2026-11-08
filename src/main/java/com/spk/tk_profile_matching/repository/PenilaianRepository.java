package com.spk.tk_profile_matching.repository;

import com.spk.tk_profile_matching.model.Penilaian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PenilaianRepository extends JpaRepository<Penilaian, Long> {
    List<Penilaian> findBySiswaId(Long siswaId);

    // Dipakai untuk mengecek apakah nilai sudah ada sebelum disimpan (Update vs Insert)
    Optional<Penilaian> findBySiswaIdAndSubKriteriaId(Long siswaId, Long subKriteriaId);

}

