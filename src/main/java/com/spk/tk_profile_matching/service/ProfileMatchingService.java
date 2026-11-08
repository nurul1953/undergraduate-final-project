package com.spk.tk_profile_matching.service;

import com.spk.tk_profile_matching.dto.HasilRankingDTO;
import com.spk.tk_profile_matching.dto.PenilaianRequestDTO;
import com.spk.tk_profile_matching.model.*;
import com.spk.tk_profile_matching.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileMatchingService {

    private final SiswaRepository siswaRepository;
    private final SubKriteriaRepository subKriteriaRepository;
    private final PenilaianRepository penilaianRepository;
    private final BobotGapRepository bobotGapRepository;

    // Bobot Persentase (misal: 60% Core Factor, 40% Secondary Factor)
    private static final double BOBOT_CORE = 0.60;
    private static final double BOBOT_SECONDARY = 0.40;
    private static final double CUT_OFF_LULUS = 4.0; // Minimal nilai akhir untuk lulus

    // 1. Simpan/Input Penilaian
    public Penilaian inputNilai(PenilaianRequestDTO dto) {
        Siswa siswa = siswaRepository.findById(dto.getSiswaId())
                .orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));
        SubKriteria subKriteria = subKriteriaRepository.findById(dto.getSubKriteriaId())
                .orElseThrow(() -> new RuntimeException("Sub Kriteria tidak ditemukan"));

        // Cek apakah penilaian untuk siswa & kriteria ini sudah ada (Update) atau belum ada (Insert)
        Optional<Penilaian> existingPenilaian = penilaianRepository
                .findBySiswaIdAndSubKriteriaId(dto.getSiswaId(), dto.getSubKriteriaId());

        Penilaian penilaian;
        if (existingPenilaian.isPresent()) {
            penilaian = existingPenilaian.get();
            penilaian.setNilai(dto.getNilai()); // Update nilai lama
        } else {
            penilaian = Penilaian.builder()
                    .siswa(siswa)
                    .subKriteria(subKriteria)
                    .nilai(dto.getNilai())
                    .build(); // Buat nilai baru
        }

        return penilaianRepository.save(penilaian);
    }

    // 2. Kalkulasi Proses SPK Profile Matching untuk Semua Siswa
    public List<HasilRankingDTO> hitungSPK() {
        List<Siswa> listSiswa = siswaRepository.findAll();
        List<HasilRankingDTO> hasilList = new ArrayList<>();

        for (Siswa siswa : listSiswa) {
            List<Penilaian> penilaianSiswa = penilaianRepository.findBySiswaId(siswa.getId());
            if (penilaianSiswa.isEmpty()) continue;

            List<Double> listBobotCF = new ArrayList<>();
            List<Double> listBobotSF = new ArrayList<>();

            for (Penilaian p : penilaianSiswa) {
                // a. Hitung GAP = Nilai Siswa - Nilai Target
                // gap = 4-2, 4-2, 4-3, 4-3, 2-3 = 2, 2, 1, 1, -1
                double gap = p.getNilai() - p.getSubKriteria().getNilaiTarget();

                // b. Konversi GAP ke Nilai Bobot
                // bobot = 3.5, 3.5, 4.5, 4.5, 4
                double bobot = getBobotFromGap(gap);

                // c. Kelompokkan ke Core Factor atau Secondary Factor
                if (p.getSubKriteria().getJenisFaktor() == SubKriteria.JenisFaktor.CORE_FACTOR) {
                    listBobotCF.add(bobot);
                } else {
                    listBobotSF.add(bobot);
                }
            }

            // d. Hitung Rata-rata Core Factor (NCF) & Secondary Factor (NSF)
            double ncf = hitungRataRata(listBobotCF);
            double nsf = hitungRataRata(listBobotSF);

            // e. Hitung Nilai Total (N = 60% NCF + 40% NSF)
            double nilaiTotal = (BOBOT_CORE * ncf) + (BOBOT_SECONDARY * nsf);

            // f. Tentukan Status Kelulusan
            String status = nilaiTotal >= CUT_OFF_LULUS ? "LULUS / SIAP" : "BELUM SIAP";

            hasilList.add(HasilRankingDTO.builder()
                    .siswaId(siswa.getId())
                    .namaSiswa(siswa.getNama())
                    .nilaiCoreFactor(roundTwoDecimals(ncf))
                    .nilaiSecondaryFactor(roundTwoDecimals(nsf))
                    .nilaiTotal(roundTwoDecimals(nilaiTotal))
                    .statusKelulusan(status)
                    .build());
        }

        // g. Urutkan dan berikan Ranking
        Collections.sort(hasilList);
        for (int i = 0; i < hasilList.size(); i++) {
            hasilList.get(i).setRanking(i + 1);
        }

        return hasilList;
    }

    private double getBobotFromGap(double gap) {
        return bobotGapRepository.findByGap(gap)
                .map(BobotGap::getBobot)
                .orElse(1.0); // Default bobot terkecil jika tidak ditemukan
    }

    private double hitungRataRata(List<Double> list) {
        if (list.isEmpty()) return 0.0;
        return list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private double roundTwoDecimals(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

//    @Autowired
//    private PenilaianRepository penilaianRepository;

    /**
     * Method helper untuk mengambil objek Penilaian berdasarkan Siswa ID dan SubKriteria ID
     */
    public Penilaian getNilaiBySiswaAndKriteria(Long siswaId, Long subKriteriaId) {
        return penilaianRepository.findBySiswaIdAndSubKriteriaId(siswaId, subKriteriaId)
                .orElse(null);
    }
}
