package com.nerus.apparquos.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.Anomalia;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Ruta;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class AnomaliaViewModel extends AndroidViewModel {
    private AppArquosRepository mRepository;
    //private LiveData<List<Anomalia>> mAnomalias;
    public AnomaliaViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","AnomaliaViewModel.new " );
        //mRepository = new AppArquosRepository(application);
        //mAnomalias = new MutableLiveData<>();
    }

    public LiveData<List<Anomalia>> getAnomalias() {
        return mRepository.getAllAnomalias();
        //mAnomalias = mRepository.getAllAnomalias();
        //return mAnomalias;
    }

    public void downloadAnomalias(){
        mRepository.downloadAnomalias();
    }
}
