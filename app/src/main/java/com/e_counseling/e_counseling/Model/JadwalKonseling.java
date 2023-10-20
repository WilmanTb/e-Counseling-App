package com.e_counseling.e_counseling.Model;

import java.io.Serializable;

public class JadwalKonseling implements Serializable {
    String Konseling;
    String Jadwal;
    String Perihal;
    String Keterangan;
    String Status;
    String OrangTua;
    String Siswa;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOrangTua() {
        return OrangTua;
    }

    public void setOrangTua(String orangTua) {
        OrangTua = orangTua;
    }

    public String getSiswa() {
        return Siswa;
    }

    public void setSiswa(String siswa) {
        Siswa = siswa;
    }

    public String getKonseling() {
        return Konseling;
    }

    public void setKonseling(String konseling) {
        Konseling = konseling;
    }

    public String getJadwal() {
        return Jadwal;
    }

    public void setJadwal(String jadwal) {
        Jadwal = jadwal;
    }

    public String getPerihal() {
        return Perihal;
    }

    public void setPerihal(String perihal) {
        Perihal = perihal;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }
}
