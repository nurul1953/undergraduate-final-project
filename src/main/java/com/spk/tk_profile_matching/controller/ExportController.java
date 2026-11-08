package com.spk.tk_profile_matching.controller;

import com.spk.tk_profile_matching.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller untuk Download Data Siswa & Kriteria
@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    // Export Data Siswa (PDF / Excel)
    @GetMapping("/siswa")
    public ResponseEntity<byte[]> downloadDataSiswa() {
        byte[] pdfBytes = exportService.generatePdfDataSiswa();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Data_Siswa.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // Export Data Kriteria & Target
    @GetMapping("/kriteria")
    public ResponseEntity<byte[]> downloadDataKriteria() {
        byte[] pdfBytes = exportService.generatePdfDataKriteria();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Data_Kriteria.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // Export Hasil Pemeringkatan SPK
    @GetMapping("/hasil")
    public ResponseEntity<byte[]> downloadHasilRanking() {
        byte[] pdfBytes = exportService.generatePdfHasilSPK();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Laporan_Hasil_SPK_TK.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // Export Matriks Penilaian Siswa ke PDF
    @GetMapping("/penilaian")
    public ResponseEntity<byte[]> downloadDataPenilaian() {
        byte[] pdfBytes = exportService.generatePdfDataPenilaian();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Matriks_Penilaian_Siswa.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
