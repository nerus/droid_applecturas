package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//@Entity(tableName = "Cat_Trabajos")
public class Trabajo implements Serializable {
    //@PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private Integer mIdTrabajo;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "registros")
    private String mRegistros;

    @ColumnInfo(name = "capturadas")
    private Integer mCapturadas;

    @ColumnInfo(name = "enviadas")
    private Integer mEnviadas;

    @ColumnInfo(name = "id_tipo")
    private Integer mIdTipo;

    @NonNull
    public Integer getIdTrabajo() {
        return mIdTrabajo;
    }

    public Trabajo setIdTrabajo(@NonNull Integer idTrabajo) {
        mIdTrabajo = idTrabajo;
        return this;
    }

    public Integer getCapturadas() {
        return mCapturadas;
    }

    public Trabajo setCapturadas(Integer capturadas) {
        mCapturadas = capturadas;
        return this;
    }

    public Integer getEnviadas() {
        return mEnviadas;
    }

    public Trabajo setEnviadas(Integer enviadas) {
        mEnviadas = enviadas;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public Trabajo setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public String getRegistros() {
        return mRegistros;
    }

    public Trabajo setRegistros(String registros) {
        mRegistros = registros;
        return this;
    }

    public Integer getIdTipo() {
        return mIdTipo;
    }

    public Trabajo setIdTipo(Integer idTipo) {
        mIdTipo = idTipo;
        return this;
    }

    public Trabajo(@NonNull Integer idTrabajo, String descripcion, String registros, Integer capturadas, Integer enviadas, Integer idTipo) {
        mIdTrabajo = idTrabajo;
        mDescripcion = descripcion;
        mRegistros = registros;
        mCapturadas = capturadas;
        mEnviadas = enviadas;
        mIdTipo = idTipo;
    }

    @Override
    public String toString() {
        return "Trabajo{" +
                "  IdTrabajo=" + mIdTrabajo +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Registros='" + mRegistros + '\'' +
                ", Capturadas=" + mCapturadas +
                ", Enviadas=" + mEnviadas +
                ", IdTipo=" + mIdTipo +
                '}';
    }
}
