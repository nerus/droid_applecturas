package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cat_sectores")
public class Sector {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private Integer mId;

    @ColumnInfo(name = "sb")
    private Integer mSb;


    @ColumnInfo(name = "sector")
    private Integer mSector;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "registros")
    private String mRegistros;

    @NonNull
    public Integer getId() {
        return mId;
    }

    public void setId(@NonNull Integer id) {
        mId = id;
    }

    public Integer getSb() {
        return mSb;
    }

    public void setSb(Integer sb) {
        mSb = sb;
    }

    public Integer getSector() {
        return mSector;
    }

    public void setSector(Integer sector) {
        mSector = sector;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String descripcion) {
        mDescripcion = descripcion;
    }

    public String getRegistros() {
        return mRegistros;
    }

    public Sector setRegistros(String registros) {
        mRegistros = registros;
        return this;
    }

    public Sector(@NonNull Integer id, Integer sb, Integer sector, String descripcion, String registros) {
        mId = id;
        mSb = sb;
        mSector = sector;
        mDescripcion = descripcion;
        mRegistros = registros;
    }

    @Override
    public String toString() {
        return "Sector{" +
                " Id=" + mId +
                ", Sb=" + mSb +
                ", Sector=" + mSector +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Registros='" + mRegistros + '\'' +
                '}';
    }
}
