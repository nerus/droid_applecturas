package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cfg_Parametros")
public class Parametro {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "parametro")
    private String mParametro;

    @ColumnInfo(name = "valor")
    private String mValor;

    @ColumnInfo(name = "descripcion")
    private String mDescripcion;

    @ColumnInfo(name = "posible_valor")
    private String mPosibleValor;

    @ColumnInfo(name = "fecha")
    private String mFecha;

    @NonNull
    public String getParametro() {
        return mParametro;
    }

    public Parametro setParametro(@NonNull String parametro) {
        mParametro = parametro;
        return this;
    }

    public String getValor() {
        return mValor;
    }

    public Parametro setValor(String valor) {
        mValor = valor;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public Parametro setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public String getPosibleValor() {
        return mPosibleValor;
    }

    public Parametro setPosibleValor(String posibleValor) {
        mPosibleValor = posibleValor;
        return this;
    }

    public String getFecha() {
        return mFecha;
    }

    public Parametro setFecha(String fecha) {
        mFecha = fecha;
        return this;
    }

    public Parametro(@NonNull String parametro, String valor, String descripcion, String posibleValor, String fecha) {
        mParametro = parametro;
        mValor = valor;
        mDescripcion = descripcion;
        mPosibleValor = posibleValor;
        mFecha = fecha;
    }

    @Override
    public String toString() {
        return "Parametro{" +
               " Parametro='" + mParametro + '\'' +
               ", Valor='" + mValor + '\'' +
               ", Descripcion='" + mDescripcion + '\'' +
               ", PosibleValor='" + mPosibleValor + '\'' +
               ", Fecha='" + mFecha + '\'' +
               '}';
    }
}
