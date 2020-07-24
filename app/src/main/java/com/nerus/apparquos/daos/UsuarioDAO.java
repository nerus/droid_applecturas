package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.entities.Usuario;

import java.util.List;

@Dao
public abstract class UsuarioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(Usuario usuario);

    @Query("DELETE FROM sys_usuarios")
    abstract void deleteAll();

    @Query("SELECT * from sys_usuarios limit 1")
    public abstract LiveData<Usuario> getUsuario();

    @Transaction
    public void updateData(Usuario usuario) {
        deleteAll();
        insert(usuario);
    }
}
