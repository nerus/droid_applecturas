package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Opr_MatCapturados", primaryKeys = {"id_orden", "id_material"})
public class MaterialCapturado implements Serializable {

/*
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String mId;

*/
    @NonNull
    @ColumnInfo(name = "id_orden")
    private String mIdOrden;

    @NonNull
    @ColumnInfo(name = "id_material")
    private String mIdMaterial;

    @ColumnInfo(name = "udm")
    private String mUdm;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "cantidad")
    private String mCantidad;

    @ColumnInfo(name = "fecha_captura")
    private String mFechaCaptura;

    @ColumnInfo(name = "observacion")
    private String mObservacion;

    public String getIdOrden() {
        return mIdOrden;
    }

    public MaterialCapturado setIdOrden(String idOrden) {
        mIdOrden = idOrden;
        return this;
    }

    public String getIdMaterial() {
        return mIdMaterial;
    }

    public MaterialCapturado setIdMaterial(String idMaterial) {
        mIdMaterial = idMaterial;
        return this;
    }

    public String getUdm() {
        return mUdm;
    }

    public MaterialCapturado setUdm(String udm) {
        mUdm = udm;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public MaterialCapturado setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public String getCantidad() {
        return mCantidad;
    }

    public MaterialCapturado setCantidad(String cantidad) {
        mCantidad = cantidad;
        return this;
    }

    public String getFechaCaptura() {
        return mFechaCaptura;
    }

    public MaterialCapturado setFechaCaptura(String fechaCaptura) {
        mFechaCaptura = fechaCaptura;
        return this;
    }

    public String getObservacion() {
        return mObservacion;
    }

    public MaterialCapturado setObservacion(String observacion) {
        mObservacion = observacion;
        return this;
    }

    public MaterialCapturado(String idOrden, String idMaterial, String udm, String descripcion, String cantidad, String fechaCaptura, String observacion) {
        mIdOrden = idOrden;
        mIdMaterial = idMaterial;
        mUdm = udm;
        mDescripcion = descripcion;
        mCantidad = cantidad;
        mFechaCaptura = fechaCaptura;
        mObservacion = observacion;
    }

    @Override
    public String toString() {
        return "MaterialCapturado{" +
                " IdOrden='" + mIdOrden + '\'' +
                ", IdMaterial='" + mIdMaterial + '\'' +
                ", Udm='" + mUdm + '\'' +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Cantidad='" + mCantidad + '\'' +
                ", FechaCaptura='" + mFechaCaptura + '\'' +
                ", Observacion='" + mObservacion + '\'' +
                '}';
    }
}
