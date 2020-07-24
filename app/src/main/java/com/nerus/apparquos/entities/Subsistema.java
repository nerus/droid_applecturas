package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cat_subsistemas")
public class Subsistema {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "sb")
    private Integer mSb;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "registros")
    private String mRegistros;

    @NonNull
    public Integer getSb() {
        return mSb;
    }

    public void setSb(@NonNull Integer sb) {
        mSb = sb;
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

    public Subsistema setRegistros(String registros) {
        mRegistros = registros;
        return this;
    }

    public Subsistema(@NonNull Integer sb, String descripcion, String registros) {
        mSb = sb;
        mDescripcion = descripcion;
        mRegistros = registros;
    }

    @Override
    public String toString() {
        return "Subsistema{" +
                " Sb=" + mSb +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Registros='" + mRegistros + '\'' +
                '}';
    }
}
