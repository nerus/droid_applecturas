package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Ruta;

import java.util.List;

@Dao
public interface CuentaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cuenta cuenta);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Cuenta> cuentaList);

    @Query("DELETE FROM Cat_Padron")
    void deleteAll();

    @Query("SELECT * from Cat_Padron")
    LiveData<List<Cuenta>> getAllCuentas();

    @Query("SELECT * FROM Cat_Padron WHERE sb=:sub and sector=:sec and id_ruta=:ruta ORDER BY idrow")
    LiveData<List<Cuenta>> getCuentasByRuta(Integer sub, Integer sec, Integer ruta);

    @Query("SELECT * FROM Cat_Padron WHERE id_padron=:idpadron LIMIT 1")
    LiveData<Cuenta> getCuentaByIdPadron(Integer idpadron);

    @Update
    void update(Cuenta cuenta);


/*
    String cQuery = "SELECT sb, sector, id_ruta, ruta as descripcion"
            + ", SUM(CASE WHEN id_tipocalculo=1 THEN 1 ELSE 0 END) AS medidos"
            + ", SUM(CASE WHEN id_tipocalculo=2 THEN 1 ELSE 0 END) AS promedios"
            + ", SUM(CASE WHEN id_tipocalculo=3 THEN 1 ELSE 0 END) AS fijos"
            + ", COUNT(*) AS registros"
            // + ", SUM(CASE WHEN id_tipocalculo=1 and ifnull(capt.id_padron,0)>0 THEN 1 ELSE 0 END) AS cmedidos"
            //  + ", SUM(CASE WHEN id_tipocalculo=2 and ifnull(capt.id_padron,0)>0 THEN 1 ELSE 0 END) AS cpromedios"
            //  + ", SUM(CASE WHEN id_tipocalculo=3 and ifnull(capt.id_padron,0)>0 THEN 1 ELSE 0 END) AS cfijos"
            + ", SUM(CASE WHEN (SELECT id_padron FROM Lecturas AS C WHERE C.id_padron = P.id_padron ORDER BY fecha DESC LIMIT 1 )>0 THEN 1 ELSE 0 END) AS capturadas"
            + ", SUM(CASE WHEN (SELECT is_uploaded FROM Lecturas AS C WHERE C.id_padron = P.id_padron ORDER BY fecha DESC LIMIT 1 )>0 THEN 1 ELSE 0 END) AS enviadas"
            + " FROM " + CuentasContent.TABLE_NAME + " AS P"
            + " WHERE P.id_ruta="+idRuta.toString()
            + " GROUP BY sb,sector,id_ruta,ruta";
*/

    @Query("SELECT p.id_ruta as id, p.sb as sb, p.sector as sector, p.id_ruta as id_ruta, p.ruta as descripcion" +
            ", SUM(CASE WHEN p.id_tipocalculo=1 THEN 1 ELSE 0 END) AS medidos" +
            ", SUM(CASE WHEN p.id_tipocalculo=2 THEN 1 ELSE 0 END) AS promedios" +
            ", SUM(CASE WHEN p.id_tipocalculo=3 THEN 1 ELSE 0 END) AS fijos" +
            ", COUNT(*) AS registros" +
            ", SUM(CASE WHEN (SELECT id_padron FROM opr_lecturas AS C WHERE C.id_padron = P.id_padron ORDER BY fecha DESC LIMIT 1 )>0 THEN 1 ELSE 0 END) AS capturadas" +
            ", 0 AS enviadas" +
            ", 'Descargadas' AS observaciones" +
            " FROM Cat_Padron as P " +
            " WHERE P.sb=:sub and P.sector=:sec and P.id_ruta=:ruta" +
            " GROUP BY P.sb, P.sector, P.id_ruta, P.ruta")
    LiveData<List<Ruta>> getResumenByRuta(Integer sub, Integer sec, Integer ruta);

    @Query( " SELECT -1 as id, -1 as sb, -1 as sector, -1 as id_ruta, '-- SIN ESPECIFICAR --' as descripcion, 0 as medidos, 0 as promedios, 0 as fijos, 0 as registros, 0 AS capturadas, 0 AS enviadas, '' as observaciones " +
            " UNION " +
            " SELECT p.id_ruta as id, p.sb as sb, p.sector as sector, p.id_ruta as id_ruta, p.ruta as descripcion" +
            ", SUM(CASE WHEN p.id_tipocalculo=1 THEN 1 ELSE 0 END) AS medidos" +
            ", SUM(CASE WHEN p.id_tipocalculo=2 THEN 1 ELSE 0 END) AS promedios" +
            ", SUM(CASE WHEN p.id_tipocalculo=3 THEN 1 ELSE 0 END) AS fijos" +
            ", COUNT(*) AS registros" +
            ", SUM(CASE WHEN (SELECT id_padron FROM opr_lecturas AS C WHERE C.id_padron = P.id_padron ORDER BY fecha DESC LIMIT 1 )>0 THEN 1 ELSE 0 END) AS capturadas" +
            ", SUM(CASE WHEN (SELECT is_uploaded FROM Opr_Lecturas AS C WHERE C.id_padron = P.id_padron ORDER BY fecha DESC LIMIT 1 )>0 THEN 1 ELSE 0 END) AS enviadas" +
            ", 'Descargadas' AS observaciones" +
            " FROM Cat_Padron as P " +
            " GROUP BY P.sb, P.sector, P.id_ruta, P.ruta")
    LiveData<List<Ruta>> getResumenDeRutas();


    @Query("DELETE FROM Cat_Padron WHERE id_ruta=:idRuta")
    void deleteByID(Integer idRuta);

}
