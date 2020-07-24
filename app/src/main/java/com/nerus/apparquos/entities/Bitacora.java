package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Opr_Bitacoras")
public class Bitacora implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_bitacora")
    private Integer mIdBitacora;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "fecha")
    private String mFecha;

    @ColumnInfo(name = "longitud")
    private Float mLongitud;

    @ColumnInfo(name = "latitud")
    private Float mLatitud;

    @ColumnInfo(name = "id_personal")
    private Integer mIdPersonal;

    @ColumnInfo(name = "id_usuario")
    private String mIdUsuario;

    @NonNull
    public Integer getIdBitacora() {
        return mIdBitacora;
    }

    public Bitacora setIdBitacora(@NonNull Integer idBitacora) {
        mIdBitacora = idBitacora;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public Bitacora setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public String getFecha() {
        return mFecha;
    }

    public Bitacora setFecha(String fecha) {
        mFecha = fecha;
        return this;
    }

    public Float getLongitud() {
        return mLongitud;
    }

    public Bitacora setLongitud(Float longitud) {
        mLongitud = longitud;
        return this;
    }

    public Float getLatitud() {
        return mLatitud;
    }

    public Bitacora setLatitud(Float latitud) {
        mLatitud = latitud;
        return this;
    }

    public Integer getIdPersonal() {
        return mIdPersonal;
    }

    public Bitacora setIdPersonal(Integer idPersonal) {
        mIdPersonal = idPersonal;
        return this;
    }

    public String getIdUsuario() {
        return mIdUsuario;
    }

    public Bitacora setIdUsuario(String idUsuario) {
        mIdUsuario = idUsuario;
        return this;
    }

    public Bitacora(@NonNull Integer idBitacora, String descripcion, String fecha, Float longitud, Float latitud, Integer idPersonal, String idUsuario) {
        mIdBitacora = idBitacora;
        mDescripcion = descripcion;
        mFecha = fecha;
        mLongitud = longitud;
        mLatitud = latitud;
        mIdPersonal = idPersonal;
        mIdUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Bitacora{" +
                " IdBitacora=" + mIdBitacora +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Fecha='" + mFecha + '\'' +
                ", Longitud=" + mLongitud +
                ", Latitud=" + mLatitud +
                ", IdPersonal=" + mIdPersonal +
                ", IdUsuario='" + mIdUsuario + '\'' +
                '}';
    }
}
