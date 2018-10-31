package com.tokosepeda.tikum.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Toko implements Serializable {

    private String idToko, namaToko, emailToko, noHp, alamatToko, latToko, longToko, sparePart, imageToko;

    public Toko() {
    }

    public Toko(String idToko, String namaToko, String emailToko, String noHp, String alamatToko, String latToko, String longToko, String sparePart, String imageToko) {
        this.idToko = idToko;
        this.namaToko = namaToko;
        this.emailToko = emailToko;
        this.noHp = noHp;
        this.alamatToko = alamatToko;
        this.latToko = latToko;
        this.longToko = longToko;
        this.sparePart = sparePart;
        this.imageToko = imageToko;
    }

    public String getIdToko() {
        return idToko;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public String getEmailToko() {
        return emailToko;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getAlamatToko() {
        return alamatToko;
    }

    public String getLatToko() {
        return latToko;
    }

    public String getLongToko() {
        return longToko;
    }

    public String getSparePart() {
        return sparePart;
    }

    public String getImageToko() {
        return imageToko;
    }
}
