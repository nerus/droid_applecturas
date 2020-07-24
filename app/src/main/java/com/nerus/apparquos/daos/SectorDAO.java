package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.nerus.apparquos.entities.Sector;

import java.util.List;

@Dao
public interface SectorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Sector sector);

    @Query("DELETE FROM cat_sectores")
    void deleteAll();

    @Query("SELECT * from cat_sectores")
    LiveData<List<Sector>> getAllSectores();

    @Query("SELECT * FROM cat_sectores WHERE sb =:sb")
    LiveData<List<Sector>> getSectoresBySb(Integer sb);

    @Query("SELECT * FROM cat_sectores LIMIT 1")
    LiveData<List<Sector>> getSector();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Sector> list);
}
