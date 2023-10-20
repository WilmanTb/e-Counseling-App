package com.e_counseling.e_counseling.Model;

import java.io.Serializable;

public class PengaduanClass implements Serializable {

    String JenisPelanggaran;
    String Deskripsi;
    String Pelapor;
    String SaranTindakan;
    String WaktuPelanggaran;
    String IdPengaduan;
    String Status;
    String Pelanggar;
    String Poin;
    String Foto;
    String TipePelanggaran;

    public String getTipePelanggaran() {
        return TipePelanggaran;
    }

    public void setTipePelanggaran(String tipePelanggaran) {
        TipePelanggaran = tipePelanggaran;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getLokasi() {
        return Lokasi;
    }

    public void setLokasi(String lokasi) {
        Lokasi = lokasi;
    }

    String Lokasi;

    public String getKelas() {
        return Kelas;
    }

    public void setKelas(String kelas) {
        Kelas = kelas;
    }

    String Kelas;

    public String getPoin() {
        return Poin;
    }

    public void setPoin(String poin) {
        Poin = poin;
    }

    public String getPelanggar() {
        return Pelanggar;
    }

    public void setPelanggar(String pelanggar) {
        Pelanggar = pelanggar;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public PengaduanClass(String jenisPelanggaran, String deskripsi, String pelapor, String saranTindakan, String waktuPelanggaran, String idPengaduan, String status, String pelanggar, String poin) {
        JenisPelanggaran = jenisPelanggaran;
        Deskripsi = deskripsi;
        Pelapor = pelapor;
        SaranTindakan = saranTindakan;
        WaktuPelanggaran = waktuPelanggaran;
        IdPengaduan = idPengaduan;
        Status = status;
        Pelanggar = pelanggar;
        Poin = poin;
    }

    public PengaduanClass(String jenisPelanggaran, String deskripsi, String pelapor, String saranTindakan, String waktuPelanggaran, String idPengaduan, String status, String pelanggar, String poin, String lokasi, String foto, String tipePelanggaran) {
        JenisPelanggaran = jenisPelanggaran;
        Deskripsi = deskripsi;
        Pelapor = pelapor;
        SaranTindakan = saranTindakan;
        WaktuPelanggaran = waktuPelanggaran;
        IdPengaduan = idPengaduan;
        Status = status;
        Pelanggar = pelanggar;
        Poin = poin;
        Lokasi = lokasi;
        Foto = foto;
        TipePelanggaran = tipePelanggaran;
    }

    public PengaduanClass() {

    }

    public PengaduanClass(String jenisPelanggaran, String deskripsi, String pelapor, String saranTindakan, String waktuPelanggaran, String idPengaduan, String status, String pelanggar) {
        JenisPelanggaran = jenisPelanggaran;
        Deskripsi = deskripsi;
        Pelapor = pelapor;
        SaranTindakan = saranTindakan;
        WaktuPelanggaran = waktuPelanggaran;
        IdPengaduan = idPengaduan;
        Status = status;
        Pelanggar = pelanggar;
        /*Poin = poin;
        Kelas = kelas;*/
    }


    public String getIdPengaduan() {
        return IdPengaduan;
    }

    public void setIdPengaduan(String idPengaduan) {
        IdPengaduan = idPengaduan;
    }

    public String getPelapor() {
        return Pelapor;
    }

    public void setPelapor(String pelapor) {
        Pelapor = pelapor;
    }

    public String getSaranTindakan() {
        return SaranTindakan;
    }

    public void setSaranTindakan(String saranTindakan) {
        SaranTindakan = saranTindakan;
    }

    public String getWaktuPelanggaran() {
        return WaktuPelanggaran;
    }

    public void setWaktuPelanggaran(String waktuPelanggaran) {
        WaktuPelanggaran = waktuPelanggaran;
    }

    public String getJenisPelanggaran() {
        return JenisPelanggaran;
    }

    public void setJenisPelanggaran(String jenisPelanggaran) {
        JenisPelanggaran = jenisPelanggaran;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        Deskripsi = deskripsi;
    }


}
