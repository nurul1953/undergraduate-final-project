package com.spk.tk_profile_matching.service;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.spk.tk_profile_matching.dto.HasilRankingDTO;
import com.spk.tk_profile_matching.model.Siswa;
import com.spk.tk_profile_matching.model.SubKriteria;
import com.spk.tk_profile_matching.repository.SiswaRepository;
import com.spk.tk_profile_matching.repository.SubKriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class ExportService {

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private SubKriteriaRepository subKriteriaRepository;

    @Autowired
    private ProfileMatchingService profileMatchingService;

    // ==========================================
    // HELPER: MEMBUAT KOP SURAT SEKOLAH
    // ==========================================
    private void addKopSurat(Document document) {
        // Nama Sekolah (Sesuai Gambar 2)
        Paragraph namaSekolah = new Paragraph("TK AISYIYAH PROGRAM KHUSUS SAMBI")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(2);

        // Alamat & NPSN (Sesuai Gambar 2)
        Paragraph alamat = new Paragraph("Jati, Desa Sambi, Kec. Sambi, Kab. Boyolali, Prov. Jawa Tengah\nEmail: tkaisyiyah.sambi@gmail.com")
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(8);

        document.add(namaSekolah);
        document.add(alamat);

        // Garis Pembatas Kop Surat (Sesuai Gambar 1)
        SolidLine line = new SolidLine(1.5f);
        line.setColor(new DeviceRgb(0, 0, 0));
        LineSeparator lineSeparator = new LineSeparator(line);
        lineSeparator.setMarginBottom(15);
        document.add(lineSeparator);
    }

    // ==========================================
    // HELPER: MEMBUAT BAGIAN TANDA TANGAN DOKUMEN
    // ==========================================
    private void addTandaTangan(Document document, String namaPenandatangan) {
        // Format Tanggal Indonesia: Boyolali, [Tanggal Hari Ini]
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", new Locale("id", "ID"));
        String tanggalStr = "Boyolali, " + today.format(formatter);

        Paragraph ttdArea = new Paragraph()
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(30)
                .setFontSize(10);

        ttdArea.add(tanggalStr + "\n");
        ttdArea.add("Kepala Sekolah\n\n\n\n\n"); // Spasi Tanda Tangan
        ttdArea.add(new Paragraph(namaPenandatangan).setBold().setUnderline());

        document.add(ttdArea);
    }

    // Helper Styling Header Tabel Gray (Sesuai Gambar 1)
    private void addHeaderCell(Table table, String text) {
        Cell cell = new Cell();
        cell.setBackgroundColor(new DeviceRgb(240, 240, 240)); // Warna Abu-abu Terang
        cell.add(new Paragraph(text).setBold().setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cell.setPadding(6);
        table.addHeaderCell(cell);
    }


    // ==========================================
    // 1. LAPORAN HASIL EVALUASI SPK (HASIL & RANKING)
    // ==========================================
    public byte[] generatePdfHasilSPK() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(36, 36, 36, 36);

        // 1. Tambahkan Kop Surat
        addKopSurat(document);

        // 2. Judul Laporan (Sesuai Gambar 1)
        Paragraph title = new Paragraph("Laporan Hasil Evaluasi Kelayakan Kelulusan")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(title);

        // 3. Tabel Data Hasil SPK
        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 30, 15, 15, 15, 15})).useAllAvailableWidth();
        addHeaderCell(table, "Rank");
        addHeaderCell(table, "Nama Siswa");
        addHeaderCell(table, "Nilai CF");
        addHeaderCell(table, "Nilai SF");
        addHeaderCell(table, "Nilai Total");
        addHeaderCell(table, "Status");

        List<HasilRankingDTO> hasilList = profileMatchingService.hitungSPK();

        for (HasilRankingDTO h : hasilList) {
            table.addCell(new Cell().add(new Paragraph("#" + h.getRanking()).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(h.getNamaSiswa()).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", h.getNilaiCoreFactor())).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", h.getNilaiSecondaryFactor())).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", h.getNilaiTotal())).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(h.getStatusKelulusan()).setFontSize(9)));
        }
        document.add(table);

        // 4. Tambahkan Tanda Tangan
        addTandaTangan(document, "Mulyati, S.Pd");

        document.close();
        return out.toByteArray();
    }


    // ==========================================
    // 2. LAPORAN DATA KRITERIA & TARGET EVALUASI
    // ==========================================
    public byte[] generatePdfDataKriteria() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(36, 36, 36, 36);

        addKopSurat(document);

        Paragraph title = new Paragraph("Laporan Kriteria Analis")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(title);

        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 40, 25, 25})).useAllAvailableWidth();
        addHeaderCell(table, "No");
        addHeaderCell(table, "Nama Kriteria / Indikator");
        addHeaderCell(table, "Nilai Target");
        addHeaderCell(table, "Jenis Faktor");

        List<SubKriteria> listKriteria = subKriteriaRepository.findAll();
        int no = 1;
        for (SubKriteria k : listKriteria) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(no++)).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(k.getNamaKriteria()).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(k.getNilaiTarget())).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(k.getJenisFaktor())).setFontSize(9)));
        }
        document.add(table);

        addTandaTangan(document, "Mulyati, S.Pd");

        document.close();
        return out.toByteArray();
    }


    // ==========================================
    // 3. LAPORAN MASTER DATA SISWA
    // ==========================================
    public byte[] generatePdfDataSiswa() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(36, 36, 36, 36);

        addKopSurat(document);

        Paragraph title = new Paragraph("Laporan Master Data Siswa")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(title);

        // Tabel dengan 4 Kolom: No, NIS, Nama Lengkap, Tahun Ajaran
        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 25, 45, 20})).useAllAvailableWidth();
        addHeaderCell(table, "No");
        addHeaderCell(table, "NIS");
        addHeaderCell(table, "Nama Lengkap Siswa");
        addHeaderCell(table, "Tahun Ajaran");

        List<Siswa> listSiswa = siswaRepository.findAll();
        int no = 1;
        for (Siswa s : listSiswa) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(no++)).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(s.getNisn() != null ? s.getNisn() : "-").setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(s.getNama()).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(s.getTahunAjaran() != null ? s.getTahunAjaran() : "-").setFontSize(9)));
        }
        document.add(table);

        addTandaTangan(document, "Mulyati, S.Pd");

        document.close();
        return out.toByteArray();
    }

    // ==========================================
    // LAPORAN MATRIKS PENILAIAN SISWA
    // ==========================================
    public byte[] generatePdfDataPenilaian() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(36, 36, 36, 36);

        addKopSurat(document);

        Paragraph title = new Paragraph("Laporan Matriks Penilaian Evaluasi Siswa")
                .setFontSize(14)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(title);

        List<SubKriteria> kriteriaList = subKriteriaRepository.findAll();
        List<Siswa> siswaList = siswaRepository.findAll();

        // Hitung total kolom (No + Nama + Kriteria + Rata-Rata)
        int totalCols = 3 + kriteriaList.size();
        float[] columnWidths = new float[totalCols];
        columnWidths[0] = 8;  // No
        columnWidths[1] = 30; // Nama Siswa
        for (int i = 2; i < totalCols - 1; i++) {
            columnWidths[i] = 50.0f / kriteriaList.size(); // Lebar dinamis kriteria
        }
        columnWidths[totalCols - 1] = 12; // Rata-rata

        Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
        // Ubah dari k.getNamaSubKriteria() menjadi k.getNamaKriteria()
        addHeaderCell(table, "No");
        addHeaderCell(table, "Nama Siswa");
        for (SubKriteria k : kriteriaList) {
            addHeaderCell(table, k.getNamaKriteria());
        }
        addHeaderCell(table, "Rata-Rata");

        int no = 1;
        for (Siswa s : siswaList) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(no++)).setFontSize(8)));
            table.addCell(new Cell().add(new Paragraph(s.getNama()).setFontSize(8)));

            double totalNilai = 0;
            int count = 0;

            for (SubKriteria k : kriteriaList) {
                // Ambil nilai per kriteria
                var p = profileMatchingService.getNilaiBySiswaAndKriteria(s.getId(), k.getId());
                double val = (p != null) ? p.getNilai() : 0.0;
                if (p != null) {
                    totalNilai += val;
                    count++;
                }
                table.addCell(new Cell().add(new Paragraph(val > 0 ? String.valueOf((int)val) : "-").setFontSize(8)));
            }

            double avg = count > 0 ? totalNilai / count : 0.0;
            table.addCell(new Cell().add(new Paragraph(String.format("%.1f", avg)).setFontSize(8).setBold()));
        }

        document.add(table);
        addTandaTangan(document, "Mulyati, S.Pd");

        document.close();
        return out.toByteArray();
    }
}
