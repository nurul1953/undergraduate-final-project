package com.spk.tk_profile_matching.controller;

import com.spk.tk_profile_matching.dto.HasilRankingDTO;
import com.spk.tk_profile_matching.dto.PenilaianRequestDTO;
import com.spk.tk_profile_matching.model.Penilaian;
import com.spk.tk_profile_matching.repository.PenilaianRepository;
import com.spk.tk_profile_matching.service.ProfileMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spk")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Menizinkan akses dari Frontend (React/Vue/Angular)
public class SpkController {

    private final ProfileMatchingService profileMatchingService;
    private final PenilaianRepository penilaianRepository;

    // Get All Penilaian
    @GetMapping("/penilaian")
    public List<Penilaian> getAllPenilaian() {
        return penilaianRepository.findAll();
    }

    // Create / Update Penilaian
    @PostMapping("/penilaian")
    public ResponseEntity<Penilaian> inputNilai(@RequestBody PenilaianRequestDTO request) {
        Penilaian result = profileMatchingService.inputNilai(request);
        return ResponseEntity.ok(result);
    }

    // Delete Penilaian
    @DeleteMapping("/penilaian/{id}")
    public ResponseEntity<Void> deletePenilaian(@PathVariable Long id) {
        if (penilaianRepository.existsById(id)) {
            penilaianRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint 2: Proses Kalkulasi SPK & Dapatkan Hasil Ranking
    @GetMapping("/proses-hitung")
    public ResponseEntity<List<HasilRankingDTO>> hitungSPK() {
        List<HasilRankingDTO> hasil = profileMatchingService.hitungSPK();
        return ResponseEntity.ok(hasil);
    }
}
