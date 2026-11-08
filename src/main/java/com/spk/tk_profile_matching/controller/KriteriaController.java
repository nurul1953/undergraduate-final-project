package com.spk.tk_profile_matching.controller;

import com.spk.tk_profile_matching.model.SubKriteria;
import com.spk.tk_profile_matching.repository.SubKriteriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kriteria")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KriteriaController {

    private final SubKriteriaRepository subKriteriaRepository;

    // 1. Get All
    @GetMapping
    public List<SubKriteria> getAllKriteria() {
        return subKriteriaRepository.findAll();
    }

    // 2. Create / Add
    @PostMapping
    public SubKriteria createKriteria(@RequestBody SubKriteria kriteria) {
        return subKriteriaRepository.save(kriteria);
    }

    // 3. Update / Edit
    @PutMapping("/{id}")
    public ResponseEntity<SubKriteria> updateKriteria(@PathVariable Long id, @RequestBody SubKriteria updateData) {
        return subKriteriaRepository.findById(id)
                .map(k -> {
                    k.setNamaKriteria(updateData.getNamaKriteria());
                    k.setNamaSubKriteria(updateData.getNamaSubKriteria());
                    k.setNilaiTarget(updateData.getNilaiTarget());
                    k.setJenisFaktor(updateData.getJenisFaktor());
                    return ResponseEntity.ok(subKriteriaRepository.save(k));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKriteria(@PathVariable Long id) {
        if (subKriteriaRepository.existsById(id)) {
            subKriteriaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}