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
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.entities.OrdenCerrada;
import com.nerus.apparquos.entities.Trabajo;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.fragments.UpLoadOrdenesFragment;
import com.nerus.apparquos.tasks.TasksCallBacks;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

import javax.security.auth.callback.Callback;

public class OrdenViewModel extends AndroidViewModel {
    private AppArquosRepository mRepository;
    //private LiveData<List<Trabajo>> mResumen;
    //private LiveData<List<Trabajo>> mGroupByColonia;

    public OrdenViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","OrdenViewModel.new " );

    }

    public LiveData<List<Trabajo>> getResumenByTrabajo(Integer idEmpleado) {
        return mRepository.getResumenByTrabajo(idEmpleado);
    }
    /*
    public LiveData<List<Trabajo>> getResumenByColonia(Integer idEmpleado) {
        mGroupByColonia = mRepository.getResumenByColonia(idEmpleado);
        return mGroupByColonia;
    }
    */
    public void downloadOrdenes(Integer idEmpleado, TasksCallBacks listener){
        mRepository.downloadOrdenes(idEmpleado, listener);
    }

    public LiveData<List<Orden>> getOrdenes(Integer idEmpleado) {
        return mRepository.getOrdenes(idEmpleado);
    }


    public LiveData<List<Orden>> getOrdenesByTrabajo(Integer idEmpleado, Integer idTrabajo) {
        return mRepository.getOrdenesByTrabajo(idEmpleado, idTrabajo);
    }

    public LiveData<List<Orden>> getOrdenesByColonia(Integer idEmpleado, String pob_colonia) {
        return mRepository.getOrdenesByColonia(idEmpleado, pob_colonia);
    }

    public LiveData<OrdenCerrada> getOrdenCerrada(String idOrden) {
        return mRepository.getOrdenCerrada(idOrden);
    }
    public LiveData<List<OrdenCerrada>> getOrdenesCerradas(Integer idPersonal) {
        return mRepository.getOrdenesCerradas(idPersonal);
    }
    public void insertOTCerrada(OrdenCerrada ordenCerrada) {
        mRepository.insertOTCerrada(ordenCerrada);
    }

    public void updateOTCerrada(OrdenCerrada ordenCerrada) {
        mRepository.updateOTCerrada(ordenCerrada);
    }

    public void setCapturado(Orden orden) {
        mRepository.setCapturado(orden);
    }


    public void upLoadOrdenes(String xmlToSend, Integer idPersonal, String idUsuario, TasksCallBacks listener) {
        mRepository.upLoadOrdenes(xmlToSend,idPersonal,idUsuario,listener);
    }

    public void setOTEnviadas(List<Orden> otEnviadas, TasksCallBacks listener) {
        mRepository.setOTEnviadas(otEnviadas,listener);
    }
}
