package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerus.apparquos.entities.OrdenCerrada;

import java.util.List;

@Dao
public interface OrdenCerradaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrdenCerrada orden);

    @Update
    void update(OrdenCerrada orden);

    @Query("SELECT * FROM Opr_OTCerradas WHERE id_orden=:idOrden LIMIT 1")
    LiveData<OrdenCerrada> getOrdenCerrada(String idOrden);

    @Query("SELECT * FROM Opr_OTCerradas WHERE id_empleado=:idPersonal")
    LiveData<List<OrdenCerrada>> getOrdenesCerradas(Integer idPersonal);
}