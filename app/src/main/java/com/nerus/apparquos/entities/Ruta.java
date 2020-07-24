package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Cat_Rutas")
public class Ruta implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private Integer mId;

    @ColumnInfo(name = "sb")
    private Integer mSb;


    @ColumnInfo(name = "sector")
    private Integer mSector;

    @ColumnInfo(name = "id_ruta")
    private Integer mIdRuta;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "registros")
    private Integer mRegistros;

    @ColumnInfo(name = "medidos")
    private Integer mMedidos;

    @ColumnInfo(name = "fijos")
    private Integer mFijos;

    @ColumnInfo(name = "promedios")
    private Integer mPromedios;

    @ColumnInfo(name = "capturadas")
    private Integer mCapturadas;

    @ColumnInfo(name = "enviadas")
    private Integer mEnviadas;

    @ColumnInfo(name = "observaciones")
    private String mObservaciones;

    public String getObservaciones() {
        return mObservaciones;
    }

    public Ruta setObservaciones(String observaciones) {
        mObservaciones = observaciones;
        return this;
    }

    @NonNull
    public Integer getId() {
        return mId;
    }

    public Ruta setId(@NonNull Integer id) {
        mId = id;
        return this;
    }

    public Integer getSb() {
        return mSb;
    }

    public Ruta setSb(Integer sb) {
        mSb = sb;
        return this;
    }

    public Integer getSector() {
        return mSector;
    }

    public Ruta setSector(Integer sector) {
        mSector = sector;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public Ruta setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public Integer getIdRuta() {
        return mIdRuta;
    }

    public Ruta setIdRuta(Integer idRuta) {
        mIdRuta = idRuta;
        return this;
    }

    public Integer getRegistros() {
        return mRegistros;
    }

    public Ruta setRegistros(Integer registros) {
        mRegistros = registros;
        return this;
    }

    public Integer getMedidos() {
        return mMedidos;
    }

    public Ruta setMedidos(Integer medidos) {
        mMedidos = medidos;
        return this;
    }

    public Integer getFijos() {
        return mFijos;
    }

    public Ruta setFijos(Integer fijos) {
        mFijos = fijos;
        return this;
    }

    public Integer getPromedios() {
        return mPromedios;
    }

    public Ruta setPromedios(Integer promedios) {
        mPromedios = promedios;
        return this;
    }

    public Integer getCapturadas() {
        return mCapturadas;
    }

    public Ruta setCapturadas(Integer capturadas) {
        mCapturadas = capturadas;
        return this;
    }

    public Integer getEnviadas() {
        return mEnviadas;
    }

    public Ruta setEnviadas(Integer enviadas) {
        mEnviadas = enviadas;
        return this;
    }
    @Ignore
    public Ruta(@NonNull Integer id, Integer sb, Integer sector, Integer idRuta, String descripcion, Integer registros) {
        mId = id;
        mSb = sb;
        mSector = sector;
        mIdRuta = idRuta;
        mDescripcion = descripcion;
        mRegistros = registros;
        mMedidos = 0;
        mFijos = 0;
        mPromedios = 0;
        mCapturadas = 0;
        mEnviadas = 0;
        mObservaciones = "";
    }
    /*
    public Ruta(@NonNull Integer id, Integer sb, Integer sector, Integer idRuta, String descripcion, Integer registros, Integer medidos, Integer fijos, Integer promedios, Integer capturadas, Integer enviadas) {
        mId = id;
        mSb = sb;
        mSector = sector;
        mIdRuta = idRuta;
        mDescripcion = descripcion;
        mRegistros = registros;
        mMedidos = medidos;
        mFijos = fijos;
        mPromedios = promedios;
        mCapturadas = capturadas;
        mEnviadas = enviadas;
    }*/
    public Ruta(@NonNull Integer id, Integer sb, Integer sector, Integer idRuta, String descripcion, Integer registros, Integer medidos, Integer fijos, Integer promedios, Integer capturadas, Integer enviadas, String observaciones) {
        mId = id;
        mSb = sb;
        mSector = sector;
        mIdRuta = idRuta;
        mDescripcion = descripcion;
        mRegistros = registros;
        mMedidos = medidos;
        mFijos = fijos;
        mPromedios = promedios;
        mCapturadas = capturadas;
        mEnviadas = enviadas;
        mObservaciones = observaciones;
    }
    @Override
    public String toString() {
        return "Ruta{" +
                " Id=" + mId +
                ", Sb=" + mSb +
                ", Sector=" + mSector +
                ", IdRuta=" + mIdRuta +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Registros=" + mRegistros +
                ", Medidos=" + mMedidos +
                ", Fijos=" + mFijos +
                ", Promedios=" + mPromedios +
                ", Capturadas=" + mCapturadas +
                ", Enviadas=" + mEnviadas +
                ", Observaciones='" + mObservaciones + '\'' +
                '}';
    }

}
