package com.nerus.apparquos.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Empresa;
import com.nerus.apparquos.entities.Parametro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParametroViewModel extends AndroidViewModel {

    private AppArquosRepository mRepository;
    //private LiveData<List<Parametro>> mParametros;
    private LiveData<Parametro> mTokenUser;
    private LiveData<Parametro> mEmpleadoLogged;
    private LiveData<Parametro> mLastRuta;
    private LiveData<Parametro> mLastCuenta;
    private LiveData<Parametro> mLastTrabajo;
    private LiveData<Parametro> mLastOrden;
    private LiveData<Parametro> mApiUrl;
    private LiveData<Parametro> mRFC;
    private LiveData<Parametro> mLicence;

    public ParametroViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();

        mTokenUser = new MutableLiveData<>();
        mLastRuta =  new MutableLiveData<>();
        mLastCuenta =  new MutableLiveData<>();
        mLastOrden =  new MutableLiveData<>();
        mApiUrl =  new MutableLiveData<>();
        mRFC =  new MutableLiveData<>();
        mLicence =  new MutableLiveData<>();
    }


    public LiveData<Parametro> getEmpleadoLogged(){
        mEmpleadoLogged = mRepository.getParametroByName("CURRENT_IDPERSONAL");
        return mEmpleadoLogged;
    }
    public LiveData<Parametro> getLastRuta(){
        mLastRuta = mRepository.getParametroByName("LAST_RUTA");
        return mLastRuta;
    }

    public LiveData<Parametro> getLastCuenta() {
        mLastCuenta = mRepository.getParametroByName("LAST_CUENTA");
        return mLastCuenta;
    }

    public LiveData<Parametro> getLastTrabajo() {
        mLastTrabajo = mRepository.getParametroByName("LAST_TRABAJO");
        return  mLastTrabajo;
    }

    public LiveData<Parametro> getLastOrden() {
        mLastOrden = mRepository.getParametroByName("LAST_ORDEN");
        return  mLastOrden;
    }

    public LiveData<Parametro> getApiUrl() {
        mApiUrl = mRepository.getParametroByName("API_URL");
        return mApiUrl;
    }
    public LiveData<Parametro> getRFC() {
        mRFC = mRepository.getParametroByName("RFC");
        return mRFC;
    }
    public LiveData<Parametro> getLicence() {
        mLicence= mRepository.getParametroByName("TOKEN_APP");
        return mLicence;
    }
    public void saveParametro(Parametro parametro) {
        mRepository.saveParametro(parametro);
    }
    public void saveParametros(List<Parametro> parametroList) {
        mRepository.saveParametros(parametroList);
    }


}
