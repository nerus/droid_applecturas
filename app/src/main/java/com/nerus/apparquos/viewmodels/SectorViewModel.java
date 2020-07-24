package com.nerus.apparquos.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Sector;
import com.nerus.apparquos.fragments.GetRutasFragment;
import com.nerus.apparquos.tasks.TasksCallBacks;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class SectorViewModel extends AndroidViewModel {
    private AppArquosRepository mRepository;
    private LiveData<List<Sector>> mSectores;
    public SectorViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((AppArquos) application).getRepository();
        LogSNE.d("NERUS","SectorViewModel.new " );
        //mRepository = new AppArquosRepository(application);
    }

    public LiveData<List<Sector>> getSectores(Integer sub) {
        //LogSNE.d("NERUS","SectorViewModel.getSectores " + Sub.toString());
        mSectores = mRepository.getSectoresBySb(sub);
        return mSectores;
    }

    public void downloadSectoresDelSb(Integer sb, TasksCallBacks listener){
        mRepository.downloadSectoresDelSb(sb, listener);
    }
}
