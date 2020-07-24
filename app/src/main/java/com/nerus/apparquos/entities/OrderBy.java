package com.nerus.apparquos.entities;

import java.io.Serializable;


public class OrderBy implements Serializable {
    private Integer mIdOrder;
    private String mDescripcion;

    public Integer getIdOrder() {
        return mIdOrder;
    }

    public OrderBy setIdOrder(Integer idOrder) {
        mIdOrder = idOrder;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public OrderBy setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public OrderBy(Integer idOrder, String descripcion) {
        mIdOrder = idOrder;
        mDescripcion = descripcion;
    }

    @Override
    public String toString() {
        return "OrderBy{" +
                " IdOrder=" + mIdOrder +
                ", Descripcion='" + mDescripcion + '\'' +
                '}';
    }
}
