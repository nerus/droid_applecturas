package com.nerus.apparquos.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Material;
import com.nerus.apparquos.entities.MaterialCapturado;
import com.nerus.apparquos.fragments.UpLoadOrdenesFragment;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
import com.nerus.apparquos.tasks.TasksCallBacks;

import java.util.List;

import javax.security.auth.callback.Callback;

public class MaterialViewModel extends AndroidViewModel {
    private AppArquosRepository mRepository;

    public MaterialViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","CausaNoEjecucionViewModel.new " );

    }

    public LiveData<List<Material>> getMateriales() {
        return  mRepository.getAllMateriales();
    }

    public void downloadMateriales(TasksCallBacks listener){
        mRepository.downloadMateriales(listener);
    }

    public LiveData<Material> getMaterial(String id) {
        return mRepository.getMaterialById(id);
    }
    public LiveData<List<MaterialCapturado>> getMaterialesByIdOrden(String idOrden) {
        return mRepository.getMaterialesByIdOrden(idOrden);
    }
    public LiveData<List<MaterialCapturado>> getMaterialUsado(Integer idPersonal) {
        return mRepository.getMaterialUsado(idPersonal);
    }
    public void insert(MaterialCapturado item) {
        mRepository.insertMatCapturado(item);
    }

    public void delete(MaterialCapturado item) {
        mRepository.deleteMatCapturado(item);
    }


    public void upLoadMateriales(String xmlToSend, Integer idPersonal, String idUsuario, TasksCallBacks listener) {
        mRepository.upLoadMateriales(xmlToSend,idPersonal,idUsuario,listener);
    }
}
