package com.spk.tk_profile_matching.controller;

import com.spk.tk_profile_matching.model.Siswa;
import com.spk.tk_profile_matching.repository.SiswaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/siswa")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SiswaController {

    private final SiswaRepository siswaRepository;

    // 1. Get All
    @GetMapping
    public List<Siswa> getAllSiswa() {
        return siswaRepository.findAll();
    }

    // 2. Create / Add
    @PostMapping
    public Siswa createSiswa(@RequestBody Siswa siswa) {
        return siswaRepository.save(siswa);
    }

    // 3. Update / Edit
    @PutMapping("/{id}")
    public ResponseEntity<Siswa> updateSiswa(@PathVariable Long id, @RequestBody Siswa updateData) {
        return siswaRepository.findById(id)
                .map(siswa -> {
                    siswa.setNama(updateData.getNama());
                    siswa.setNisn(updateData.getNisn());
                    return ResponseEntity.ok(siswaRepository.save(siswa));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSiswa(@PathVariable Long id) {
        if (siswaRepository.existsById(id)) {
            siswaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
