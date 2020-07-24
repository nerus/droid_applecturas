package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerus.apparquos.entities.Empleado;

import java.util.List;

@Dao
public interface EmpleadoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Empleado empleado);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Empleado> list);

    @Query("DELETE FROM cat_personal")
    void deleteAll();

    @Query("SELECT * from cat_personal")
    LiveData<List<Empleado> > getAllEmpleados();

    @Update
    void update(Empleado empleado);

    @Query("SELECT * FROM cat_personal WHERE token<>'' LIMIT 1")
    LiveData<Empleado> getEmpleadoLogged();
}
