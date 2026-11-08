package com.spk.tk_profile_matching.repository;

import com.spk.tk_profile_matching.model.BobotGap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BobotGapRepository extends JpaRepository<BobotGap, Long> {
    Optional<BobotGap> findByGap(Double gap);
}
