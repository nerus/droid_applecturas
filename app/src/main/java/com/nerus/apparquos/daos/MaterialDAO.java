package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;


import com.nerus.apparquos.entities.Material;

import java.util.List;

@Dao
public abstract class MaterialDAO {
    @Query("DELETE FROM cat_materiales")
   public abstract void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Material material);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertALL(List<Material> list);

    @Transaction
    public void updateData(List<Material> list) {
        deleteAll();
        insertALL(list);
    }

    @Query("SELECT * FROM Cat_Materiales ORDER BY descripcion")
    public abstract LiveData<List<Material>> getAllMateriales();

    @Query("SELECT * FROM Cat_Materiales WHERE id_material=:id")
    public abstract LiveData<Material> getMaterial(String id);
}