package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Cat_CausasNoEjecucion")
public class CausaNoEjecucion implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_causa")
    private Integer mIdCausa;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "observaciones")
    private String mObservaciones;

    @ColumnInfo(name = "is_imagerequired")
    private Boolean mImageRequired;

    @NonNull
    public Integer getIdCausa() {
        return mIdCausa;
    }

    public CausaNoEjecucion setIdCausa(@NonNull Integer idCausa) {
        mIdCausa = idCausa;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public CausaNoEjecucion setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public String getObservaciones() {
        return mObservaciones;
    }

    public CausaNoEjecucion setObservaciones(String observaciones) {
        mObservaciones = observaciones;
        return this;
    }

    public Boolean getImageRequired() {
        return mImageRequired;
    }

    public CausaNoEjecucion setImageRequired(Boolean imageRequired) {
        mImageRequired = imageRequired;
        return this;
    }

    public CausaNoEjecucion(@NonNull Integer idCausa, String descripcion, String observaciones, Boolean imageRequired) {
        mIdCausa = idCausa;
        mDescripcion = descripcion;
        mObservaciones = observaciones;
        mImageRequired = imageRequired;
    }

    @Override
    public String toString() {
        return "CausaNoEjecucion{" +
                " IdCausa=" + mIdCausa +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Observaciones='" + mObservaciones + '\'' +
                ", ImageRequired=" + mImageRequired +
                '}';
    }
}
