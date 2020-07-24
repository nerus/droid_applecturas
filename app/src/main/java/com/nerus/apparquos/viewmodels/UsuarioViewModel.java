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
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.tasks.TasksCallBacks;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class UsuarioViewModel extends AndroidViewModel  {
    private AppArquosRepository mRepository;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","UsuarioViewModel.new " );
    }

    public LiveData<Usuario> getUsuario() {
        return  mRepository.getUsuario();
    }


    public void getAuthorization(String usuario, String validar, TasksCallBacks listener) {
        mRepository.getUserArquos(usuario,validar, listener);
    }
    /*public void getRemoveAuth() {
        mRepository.delete_AllUsuarios();
    }*/


}
