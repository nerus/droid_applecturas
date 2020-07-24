package com.nerus.apparquos.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Empleado;
import com.nerus.apparquos.tasks.TasksCallBacks;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class EmpleadoViewModel extends AndroidViewModel {
    private AppArquosRepository mRepository;
    //private LiveData<List<Empleado>> mEmpleados;

    public EmpleadoViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","EmpleadoViewModel.new " );
    }

    public LiveData<List<Empleado>> getEmpleados() {
        return mRepository.getAllEmpleados();
    }
    public LiveData<Empleado> getEmpleadoLogged() {
        return mRepository.getEmpleadoLogged();
    }

    public void downloadEmpleados(TasksCallBacks listener){
        mRepository.downloadEmpleados(listener);
    }

    public void getAuthorization(Empleado empleado, String validar, TasksCallBacks listener) {
        mRepository.getAuthorization(empleado.getIdPersonal(),validar, listener);
    }
}
