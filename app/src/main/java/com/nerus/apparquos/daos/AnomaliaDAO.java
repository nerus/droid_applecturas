package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerus.apparquos.entities.Anomalia;
import com.nerus.apparquos.entities.Ruta;

import java.util.List;

@Dao
public interface AnomaliaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Anomalia anomalia);

    @Query("DELETE FROM Cat_Anomalias")
    void deleteAll();

    @Query("SELECT * FROM Cat_Anomalias ORDER BY descripcion")
    LiveData<List<Anomalia>> getAllAnomalias();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Anomalia> list);
}
