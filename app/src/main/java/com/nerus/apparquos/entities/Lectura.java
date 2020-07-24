package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Opr_Lecturas")
public class Lectura {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_lectura")
    private Integer mIdLectura;

    @ColumnInfo(name = "id_padron")
    private Integer mIdPadron;


    @ColumnInfo(name = "id_medidor")
    private String mIdMedidor;

    @ColumnInfo(name = "lectura")
    private Integer mLectura;

    @ColumnInfo(name = "id_anomalia")
    private Integer mIdAnomalia;

    @ColumnInfo(name = "observaciones")
    private String mObservaciones;


    @ColumnInfo(name = "id_personal")
    private Integer mIdPersonal;

    @ColumnInfo(name = "fecha")
    private String mFecha;

    @ColumnInfo(name = "latitud")
    private double mLatitud;

    @ColumnInfo(name = "longitud")
    private double mLongitud;

    @ColumnInfo(name = "is_uploaded")
    private Integer mIsUploaded;

    @ColumnInfo(name = "fecha_uploaded")
    private String mFechaUploaded;

    @NonNull
    public Integer getIdLectura() {
        return mIdLectura;
    }

    public Lectura setIdLectura(@NonNull Integer idLectura) {
        mIdLectura = idLectura;
        return this;
    }

    public Integer getIdPadron() {
        return mIdPadron;
    }

    public Lectura setIdPadron(Integer idPadron) {
        mIdPadron = idPadron;
        return this;
    }

    public String getIdMedidor() {
        return mIdMedidor;
    }

    public Lectura setIdMedidor(String idMedidor) {
        mIdMedidor = idMedidor;
        return this;
    }

    public Integer getLectura() {
        return mLectura;
    }

    public Lectura setLectura(Integer lectura) {
        mLectura = lectura;
        return this;
    }

    public Integer getIdAnomalia() {
        return mIdAnomalia;
    }

    public Lectura setIdAnomalia(Integer idAnomalia) {
        mIdAnomalia = idAnomalia;
        return this;
    }

    public String getObservaciones() {
        return mObservaciones;
    }

    public Lectura setObservaciones(String observaciones) {
        mObservaciones = observaciones;
        return this;
    }

    public Integer getIdPersonal() {
        return mIdPersonal;
    }

    public Lectura setIdPersonal(Integer idPersonal) {
        mIdPersonal = idPersonal;
        return this;
    }

    public String getFecha() {
        return mFecha;
    }

    public Lectura setFecha(String fecha) {
        mFecha = fecha;
        return this;
    }

    public double getLatitud() {
        return mLatitud;
    }

    public Lectura setLatitud(double latitud) {
        mLatitud = latitud;
        return this;
    }

    public double getLongitud() {
        return mLongitud;
    }

    public Lectura setLongitud(double longitud) {
        mLongitud = longitud;
        return this;
    }

    public Integer getIsUploaded() {
        return mIsUploaded;
    }

    public Lectura setIsUploaded(Integer isUploaded) {
        mIsUploaded = isUploaded;
        return this;
    }

    public String getFechaUploaded() {
        return mFechaUploaded;
    }

    public Lectura setFechaUploaded(String fechaUploaded) {
        mFechaUploaded = fechaUploaded;
        return this;
    }

    public Lectura(@NonNull Integer idLectura, Integer idPadron, String idMedidor, Integer lectura, Integer idAnomalia, String observaciones, Integer idPersonal, String fecha, double latitud, double longitud, Integer isUploaded, String fechaUploaded) {
        mIdLectura = idLectura;
        mIdPadron = idPadron;
        mIdMedidor = idMedidor;
        mLectura = lectura;
        mIdAnomalia = idAnomalia;
        mObservaciones = observaciones;
        mIdPersonal = idPersonal;
        mFecha = fecha;
        mLatitud = latitud;
        mLongitud = longitud;
        mIsUploaded = isUploaded;
        mFechaUploaded = fechaUploaded;
    }

    @Override
    public String toString() {
        return "Lectura{" +
                " IdLectura=" + mIdLectura +
                ", IdPadron=" + mIdPadron +
                ", IdMedidor='" + mIdMedidor + '\'' +
                ", Lectura=" + mLectura +
                ", IdAnomalia=" + mIdAnomalia +
                ", Observaciones='" + mObservaciones + '\'' +
                ", IdPersonal=" + mIdPersonal +
                ", Fecha='" + mFecha + '\'' +
                ", Latitud=" + mLatitud +
                ", Longitud=" + mLongitud +
                ", IsUploaded=" + mIsUploaded +
                ", FechaUploaded='" + mFechaUploaded + '\'' +
                '}';
    }
}
