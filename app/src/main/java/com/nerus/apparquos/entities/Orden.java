package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Opr_Ordenes")
public class Orden implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_orden")
    private String mIdOrden;

    @ColumnInfo(name = "id_padron")
    private Integer mIdPadron;

    @ColumnInfo(name = "id_cuenta")
    private Integer mIdCuenta;

    @ColumnInfo(name = "id_empleado")
    private Integer mIdEmpleado;

    @ColumnInfo(name = "id_trabajo")
    private Integer mIdTrabajo;

    @ColumnInfo(name = "trabajo")
    private String mTrabajo;

    @ColumnInfo(name = "observa_a")
    private String mObservaA;

    @ColumnInfo(name = "longitud")
    private double mLongitud;

    @ColumnInfo(name = "latitud")
    private double mLatitud;

    @ColumnInfo(name = "localizacion")
    private String mLocalizacion;

    @ColumnInfo(name = "id_medidor")
    private String mIdMedidor;

    @ColumnInfo(name = "estatus")
    private String mEstatus;

    @ColumnInfo(name = "nombre")
    private String mNombre;

    @ColumnInfo(name = "direccion")
    private String mDireccion;

    @ColumnInfo(name = "colonia")
    private String mColonia;

    @ColumnInfo(name = "poblacion")
    private String mPoblacion;

    @ColumnInfo(name = "telefono")
    private String mTelefono;

    @ColumnInfo(name = "genero")
    private String mGenero;

    @ColumnInfo(name = "fecha_genero")
    private String mFechaGenero;

    @ColumnInfo(name = "is_uploaded")
    private Boolean mIsUpLoaded;

    @ColumnInfo(name = "fecha_uploaded")
    private String mFechaUploaded;

    @ColumnInfo(name = "capturado")
    private Boolean mCapturado;

    public Boolean getCapturado() {
        return mCapturado;
    }

    public Orden setCapturado(Boolean capturado) {
        mCapturado = capturado;
        return this;
    }

    @NonNull
    public String getIdOrden() {
        return mIdOrden;
    }

    public Orden setIdOrden(@NonNull String idOrden) {
        mIdOrden = idOrden;
        return this;
    }

    public Integer getIdPadron() {
        return mIdPadron;
    }

    public Orden setIdPadron(Integer idPadron) {
        mIdPadron = idPadron;
        return this;
    }

    public Integer getIdCuenta() {
        return mIdCuenta;
    }

    public Orden setIdCuenta(Integer idCuenta) {
        mIdCuenta = idCuenta;
        return this;
    }

    public Integer getIdEmpleado() {
        return mIdEmpleado;
    }

    public Orden setIdEmpleado(Integer idEmpleado) {
        mIdEmpleado = idEmpleado;
        return this;
    }

    public Integer getIdTrabajo() {
        return mIdTrabajo;
    }

    public Orden setIdTrabajo(Integer idTrabajo) {
        mIdTrabajo = idTrabajo;
        return this;
    }

    public String getTrabajo() {
        return mTrabajo;
    }

    public Orden setTrabajo(String trabajo) {
        mTrabajo = trabajo;
        return this;
    }

    public String getObservaA() {
        return mObservaA;
    }

    public Orden setObservaA(String observaA) {
        mObservaA = observaA;
        return this;
    }

    public double getLongitud() {
        return mLongitud;
    }

    public Orden setLongitud(double longitud) {
        mLongitud = longitud;
        return this;
    }

    public double getLatitud() {
        return mLatitud;
    }

    public Orden setLatitud(double latitud) {
        mLatitud = latitud;
        return this;
    }

    public String getLocalizacion() {
        return mLocalizacion;
    }

    public Orden setLocalizacion(String localizacion) {
        mLocalizacion = localizacion;
        return this;
    }

    public String getIdMedidor() {
        return mIdMedidor;
    }

    public Orden setIdMedidor(String idMedidor) {
        mIdMedidor = idMedidor;
        return this;
    }

    public String getEstatus() {
        return mEstatus;
    }

    public Orden setEstatus(String estatus) {
        mEstatus = estatus;
        return this;
    }

    public String getNombre() {
        return mNombre;
    }

    public Orden setNombre(String nombre) {
        mNombre = nombre;
        return this;
    }

    public String getDireccion() {
        return mDireccion;
    }

    public Orden setDireccion(String direccion) {
        mDireccion = direccion;
        return this;
    }

    public String getColonia() {
        return mColonia;
    }

    public Orden setColonia(String colonia) {
        mColonia = colonia;
        return this;
    }

    public String getPoblacion() {
        return mPoblacion;
    }

    public Orden setPoblacion(String poblacion) {
        mPoblacion = poblacion;
        return this;
    }

    public String getTelefono() {
        return mTelefono;
    }

    public Orden setTelefono(String telefono) {
        mTelefono = telefono;
        return this;
    }

    public String getGenero() {
        return mGenero;
    }

    public Orden setGenero(String genero) {
        mGenero = genero;
        return this;
    }

    public String getFechaGenero() {
        return mFechaGenero;
    }

    public Orden setFechaGenero(String fechaGenero) {
        mFechaGenero = fechaGenero;
        return this;
    }

    public Boolean getIsUpLoaded() {
        return mIsUpLoaded;
    }

    public Orden setIsUpLoaded(Boolean uploaded) {
        mIsUpLoaded = uploaded;
        return this;
    }

    public String getFechaUploaded() {
        return mFechaUploaded;
    }

    public Orden setFechaUploaded(String fechaUploaded) {
        mFechaUploaded = fechaUploaded;
        return this;
    }

    public Boolean getUpLoaded() {
        return mIsUpLoaded;
    }

    public Orden setUpLoaded(Boolean upLoaded) {
        mIsUpLoaded = upLoaded;
        return this;
    }

    public Orden(@NonNull String idOrden, Integer idPadron, Integer idCuenta, Integer idEmpleado, Integer idTrabajo, String trabajo, String observaA, double longitud, double latitud, String localizacion, String idMedidor, String estatus, String nombre, String direccion, String colonia, String poblacion, String telefono, String genero, String fechaGenero, Boolean isUpLoaded, String fechaUploaded, Boolean capturado) {
        mIdOrden = idOrden;
        mIdPadron = idPadron;
        mIdCuenta = idCuenta;
        mIdEmpleado = idEmpleado;
        mIdTrabajo = idTrabajo;
        mTrabajo = trabajo;
        mObservaA = observaA;
        mLongitud = longitud;
        mLatitud = latitud;
        mLocalizacion = localizacion;
        mIdMedidor = idMedidor;
        mEstatus = estatus;
        mNombre = nombre;
        mDireccion = direccion;
        mColonia = colonia;
        mPoblacion = poblacion;
        mTelefono = telefono;
        mGenero = genero;
        mFechaGenero = fechaGenero;
        mIsUpLoaded = isUpLoaded;
        mFechaUploaded = fechaUploaded;
        mCapturado = capturado;
    }

    @Override
    public String toString() {
        return "Orden{" +
                " IdOrden='" + mIdOrden + '\'' +
                ", IdPadron=" + mIdPadron +
                ", IdCuenta=" + mIdCuenta +
                ", IdEmpleado=" + mIdEmpleado +
                ", IdTrabajo=" + mIdTrabajo +
                ", Trabajo='" + mTrabajo + '\'' +
                ", ObservaA='" + mObservaA + '\'' +
                ", Longitud=" + mLongitud +
                ", Latitud=" + mLatitud +
                ", Localizacion='" + mLocalizacion + '\'' +
                ", IdMedidor='" + mIdMedidor + '\'' +
                ", Estatus='" + mEstatus + '\'' +
                ", Nombre='" + mNombre + '\'' +
                ", Direccion='" + mDireccion + '\'' +
                ", Colonia='" + mColonia + '\'' +
                ", Poblacion='" + mPoblacion + '\'' +
                ", Telefono='" + mTelefono + '\'' +
                ", Genero='" + mGenero + '\'' +
                ", FechaGenero='" + mFechaGenero + '\'' +
                ", IsUpLoaded=" + mIsUpLoaded +
                ", FechaUploaded='" + mFechaUploaded + '\'' +
                ", Capturado=" + mCapturado +
                '}';
    }
}
