package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerus.apparquos.entities.CausaNoEjecucion;

import java.util.List;

@Dao
public interface CausaNoEjecucionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CausaNoEjecucion noEjecucion);

    @Query("DELETE FROM Cat_CausasNoEjecucion")
    void deleteAll();

    @Query("SELECT * FROM Cat_CausasNoEjecucion ORDER BY descripcion")
    LiveData<List<CausaNoEjecucion>> getAllCausas();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CausaNoEjecucion> list);

}
