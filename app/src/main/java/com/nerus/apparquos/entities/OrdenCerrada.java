package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Opr_OTCerradas")
public class OrdenCerrada implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_orden")
    private String mIdOrden;

    @ColumnInfo(name = "id_trabajo")
    private Integer mIdTrabajo;

    @ColumnInfo(name = "trabajo")
    private String mTrabajo;

    @ColumnInfo(name = "id_empleado")
    private Integer mIdEmpleado;

    @ColumnInfo(name = "observa_b")
    private String mObservaB;

    @ColumnInfo(name = "observa_c")
    private String mObservaC;

    @ColumnInfo(name = "longitud")
    private double mLongitud;

    @ColumnInfo(name = "latitud")
    private double mLatitud;

    @ColumnInfo(name = "id_medidor")
    private String mIdMedidor;

    @ColumnInfo(name = "fecha_capturo")
    private String mFechaGenero;

    @ColumnInfo(name = "fecha_realizo")
    private String mFechaRealizo;

    @ColumnInfo(name = "fecha_cancelo")
    private String mFechaCancelo;

    @ColumnInfo(name = "ejecutada")
    private Boolean mEjecutada;

    @ColumnInfo(name = "id_noejecucion")
    private Integer mIdNoEjecucion;

    @ColumnInfo(name = "is_uploaded")
    private Boolean mUpLoaded;

    @ColumnInfo(name = "fecha_uploaded")
    private String mFechaUploaded;

    @NonNull
    public String getIdOrden() {
        return mIdOrden;
    }

    public OrdenCerrada setIdOrden(@NonNull String idOrden) {
        mIdOrden = idOrden;
        return this;
    }

    public Integer getIdTrabajo() {
        return mIdTrabajo;
    }

    public OrdenCerrada setIdTrabajo(Integer idTrabajo) {
        mIdTrabajo = idTrabajo;
        return this;
    }

    public String getTrabajo() {
        return mTrabajo;
    }

    public OrdenCerrada setTrabajo(String trabajo) {
        mTrabajo = trabajo;
        return this;
    }

    public Boolean getUpLoaded() {
        return mUpLoaded;
    }

    public OrdenCerrada setUpLoaded(Boolean upLoaded) {
        mUpLoaded = upLoaded;
        return this;
    }

    public Integer getIdEmpleado() {
        return mIdEmpleado;
    }

    public OrdenCerrada setIdEmpleado(Integer idEmpleado) {
        mIdEmpleado = idEmpleado;
        return this;
    }

    public String getObservaB() {
        return mObservaB;
    }

    public OrdenCerrada setObservaB(String observaB) {
        mObservaB = observaB;
        return this;
    }

    public String getObservaC() {
        return mObservaC;
    }

    public OrdenCerrada setObservaC(String observaC) {
        mObservaC = observaC;
        return this;
    }

    public double getLongitud() {
        return mLongitud;
    }

    public OrdenCerrada setLongitud(double longitud) {
        mLongitud = longitud;
        return this;
    }

    public double getLatitud() {
        return mLatitud;
    }

    public OrdenCerrada setLatitud(double latitud) {
        mLatitud = latitud;
        return this;
    }

    public String getIdMedidor() {
        return mIdMedidor;
    }

    public OrdenCerrada setIdMedidor(String idMedidor) {
        mIdMedidor = idMedidor;
        return this;
    }

    public String getFechaGenero() {
        return mFechaGenero;
    }

    public OrdenCerrada setFechaGenero(String fechaGenero) {
        mFechaGenero = fechaGenero;
        return this;
    }

    public String getFechaRealizo() {
        return mFechaRealizo;
    }

    public OrdenCerrada setFechaRealizo(String fechaRealizo) {
        mFechaRealizo = fechaRealizo;
        return this;
    }

    public String getFechaCancelo() {
        return mFechaCancelo;
    }

    public OrdenCerrada setFechaCancelo(String fechaCancelo) {
        mFechaCancelo = fechaCancelo;
        return this;
    }

    public Boolean getEjecutada() {
        return mEjecutada;
    }

    public OrdenCerrada setEjecutada(Boolean ejecutada) {
        mEjecutada = ejecutada;
        return this;
    }

    public Integer getIdNoEjecucion() {
        return mIdNoEjecucion;
    }

    public OrdenCerrada setIdNoEjecucion(Integer idNoEjecucion) {
        mIdNoEjecucion = idNoEjecucion;
        return this;
    }

    public String getFechaUploaded() {
        return mFechaUploaded;
    }

    public OrdenCerrada setFechaUploaded(String fechaUploaded) {
        mFechaUploaded = fechaUploaded;
        return this;
    }


    public OrdenCerrada(@NonNull String idOrden, Integer idTrabajo, String trabajo, Integer idEmpleado, String observaB, String observaC, double longitud, double latitud, String idMedidor, String fechaGenero, String fechaRealizo, String fechaCancelo, Boolean ejecutada, Integer idNoEjecucion, Boolean upLoaded, String fechaUploaded) {
        mIdOrden = idOrden;
        mIdTrabajo = idTrabajo;
        mTrabajo = trabajo;
        mIdEmpleado = idEmpleado;
        mObservaB = observaB;
        mObservaC = observaC;
        mLongitud = longitud;
        mLatitud = latitud;
        mIdMedidor = idMedidor;
        mFechaGenero = fechaGenero;
        mFechaRealizo = fechaRealizo;
        mFechaCancelo = fechaCancelo;
        mEjecutada = ejecutada;
        mIdNoEjecucion = idNoEjecucion;
        mUpLoaded = upLoaded;
        mFechaUploaded = fechaUploaded;
    }

    @Override
    public String toString() {
        return "OrdenCerrada{" +
                " IdOrden='" + mIdOrden + '\'' +
                ", IdTrabajo=" + mIdTrabajo +
                ", Trabajo='" + mTrabajo + '\'' +
                ", IdEmpleado=" + mIdEmpleado +
                ", ObservaB='" + mObservaB + '\'' +
                ", ObservaC='" + mObservaC + '\'' +
                ", Longitud=" + mLongitud +
                ", Latitud=" + mLatitud +
                ", IdMedidor='" + mIdMedidor + '\'' +
                ", FechaGenero='" + mFechaGenero + '\'' +
                ", FechaRealizo='" + mFechaRealizo + '\'' +
                ", FechaCancelo='" + mFechaCancelo + '\'' +
                ", Ejecutada=" + mEjecutada +
                ", IdNoEjecucion=" + mIdNoEjecucion +
                ", Uploaded=" + mUpLoaded +
                ", FechaUploaded='" + mFechaUploaded + '\'' +
                '}';
    }
}
