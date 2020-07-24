package com.nerus.apparquos.entities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nerus.apparquos.daos.AnomaliaDAO;
import com.nerus.apparquos.daos.CausaNoEjecucionDAO;
import com.nerus.apparquos.daos.CuentaDAO;
import com.nerus.apparquos.daos.EmpleadoDAO;
import com.nerus.apparquos.daos.LecturaDAO;
import com.nerus.apparquos.daos.MaterialCapturadoDAO;
import com.nerus.apparquos.daos.MaterialDAO;
import com.nerus.apparquos.daos.OrdenCerradaDAO;
import com.nerus.apparquos.daos.OrdenDAO;
import com.nerus.apparquos.daos.ParametroDAO;
import com.nerus.apparquos.daos.RutaDAO;
import com.nerus.apparquos.daos.SectorDAO;
import com.nerus.apparquos.daos.SubsistemaDAO;
import com.nerus.apparquos.daos.UsuarioDAO;
import com.nerus.apparquos.viewmodels.OrdenViewModel;

@Database(entities = {Usuario.class, Cuenta.class, Subsistema.class, Sector.class, Ruta.class, Anomalia.class, Lectura.class, Empleado.class, Parametro.class, Bitacora.class, Orden.class, OrdenCerrada.class, CausaNoEjecucion.class, Material.class, MaterialCapturado.class}, version = 1)
public abstract class AppArquosDataBase extends RoomDatabase {
    public abstract UsuarioDAO mUsuarioDAO();
    public abstract CuentaDAO mCuentaDAO();
    public abstract SubsistemaDAO mSubsistemaDAO();
    public abstract SectorDAO mSectorDAO();
    public abstract RutaDAO mRutaDAO();
    public abstract AnomaliaDAO mAnomaliaDAO();
    public abstract LecturaDAO mLecturaDAO();
    public abstract EmpleadoDAO mEmpleadoDAO();
    public abstract ParametroDAO mParametroDAO();
    public abstract OrdenDAO mOrdenDAO();
    public abstract CausaNoEjecucionDAO mCausaNoEjecucionDAO();
    public abstract OrdenCerradaDAO mOrdenCerradaDAO();
    public abstract MaterialDAO mMaterialDAO();
    public abstract MaterialCapturadoDAO mMaterialCapturadoDAO();

    private static volatile AppArquosDataBase INSTANCE;

    public static AppArquosDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppArquosDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppArquosDataBase.class, "AppArquosDataBase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
