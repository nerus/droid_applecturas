package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.entities.Trabajo;

import java.util.List;

@Dao
public abstract class OrdenDAO {
    @Query("DELETE FROM Opr_Ordenes")
   public abstract void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Orden orden);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertALL(List<Orden> list);
    @Update
    public abstract void update(Orden orden);

    @Transaction
    public void updateData(List<Orden> list) {
        deleteAll();
        insertALL(list);
    }

               /* " SELECT -2 as id_trabajo, 'TRABAJOS' as descripcion, count(*) as registros, 1 AS id_tipo " +
            " FROM Opr_Ordenes " +
            " WHERE id_empleado =:idEmpleado" +

            " , (select count(*) from Opr_OTCerradas as oc where oc.id_empleado=:idEmpleado ) as capturadas " +
            " , (select count(*) from Opr_OTCerradas as oc where oc.id_empleado=:idEmpleado and oc.is_uploaded) as enviadas " +

            " UNION ALL" +
            " SELECT -1 as id_trabajo, 'COLONIAS' as descripcion, count(*) as registros, 2 AS id_tipo " +
            " FROM Opr_Ordenes " +
            " WHERE id_empleado =:idEmpleado" +
            " UNION ALL" + */

               /*
               *
               *
               *
               */

    @Query( " SELECT ot.id_trabajo as id, ot.trabajo as descripcion, count(*) as registros, 1 AS id_tipo " +
            " , SUM(CASE when IFNULL(oc.id_orden,'')<>'' then 1 else 0 END) as capturadas " +
            " , SUM(CASE when ot.is_uploaded then 1 else 0 END) as enviadas " +
            " FROM Opr_Ordenes as ot" +
            " LEFT JOIN Opr_OTCerradas as oc on oc.id_orden = ot.id_orden"+
            " WHERE ot.id_empleado =:idEmpleado" +
            " GROUP BY ot.id_trabajo, ot.trabajo" +
            " UNION ALL" +
            " SELECT -2 as id_trabajo, 'TRABAJOS' as descripcion, count(*) as registros, 1 AS id_tipo " +
            " , SUM(CASE when IFNULL(oc.id_orden,'')<>'' then 1 else 0 END) as capturadas " +
            " , SUM(CASE when ot.is_uploaded then 1 else 0 END) as enviadas " +
            " FROM Opr_Ordenes as ot" +
            " LEFT JOIN Opr_OTCerradas as oc on oc.id_orden = ot.id_orden"+
            " WHERE ot.id_empleado =:idEmpleado" +
            " UNION ALL" +
            " SELECT -1 as id_trabajo, 'COLONIAS' as descripcion, count(*) as registros, 2 AS id_tipo " +
            " , SUM(CASE when IFNULL(oc.id_orden,'')<>'' then 1 else 0 END) as capturadas " +
            " , SUM(CASE when ot.is_uploaded then 1 else 0 END) as enviadas " +
            " FROM Opr_Ordenes as ot" +
            " LEFT JOIN Opr_OTCerradas as oc on oc.id_orden = ot.id_orden"+
            " WHERE ot.id_empleado =:idEmpleado" +
            " UNION ALL" +
            " SELECT count(*) as id" +
            " , ot.poblacion||'-'||ot.colonia as descripcion" +
            " , count(*) as registros, 2 AS id_tipo " +
            " , SUM(CASE when IFNULL(oc.id_orden,'')<>'' then 1 else 0 END) as capturadas " +
            " , SUM(CASE when ot.is_uploaded then 1 else 0 END) as enviadas " +
            " FROM Opr_Ordenes as ot" +
            " LEFT JOIN Opr_OTCerradas as oc on oc.id_orden = ot.id_orden"+
            " WHERE ot.id_empleado =:idEmpleado" +
            " GROUP BY ot.poblacion||'-'||ot.colonia" +
            " ORDER BY id_tipo, id_trabajo, descripcion"
           )
    public abstract LiveData<List<Trabajo>> getResumenOT (Integer idEmpleado);
/*
    @Query( " SELECT count(*) as id_trabajo" +
            " ,poblacion||'-'||colonia as descripcion" +
            " , count(*) as registros " +
            " FROM Opr_Ordenes as a " +
            " WHERE id_empleado =:idEmpleado" +
            " GROUP BY poblacion||'-'||colonia"
            )
    LiveData<List<Trabajo>> getResumenPob (Integer idEmpleado);
*/
    @Query("  SELECT ot.* FROM Opr_Ordenes as ot"+
            " WHERE id_empleado=:idEmpleado ORDER BY localizacion,direccion")
    public abstract LiveData<List<Orden>>  getOrdenesByEmpleado(Integer idEmpleado);

    @Query("SELECT * FROM Opr_Ordenes WHERE id_empleado=:idEmpleado and id_trabajo=:idTrabajo ORDER BY localizacion,direccion")
    public abstract LiveData<List<Orden>> getOrdenesByTrabajo(Integer idEmpleado, Integer idTrabajo);

    @Query("SELECT * FROM Opr_Ordenes WHERE id_empleado=:idEmpleado and (poblacion||'-'||colonia)=:pob_colonia ORDER BY localizacion,direccion")
    public abstract LiveData<List<Orden>> getOrdenesByColonia(Integer idEmpleado, String pob_colonia);


    @Query("UPDATE Opr_Ordenes SET is_uploaded=1, fecha_uploaded=:cFecha WHERE id_orden=:idOrden")
    public abstract void setUpLoad(String idOrden, String cFecha);


}