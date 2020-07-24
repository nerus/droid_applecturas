package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


public class Archivo implements Serializable {
    private Integer mId;
    private String mIdFile;
    private String mDescripcion;
    private String mFilePath;

    public Integer getId() {
        return mId;
    }

    public Archivo setId(Integer id) {
        mId = id;
        return this;
    }

    public String getIdFile() {
        return mIdFile;
    }

    public Archivo setIdFile(String idFile) {
        mIdFile = idFile;
        return this;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public Archivo setDescripcion(String descripcion) {
        mDescripcion = descripcion;
        return this;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public Archivo setFilePath(String filePath) {
        mFilePath = filePath;
        return this;
    }

    public Archivo(Integer id, String idFile, String descripcion, String filePath) {
        mId = id;
        mIdFile = idFile;
        mDescripcion = descripcion;
        mFilePath = filePath;
    }

    @Override
    public String toString() {
        return "Archivo{" +
                " Id=" + mId +
                ", IdFile='" + mIdFile + '\'' +
                ", Descripcion='" + mDescripcion + '\'' +
                ", FilePath='" + mFilePath + '\'' +
                '}';
    }
}
