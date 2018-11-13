package com.tokosepeda.tikum.model;

import java.io.Serializable;

public class Grup implements Serializable {

    String id, idGrup, namaGrup, deskripsiGrup;

    public Grup() {
    }

    public Grup(String idGrup, String namaGrup, String deskripsiGrup) {
        this.idGrup = idGrup;
        this.namaGrup = namaGrup;
        this.deskripsiGrup = deskripsiGrup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGrup() {
        return idGrup;
    }

    public String getNamaGrup() {
        return namaGrup;
    }

    public String getDeskripsiGrup() {
        return deskripsiGrup;
    }
}
