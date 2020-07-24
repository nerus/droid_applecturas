package com.nerus.apparquos;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.nerus.apparquos.entities.AppArquosDataBase;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Empleado;
import com.nerus.apparquos.entities.Empresa;
import com.nerus.apparquos.viewmodels.AnomaliaViewModel;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

public final class AppArquos extends Application  {
    private static AppArquos mAppInstance;
    private String mUrlApi = "";
    private String mTokenApp = "";
    private String mTokenUser = "";
    private Empleado mEmpleado = null;
    private Empresa mEmpresa = null;

    //private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        LogSNE.d("NERUS", "The AppArquos is created");
        //mAppExecutors = new AppExecutors();
        mAppInstance = this;
    }

    public AppArquosDataBase getDatabase() {
        return AppArquosDataBase.getDatabase (this);
    }

    public AppArquosRepository getRepository() {
        return AppArquosRepository.getRepository(this, getDatabase());
    }
    public static AppArquos getInstance(){
        if (mAppInstance == null) {
            synchronized (AppArquos.class) {
                if (mAppInstance == null) {
                    mAppInstance = new AppArquos();
                }
            }
        }
        return mAppInstance;
    }
    public String getUrlApi() {
        return mUrlApi;
    }

    public AppArquos setUrlApi(String urlApi) {
        mUrlApi = urlApi;
        return this;
    }

    public String getTokenApp() {
        return mTokenApp;
    }

    public AppArquos setTokenApp(String tokenApp) {
        mTokenApp = tokenApp;
        return this;
    }

    public String getTokenUser() {
        return mTokenUser;
    }

    public AppArquos setTokenUser(String tokenUser) {
        mTokenUser = tokenUser;
        return this;
    }

    public Empleado getEmpleado() {
        return mEmpleado;
    }

    public AppArquos setEmpleado(Empleado empleado) {
        mEmpleado = empleado;
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        mEmpresa = empresa;
    }

    public Empresa getEmpresa() {
        return mEmpresa;
    }
}