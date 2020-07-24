package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Lectura;

import java.util.List;

@Dao
public interface LecturaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Lectura lectura);

    @Query("DELETE FROM Opr_Lecturas")
    void deleteAll();

    @Query("SELECT * FROM Opr_Lecturas ORDER BY fecha")
    LiveData<List<Lectura>> getAllLecturas();

    @Query("SELECT * FROM Opr_Lecturas WHERE id_padron=:idpadron LIMIT 1")
    LiveData<List<Lectura>> getLecturaByIdPadron(Integer idpadron);

    @Update
    void update(Lectura lectura);

    @Query( " SELECT L.id_lectura,L.id_padron,L.id_medidor,L.lectura,L.id_anomalia,L.observaciones,L.id_personal,L.fecha,L.latitud,L.longitud,L.is_uploaded,L.fecha_uploaded" +
            " FROM Opr_Lecturas as L" +
            " INNER JOIN Cat_Padron as P on P.id_Padron = L.id_padron " +
            " WHERE p.sb =:sub and p.sector =:sec and p.id_ruta =:ruta")
    LiveData<List<Lectura>> getLecturasByRuta(Integer sub, Integer sec, Integer ruta);

    @Query("UPDATE Opr_Lecturas SET is_uploaded=1, fecha_uploaded=:fecha_upload WHERE id_padron=:idPadron")
    public void update(String fecha_upload, Integer idPadron);


    @Query( " DELETE  " +
            " FROM Opr_Lecturas " +
            " WHERE id_padron in ( SELECT id_padron FROM Cat_Padron AS p " +
            " WHERE p.sb =:sub and p.sector =:sec and p.id_ruta =:ruta )")
    public void deleteLecturasByRuta(Integer sub, Integer sec, Integer ruta);

}
