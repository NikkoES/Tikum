package com.tokosepeda.tikum.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Serializable{

    private String id, idUser, namaUser, email, nomorHp, jenisKelamin, alamat, tempatLahir, tanggalLahir, foto, sepeda, outfit, latitude, longitude;

    public User() {
    }

    public User(String id, String idUser, String namaUser, String email, String nomorHp, String jenisKelamin, String alamat, String tempatLahir, String tanggalLahir, String foto, String sepeda, String outfit) {
        this.id = id;
        this.idUser = idUser;
        this.namaUser = namaUser;
        this.email = email;
        this.nomorHp = nomorHp;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.foto = foto;
        this.sepeda = sepeda;
        this.outfit = outfit;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String getEmail() {
        return email;
    }

    public String getNomorHp() {
        return nomorHp;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getFoto() {
        return foto;
    }

    public String getSepeda() {
        return sepeda;
    }

    public String getOutfit() {
        return outfit;
    }
}