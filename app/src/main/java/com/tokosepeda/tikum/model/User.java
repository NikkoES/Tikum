package com.tokosepeda.tikum.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Serializable{

    private String id, idUser, namaUser, email, nomorHp, foto, sepeda, latitude, longitude;

    public User() {
    }

    public User(String id, String idUser, String namaUser, String email, String nomorHp, String foto, String sepeda) {
        this.id = id;
        this.idUser = idUser;
        this.namaUser = namaUser;
        this.email = email;
        this.nomorHp = nomorHp;
        this.foto = foto;
        this.sepeda = sepeda;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getFoto() {
        return foto;
    }

    public String getSepeda() {
        return sepeda;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
