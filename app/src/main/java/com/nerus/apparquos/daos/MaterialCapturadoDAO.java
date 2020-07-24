package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.nerus.apparquos.entities.MaterialCapturado;

import java.util.List;

@Dao
public abstract class MaterialCapturadoDAO {
    @Query("DELETE FROM Opr_MatCapturados")
   public abstract void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(MaterialCapturado material);

    @Delete
    public abstract void delete(MaterialCapturado item);

    @Update
    public abstract void update(MaterialCapturado item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertALL(List<MaterialCapturado> list);

    @Transaction
    public void updateData(List<MaterialCapturado> list) {
        deleteAll();
        insertALL(list);
    }

    @Query("SELECT * FROM Opr_MatCapturados WHERE id_orden=:idOrden")
    public abstract LiveData<List<MaterialCapturado>> getMaterialesByIdOrden(String idOrden);

    @Query( " SELECT m.* "+
            " FROM Opr_MatCapturados AS m" +
            " INNER JOIN Opr_OTCerradas as o ON o.id_orden = m.id_orden" +
            " WHERE o.id_empleado=:idPersonal")
    public abstract LiveData<List<MaterialCapturado>> getMaterialUsado(Integer idPersonal);
}