package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerus.apparquos.entities.Subsistema;

import java.util.List;

@Dao
public interface SubsistemaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Subsistema sb);

    @Query("DELETE FROM cat_subsistemas")
    void deleteAll();

    @Query("SELECT * from cat_subsistemas")
    LiveData<List<Subsistema>> getAllSubsistemas();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Subsistema> list);
}

