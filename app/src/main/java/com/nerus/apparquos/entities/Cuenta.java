package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Cat_Padron")
public final class Cuenta implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private Integer mId;


    @ColumnInfo(name = "idrow")
    private Integer mIdrow;

    @ColumnInfo(name = "sb")
    private Integer mSb;

    @ColumnInfo(name = "sector")
    private Integer mSector;

    @ColumnInfo(name = "id_ruta")
    private Integer mId_ruta;

    @ColumnInfo(name = "id_Padron")
    private Integer mId_Padron;

    @ColumnInfo(name = "id_Cuenta")
    private Integer mId_Cuenta;

    @ColumnInfo(name = "id_Poblacion")
    private Integer mId_Poblacion;

    @ColumnInfo(name = "razon_social")
    private String mRazon_social;

    @ColumnInfo(name = "direccion")
    private String mDireccion;

    @ColumnInfo(name = "clase_usuario")
    private String mClase_usuario;

    @ColumnInfo(name = "giro")
    private String mGiro;

    @ColumnInfo(name = "medidor")
    private String mMedidor;

    @ColumnInfo(name = "localizacion")
    private String mLocalizacion;

    @ColumnInfo(name = "ruta")
    private String mRuta;

    @ColumnInfo(name = "servicio")
    private String mServicio;

    @ColumnInfo(name = "situacion")
    private String mSituacion;

    @ColumnInfo(name = "tarifa")
    private String mTarifa;

    @ColumnInfo(name = "tipo_calculo")
    private String mTipo_calculo;

    @ColumnInfo(name = "id_giro")
    private Integer mId_giro;

    @ColumnInfo(name = "id_tipocalculo")
    private Integer mId_tipocalculo;

    @ColumnInfo(name = "lectura_ant")
    private Integer mLectura_ant;

    @ColumnInfo(name = "promedio")
    private Integer mPromedio;

    @ColumnInfo(name = "latitud")
    private double mLatitud;

    @ColumnInfo(name = "longitud")
    private double mLongitud;

    @ColumnInfo(name = "capturado")
    private Boolean mCapturado;

    @NonNull
    public Integer getId() {
        return mId;
    }

    public void setId(@NonNull Integer id) {
        mId = id;
    }

    public Integer getIdrow() {
        return mIdrow;
    }

    public void setIdrow(Integer idrow) {
        mIdrow = idrow;
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

    public Integer getId_ruta() {
        return mId_ruta;
    }

    public void setId_ruta(Integer id_ruta) {
        mId_ruta = id_ruta;
    }

    public Integer getId_Padron() {
        return mId_Padron;
    }

    public void setId_Padron(Integer id_Padron) {
        mId_Padron = id_Padron;
    }

    public Integer getId_Cuenta() {
        return mId_Cuenta;
    }

    public void setId_Cuenta(Integer id_Cuenta) {
        mId_Cuenta = id_Cuenta;
    }

    public Integer getId_Poblacion() {
        return mId_Poblacion;
    }

    public void setId_Poblacion(Integer id_Poblacion) {
        mId_Poblacion = id_Poblacion;
    }

    public String getRazon_social() {
        return mRazon_social;
    }

    public void setRazon_social(String razon_social) {
        mRazon_social = razon_social;
    }

    public String getDireccion() {
        return mDireccion;
    }

    public void setDireccion(String direccion) {
        mDireccion = direccion;
    }

    public String getClase_usuario() {
        return mClase_usuario;
    }

    public void setClase_usuario(String clase_usuario) {
        mClase_usuario = clase_usuario;
    }

    public String getGiro() {
        return mGiro;
    }

    public void setGiro(String giro) {
        mGiro = giro;
    }

    public String getMedidor() {
        return mMedidor;
    }

    public void setMedidor(String medidor) {
        mMedidor = medidor;
    }

    public String getLocalizacion() {
        return mLocalizacion;
    }

    public void setLocalizacion(String localizacion) {
        mLocalizacion = localizacion;
    }

    public String getRuta() {
        return mRuta;
    }

    public void setRuta(String ruta) {
        mRuta = ruta;
    }

    public String getServicio() {
        return mServicio;
    }

    public void setServicio(String servicio) {
        mServicio = servicio;
    }

    public String getSituacion() {
        return mSituacion;
    }

    public void setSituacion(String situacion) {
        mSituacion = situacion;
    }

    public String getTarifa() {
        return mTarifa;
    }

    public void setTarifa(String tarifa) {
        mTarifa = tarifa;
    }

    public String getTipo_calculo() {
        return mTipo_calculo;
    }

    public void setTipo_calculo(String tipo_calculo) {
        mTipo_calculo = tipo_calculo;
    }

    public Integer getId_giro() {
        return mId_giro;
    }

    public void setId_giro(Integer id_giro) {
        mId_giro = id_giro;
    }

    public Integer getId_tipocalculo() {
        return mId_tipocalculo;
    }

    public void setId_tipocalculo(Integer id_tipocalculo) {
        mId_tipocalculo = id_tipocalculo;
    }

    public Integer getLectura_ant() {
        return mLectura_ant;
    }

    public void setLectura_ant(Integer lectura_ant) {
        mLectura_ant = lectura_ant;
    }

    public Integer getPromedio() {
        return mPromedio;
    }

    public void setPromedio(Integer promedio) {
        mPromedio = promedio;
    }

    public double getLatitud() {
        return mLatitud;
    }

    public void setLatitud(double latitud) {
        mLatitud = latitud;
    }

    public double getLongitud() {
        return mLongitud;
    }

    public void setLongitud(double longitud) {
        mLongitud = longitud;
    }

    public Boolean getCapturado() {
        return mCapturado;
    }

    public void setCapturado(Boolean capturado) {
        mCapturado = capturado;
    }

    public Cuenta(@NonNull Integer id, Integer idrow, Integer sb, Integer sector, Integer id_ruta, Integer id_Padron, Integer id_Cuenta, Integer id_Poblacion, String razon_social, String direccion, String clase_usuario, String giro, String medidor, String localizacion, String ruta, String servicio, String situacion, String tarifa, String tipo_calculo, Integer id_giro, Integer id_tipocalculo, Integer lectura_ant, Integer promedio, double latitud, double longitud, Boolean capturado) {
        mId = id;
        mIdrow = idrow;
        mSb = sb;
        mSector = sector;
        mId_ruta = id_ruta;
        mId_Padron = id_Padron;
        mId_Cuenta = id_Cuenta;
        mId_Poblacion = id_Poblacion;
        mRazon_social = razon_social;
        mDireccion = direccion;
        mClase_usuario = clase_usuario;
        mGiro = giro;
        mMedidor = medidor;
        mLocalizacion = localizacion;
        mRuta = ruta;
        mServicio = servicio;
        mSituacion = situacion;
        mTarifa = tarifa;
        mTipo_calculo = tipo_calculo;
        mId_giro = id_giro;
        mId_tipocalculo = id_tipocalculo;
        mLectura_ant = lectura_ant;
        mPromedio = promedio;
        mLatitud = latitud;
        mLongitud = longitud;
        mCapturado = capturado;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "Id=" + mId +
                ", Idrow=" + mIdrow +
                ", Sb=" + mSb +
                ", Sector=" + mSector +
                ", Id_ruta=" + mId_ruta +
                ", Id_Padron=" + mId_Padron +
                ", Id_Cuenta=" + mId_Cuenta +
                ", Id_Poblacion=" + mId_Poblacion +
                ", Razon_social='" + mRazon_social + '\'' +
                ", Direccion='" + mDireccion + '\'' +
                ", Clase_usuario='" + mClase_usuario + '\'' +
                ", Giro='" + mGiro + '\'' +
                ", Medidor='" + mMedidor + '\'' +
                ", Localizacion='" + mLocalizacion + '\'' +
                ", Ruta='" + mRuta + '\'' +
                ", Servicio='" + mServicio + '\'' +
                ", Situacion='" + mSituacion + '\'' +
                ", Tarifa='" + mTarifa + '\'' +
                ", Tipo_calculo='" + mTipo_calculo + '\'' +
                ", Id_giro=" + mId_giro +
                ", Id_tipocalculo=" + mId_tipocalculo +
                ", Lectura_ant=" + mLectura_ant +
                ", Promedio=" + mPromedio +
                ", Latitud=" + mLatitud +
                ", Longitud=" + mLongitud +
                ", Capturado=" + mCapturado +
                '}';
    }
}
