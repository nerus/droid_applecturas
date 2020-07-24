package com.nerus.apparquos.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "sys_usuarios")
public final class Usuario implements Serializable {

    @ColumnInfo(name = "id_personal")
    private Integer mIdPersonal;

    @ColumnInfo(name = "id_rol")
    private Integer mIdRol;

    @ColumnInfo(name = "nombre")
    private String mNombre;

    @ColumnInfo(name = "usuario")
    private String mUsuario;

    @ColumnInfo(name = "id_usuario")
    private String mIdUsuario;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private String mMail;

    @ColumnInfo(name = "token")
    private String mToken;

    public Integer getIdPersonal() {
        return mIdPersonal;
    }

    public Usuario setIdPersonal(Integer idPersonal) {
        mIdPersonal = idPersonal;
        return this;
    }

    public Integer getIdRol() {
        return mIdRol;
    }

    public Usuario setIdRol(Integer idRol) {
        mIdRol = idRol;
        return this;
    }

    public String getNombre() {
        return mNombre;
    }

    public Usuario setNombre(String nombre) {
        mNombre = nombre;
        return this;
    }

    public String getUsuario() {
        return mUsuario;
    }

    public Usuario setUsuario(String usuario) {
        mUsuario = usuario;
        return this;
    }

    public String getIdUsuario() {
        return mIdUsuario;
    }

    public Usuario setIdUsuario(String idUsuario) {
        mIdUsuario = idUsuario;
        return this;
    }

    @NonNull
    public String getMail() {
        return mMail;
    }

    public Usuario setMail(@NonNull String mail) {
        mMail = mail;
        return this;
    }

    public String getToken() {
        return mToken;
    }

    public Usuario setToken(String token) {
        mToken = token;
        return this;
    }

    public Usuario(Integer idPersonal, Integer idRol, String nombre, String usuario, String idUsuario, @NonNull String mail, String token) {
        mIdPersonal = idPersonal;
        mIdRol = idRol;
        mNombre = nombre;
        mUsuario = usuario;
        mIdUsuario = idUsuario;
        mMail = mail;
        mToken = token;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                " IdPersonal=" + mIdPersonal +
                ", IdRol=" + mIdRol +
                ", Nombre='" + mNombre + '\'' +
                ", Usuario='" + mUsuario + '\'' +
                ", IdUsuario='" + mIdUsuario + '\'' +
                ", Mail='" + mMail + '\'' +
                ", Token='" + mToken + '\'' +
                '}';
    }
}
