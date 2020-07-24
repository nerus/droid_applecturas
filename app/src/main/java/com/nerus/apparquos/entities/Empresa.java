package com.nerus.apparquos.entities;

import java.io.Serializable;


public class Empresa implements Serializable {
    private String mRFC;
    private String mDescripcion;
    private String mFecha;
    private String mDireccion;

    public String getRFC() {
        return mRFC;
    }

    public Empresa setRFC(String RFC) {
        mRFC = RFC;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public Empresa setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public String getFecha() {
        return mFecha;
    }

    public Empresa setFecha(String fecha) {
        mFecha = fecha;
        return this;
    }

    public String getDireccion() {
        return mDireccion;
    }

    public Empresa setDireccion(String direccion) {
        mDireccion = direccion;
        return this;
    }

    public Empresa(String RFC, String descripcion, String fecha, String direccion) {
        mRFC = RFC;
        mDescripcion = descripcion;
        mFecha = fecha;
        mDireccion = direccion;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                " RFC='" + mRFC + '\'' +
                ", Descripcion='" + mDescripcion + '\'' +
                ", Fecha='" + mFecha + '\'' +
                ", Direccion='" + mDireccion + '\'' +
                '}';
    }

    public static Empresa getHint(){
        return new Empresa("SNE1302225E3"
                ,"SOLUCIONES NERUS SA DE CV"
                ,""
                ,"FAMILIA ROTARIA 452-D, COL. DOCTORES CP 87024, CD. VICTORIA, TAMAULIPAS.");
    }
}
