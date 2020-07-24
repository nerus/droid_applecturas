package com.nerus.apparquos.daos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerus.apparquos.entities.Ruta;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
@Dao
public abstract class RutaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Ruta ruta);

    @Query("DELETE FROM Cat_Rutas")
    public abstract void deleteAll();

    @Query("SELECT * FROM Cat_Rutas")
    public abstract LiveData<List<Ruta>> getAllRutas();


    @Update
    public abstract void update(Ruta ruta);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<Ruta> list);

    @Query("SELECT * FROM Cat_Rutas WHERE sb=:sub and sector=:sec")
    public abstract LiveData<List<Ruta>> getRutasBySbSec(Integer sub, Integer sec);
    public LiveData<List<Ruta>> getDistinctRutasBySbSec(final Integer sub, final Integer sec) {
        return new DistinctLiveData<List<Ruta>>(){
            @Override
            protected boolean equals(List<Ruta> newObj, List<Ruta> lastObj) {
                LogSNE.d("NERUS","NEW OBJ:"+newObj.toString());
                LogSNE.d("NERUS","LAST OBJ:"+newObj.toString());
                return newObj != null && !newObj.equals(lastObj);
            }

            @Override
            protected LiveData<List<Ruta>> load() { return getRutasBySbSec(sub,sec); }
        }.asLiveData();
    }
}
