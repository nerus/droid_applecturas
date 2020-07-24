package com.nerus.apparquos.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.Ruta;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.tasks.TasksCallBacks;


import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public final class CuentaViewModel extends AndroidViewModel {
    private final AppArquosRepository mRepository;

    private LiveData<List<Cuenta>> mCuentas;
    private LiveData<List<Ruta>> mResumenRutas;
    private LiveData<Cuenta> mCuenta;
    private LiveData<List<Lectura>> mLecturas;

    public CuentaViewModel (Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
LogSNE.d("NERUS","CuentaViewModel.new " );
        //mRepository = new AppArquosRepository(application);
        mResumenRutas = new MutableLiveData<>();
        mCuentas = new MutableLiveData<>();
        mCuenta = new MutableLiveData<>();
        mLecturas = new MutableLiveData<>();
    }


    public LiveData<List<Cuenta>> getCuentasDeRuta(Integer sub, Integer sec, Integer ruta) {
        //LogSNE.d("NERUS","SectorViewModel.getSectores " + Sub.toString());
        mCuentas = mRepository.getCuentasByRuta(sub,sec,ruta);
        return mCuentas;
    }

    public LiveData<List<Ruta>> getRutas() {
        //LogSNE.d("NERUS","getRutas");
        mResumenRutas = mRepository.getResumenDeRutas();
        return mResumenRutas;
    }
    public LiveData<Cuenta> getCuentaByIdPadron(Integer idpadron) {
        LogSNE.d("NERUS","getCuentaByIdPadron");
        mCuenta = mRepository.getCuentaByIdPadron(idpadron);
        return mCuenta;
    }


    public void downloadCuentasDeRuta(Ruta rutaToDownLoad, Integer orderBy, TasksCallBacks listener){
        mRepository.downloadCuentasDeRuta(rutaToDownLoad, orderBy, listener);
    }


    public void deleteRuta(Ruta ruta) {
        mRepository.deleteRuta(ruta);
    }

    /*
    public void upLoadRuta(Ruta currentRuta) {
        mRepository.upLoadRuta(currentRuta);
    }
    */

    public LiveData<List<Lectura>> getLecturasDeRuta(Integer sb, Integer sector, Integer idRuta) {
        mLecturas = mRepository.getLecturasByRuta(sb,sector,idRuta);
        return mLecturas;

    }

    public void upLoadLectura(Lectura lectura, Usuario usuario, TasksCallBacks listener) {
        mRepository.upLoadLectura(lectura, usuario, listener);
    }

    public void setUpLoadedLecturas(List<Lectura> lecturas, TasksCallBacks listener) {
        mRepository.setUpLoadedLecturas(lecturas, listener);
    }

    public void saveLastRuta(Ruta ruta) {
        mRepository.saveLastRuta(ruta);
    }

    public void saveLastCuenta(Cuenta cuenta) {
        mRepository.saveLastCuenta(cuenta);
    }

    public void setCapturado(Cuenta cuenta) {
        mRepository.setCapturado(cuenta);
    }
}
