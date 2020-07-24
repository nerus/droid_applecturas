package com.nerus.apparquos.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Lectura;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class LecturaViewModel extends AndroidViewModel {
    private AppArquosRepository mRepository;
    //private LiveData<List<Lectura>> mLectura;
    public LecturaViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","LecturaViewModel.new " );
        //mRepository = new AppArquosRepository(application);
        //mLectura = new MutableLiveData<>();
    }

    public LiveData<List<Lectura>> getLecturaByIdPadron(Integer idpadron) {
        return mRepository.getLecturaByIdPadron(idpadron);
        //mLectura = mRepository.getLecturaByIdPadron(idpadron);
        //return mLectura;
    }

    public void insert(Lectura lectura) {
        mRepository.insert_lectura(lectura);
    }

    public void update(Lectura lectura) {
        mRepository.update_lectura(lectura);
    }
}
