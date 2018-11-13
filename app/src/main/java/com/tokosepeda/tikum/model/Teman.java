package com.tokosepeda.tikum.model;

import java.io.Serializable;

public class Teman implements Serializable {

    private String idFriend, id, idUser, namaUser, email, nomorHp, foto, sepeda, latitude, longitude;

    public Teman() {
    }

    public Teman(String id, String idUser, String namaUser, String email, String nomorHp, String foto, String sepeda) {
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setIdFriend(String idFriend) {
        this.idFriend = idFriend;
    }

    public String getIdFriend() {
        return idFriend;
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
}
