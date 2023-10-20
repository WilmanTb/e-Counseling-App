package com.e_counseling.e_counseling.Activity.Guru;

import java.io.Serializable;

public class BuatJadwal implements Serializable {
    String Pelanggar;
    String Tanggal;
    String Perihal;
    String MetodeKonseling;
    String JenisKonseling;
    String UidAnak;
    String NamaRuang;

    public String getNamaRuang() {
        return NamaRuang;
    }

    public void setNamaRuang(String namaRuang) {
        NamaRuang = namaRuang;
    }

    public BuatJadwal(String pelanggar, String tanggal, String perihal, String metodeKonseling, String jenisKonseling, String uidAnak, String uidOrangTua, String namaRuang) {
        Pelanggar = pelanggar;
        Tanggal = tanggal;
        Perihal = perihal;
        MetodeKonseling = metodeKonseling;
        JenisKonseling = jenisKonseling;
        UidAnak = uidAnak;
        UidOrangTua = uidOrangTua;
        NamaRuang = namaRuang;
    }

    public BuatJadwal(){

    }

    public String getPelanggar() {
        return Pelanggar;
    }

    public void setPelanggar(String pelanggar) {
        Pelanggar = pelanggar;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }

    public String getPerihal() {
        return Perihal;
    }

    public void setPerihal(String perihal) {
        Perihal = perihal;
    }

    public String getMetodeKonseling() {
        return MetodeKonseling;
    }

    public void setMetodeKonseling(String metodeKonseling) {
        MetodeKonseling = metodeKonseling;
    }

    public String getJenisKonseling() {
        return JenisKonseling;
    }

    public void setJenisKonseling(String jenisKonseling) {
        JenisKonseling = jenisKonseling;
    }

    public String getUidAnak() {
        return UidAnak;
    }

    public void setUidAnak(String uidAnak) {
        UidAnak = uidAnak;
    }

    public String getUidOrangTua() {
        return UidOrangTua;
    }

    public void setUidOrangTua(String uidOrangTua) {
        UidOrangTua = uidOrangTua;
    }

    String UidOrangTua;
}
