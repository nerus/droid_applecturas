package com.nerus.apparquos.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Ruta;
import com.nerus.apparquos.tasks.TasksCallBacks;


import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class RutaViewModel extends AndroidViewModel {
    private AppArquosRepository mRepository;
    //private LiveData<List<Ruta>> mRutas;
    public RutaViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","RutaViewModel.new " );
        //mRepository = new AppArquosRepository(application);
    }

    public LiveData<List<Ruta>> getRutas(Integer sub, Integer sec) {
        //LogSNE.d("NERUS","SectorViewModel.getSectores " + Sub.toString());
        return  mRepository.getRutasBySbSec(sub,sec);
    }

    public void downloadRutasDelSbySec(Integer sub, Integer sec, TasksCallBacks listener){
        mRepository.downloadRutasDelSbySec(sub,sec, listener);
    }
}
