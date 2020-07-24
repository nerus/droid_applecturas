package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Cat_Anomalias")
public class Anomalia implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_anomalia")
    private Integer mId_anomalia;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "observaciones")
    private String mObservaciones;

    @ColumnInfo(name = "is_imagerequired")
    private Boolean mIs_imagerequired;

    @NonNull
    public Integer getId_anomalia() {
        return mId_anomalia;
    }

    public Anomalia setId_anomalia(@NonNull Integer id_anomalia) {
        mId_anomalia = id_anomalia;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public Anomalia setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public String getObservaciones() {
        return mObservaciones;
    }

    public Anomalia setObservaciones(String observaciones) {
        mObservaciones = observaciones;
        return this;
    }

    public Boolean getIs_imagerequired() {
        return mIs_imagerequired;
    }

    public Anomalia setIs_imagerequired(Boolean is_imagerequired) {
        mIs_imagerequired = is_imagerequired;
        return this;
    }

    public Anomalia(@NonNull Integer id_anomalia, String descripcion, String observaciones, Boolean is_imagerequired) {
        mId_anomalia = id_anomalia;
        mDescripcion = descripcion;
        mObservaciones = observaciones;
        mIs_imagerequired = is_imagerequired;
    }

    @Override
    public String toString() {
        return "Anomalia{" +
                " Id_anomalia=" + mId_anomalia +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Observaciones='" + mObservaciones + '\'' +
                ", Is_imagerequired=" + mIs_imagerequired +
                '}';
    }
}
