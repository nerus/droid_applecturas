package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cat_personal")
public final class Empleado {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_personal")
    private Integer mIdPersonal;

    @ColumnInfo(name = "nombre")
    private String mNombre;

    @ColumnInfo(name = "email")
    private String mMail;

    @ColumnInfo(name = "token")
    private String mToken;

    @ColumnInfo(name = "abc_lecturas")
    private Integer mAbcLecturas;

    @ColumnInfo(name = "abc_ordenes")
    private Integer mAbcOrdenes;

    public Integer getIdPersonal() {
        return mIdPersonal;
    }

    public Empleado setIdPersonal(Integer idPersonal) {
        mIdPersonal = idPersonal;
        return this;
    }

    public Integer getAbcLecturas() {
        return mAbcLecturas;
    }

    public Empleado setAbcLecturas(Integer abcLecturas) {
        mAbcLecturas = abcLecturas;
        return this;
    }

    public Integer getAbcOrdenes() {
        return mAbcOrdenes;
    }

    public Empleado setAbcOrdenes(Integer abcOrdenes) {
        mAbcOrdenes = abcOrdenes;
        return this;
    }

    public String getNombre() {
        return mNombre;
    }

    public Empleado setNombre(String nombre) {
        mNombre = nombre;
        return this;
    }

    @NonNull
    public String getMail() {
        return mMail;
    }

    public Empleado setMail(@NonNull String mail) {
        mMail = mail;
        return this;
    }

    public String getToken() {
        return mToken;
    }

    public Empleado setToken(String token) {
        mToken = token;
        return this;
    }

    public Empleado(@NonNull Integer idPersonal, String nombre, String mail, String token, Integer abcLecturas, Integer abcOrdenes) {
        mIdPersonal = idPersonal;
        mNombre = nombre;
        mMail = mail;
        mToken = token;
        mAbcLecturas = abcLecturas;
        mAbcOrdenes = abcOrdenes;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "mIdPersonal=" + mIdPersonal +
                ", mNombre='" + mNombre + '\'' +
                ", mMail='" + mMail + '\'' +
                ", mToken='" + mToken + '\'' +
                ", mAbcLecturas=" + mAbcLecturas +
                ", mAbcOrdenes=" + mAbcOrdenes +
                '}';
    }
}
