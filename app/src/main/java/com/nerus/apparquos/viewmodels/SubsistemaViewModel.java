package com.nerus.apparquos.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Subsistema;
import com.nerus.apparquos.fragments.GetRutasFragment;
import com.nerus.apparquos.tasks.TasksCallBacks;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class SubsistemaViewModel extends AndroidViewModel {
    private AppArquosRepository mRepository;
    private LiveData<List<Subsistema>> mAllSubsitemas;
    public SubsistemaViewModel(@NonNull Application application) {
        super(application);

        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","SubsistemaViewModel.new " );
        //mRepository = new AppArquosRepository(application);
        mAllSubsitemas = new MutableLiveData<>();

    }

   public LiveData<List<Subsistema>> getAllSubsitemas() {
        mAllSubsitemas = mRepository.getAllSubsistemas();
        return mAllSubsitemas;
    }
    public void downloadSubsistemas(TasksCallBacks listener){
        mRepository.downloadSubsistemas(listener);
    }

    public void downloadOrderBy(TasksCallBacks listener) {
        mRepository.downloadOrderBy(listener);
    }
    /*public void insert(Subsistema sb) { mRepository.insert(sb); }*/
}
