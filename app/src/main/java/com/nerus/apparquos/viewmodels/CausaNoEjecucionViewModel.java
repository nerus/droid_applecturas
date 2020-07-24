package com.nerus.apparquos.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.CausaNoEjecucion;
import com.nerus.apparquos.entities.AppArquosRepository;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class CausaNoEjecucionViewModel extends AndroidViewModel {
    private AppArquosRepository mRepository;
    private LiveData<List<CausaNoEjecucion>> mCausas;
    public CausaNoEjecucionViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","CausaNoEjecucionViewModel.new " );
        //mRepository = new AppArquosRepository(application);
        mCausas = new MutableLiveData<>();
    }

    public LiveData<List<CausaNoEjecucion>> getCausas() {
        mCausas = mRepository.getAllCausas();
        return mCausas;
    }

    public void downloadCausasNoEjecucion(){
        mRepository.downloadCausasNoEjecucion();
    }
}
