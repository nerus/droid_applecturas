package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Cat_Materiales")
public class Material implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_material")
    private String mIdMaterial;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "existencia")
    private Integer mExistencia;

    @ColumnInfo(name = "costo")
    private float mCosto;

    @ColumnInfo(name = "udm")
    private String mUdm;

    @ColumnInfo(name = "fecha_downloaded")
    private String mFechaDownloaded;

    @NonNull
    public String getIdMaterial() {
        return mIdMaterial;
    }

    public Material setIdMaterial(@NonNull String idMaterial) {
        mIdMaterial = idMaterial;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public Material setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public Integer getExistencia() {
        return mExistencia;
    }

    public Material setExistencia(Integer existencia) {
        mExistencia = existencia;
        return this;
    }

    public float getCosto() {
        return mCosto;
    }

    public Material setCosto(float costo) {
        mCosto = costo;
        return this;
    }

    public String getUdm() {
        return mUdm;
    }

    public Material setUdm(String udm) {
        mUdm = udm;
        return this;
    }

    public String getFechaDownloaded() {
        return mFechaDownloaded;
    }

    public Material setFechaDownloaded(String fechaDownloaded) {
        mFechaDownloaded = fechaDownloaded;
        return this;
    }

    public Material(@NonNull String idMaterial, String descripcion, Integer existencia, float costo, String udm, String fechaDownloaded) {
        mIdMaterial = idMaterial;
        mDescripcion = descripcion;
        mExistencia = existencia;
        mCosto = costo;
        mUdm = udm;
        mFechaDownloaded = fechaDownloaded;
    }

    @Override
    public String toString() {
        return "Material{" +
                " IdMaterial='" + mIdMaterial + '\'' +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Existencia=" + mExistencia +
                ", Costo=" + mCosto +
                ", Udm='" + mUdm + '\'' +
                ", FechaDownloaded='" + mFechaDownloaded + '\'' +
                " }";
    }
}
