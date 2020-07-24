package com.nerus.apparquos.daos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.Parametro;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
@Dao
public abstract class ParametroDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Parametro parametro);

    @Update
    public abstract void update(Parametro parametro);

    @Query("DELETE FROM cfg_parametros")
    public abstract void deleteAll();

    @Query("SELECT * FROM cfg_parametros ")
    public abstract LiveData<List<Parametro>> getAllParametros();

    //@Query("SELECT * FROM cfg_parametros WHERE parametro=:name")
    //LiveData<Parametro> getParametroByName(String name);


    @Query("SELECT * FROM cfg_parametros WHERE parametro=:name")
    protected abstract LiveData<Parametro> getParametroByName(String name);


    @Query("SELECT * FROM cfg_parametros WHERE parametro=:name LIMIT 1")
    public abstract Parametro getParametroOfName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<Parametro> lista);


    public LiveData<Parametro> getDistinctParametroByName(final String name) {
        return new DistinctLiveData<Parametro>(){
            @Override
            protected boolean equals(Parametro newObj, Parametro lastObj) {
                //LogSNE.d("NERUS","NEW OBJ:"+newObj.toString());
                //LogSNE.d("NERUS","LAST OBJ:"+newObj.toString());

                return newObj != null && !newObj.equals(lastObj);
            }

            @Override
            protected LiveData<Parametro> load() { return getParametroByName(name); }
        }.asLiveData();
    }
}
