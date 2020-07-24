package com.nerus.apparquos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.CuentaAdapter;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.Ruta;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.tasks.LecturaFileCsvTask;
import com.nerus.apparquos.tasks.LecturaUpLoadTask;
import com.nerus.apparquos.tasks.SetUpLoadedLecturasTask;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.CuentaViewModel;
import com.nerus.apparquos.viewmodels.LecturaViewModel;

import java.io.File;
import java.io.Serializable;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeoutException;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public final class UpLoadLecturasFragment extends Fragment implements TasksCallBacks {
    private ViewModelProvider mViewModelProvider;
    private CuentaViewModel mCuentaViewModel;
    private LecturaViewModel mLecturaViewModel;

    private List<Lectura> mLecturaList;
//    private List<Lectura> mLecturasToupLoad;
//    private Lectura currentLectura;
//    private Integer currentPosition=0;
    private LayoutInflater mLayoutInflater;
    private Ruta mRuta;
    private Usuario mUsuario;

    public static UpLoadLecturasFragment newInstance(String usrName){
        Bundle args = new Bundle();
        args.putString("USERNAME",usrName);
        UpLoadLecturasFragment fragment = new UpLoadLecturasFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mRuta = (Ruta) args.getSerializable("RUTA");
        mUsuario = (Usuario) args.getSerializable("USUARIO");
        mViewModelProvider = ViewModelProviders.of(this);
        mCuentaViewModel = mViewModelProvider.get(CuentaViewModel.class);
        mLecturaViewModel = mViewModelProvider.get(LecturaViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();

        clsUtilities.setHomeBack(getActivity(),"Subiendo Ruta " + mRuta.getDescripcion() );
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        //int width = displayMetrics.widthPixels;

        nHeight  = (int) (displayMetrics.heightPixels * 0.70); // ((LinearLayout) getView().findViewById(R.id.lyContainer)) .getHeight() ;
        this.setObservable();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayoutInflater = inflater;
        final View v = mLayoutInflater.inflate(R.layout.uploadlecturas_fragment, container, false);

        ((TextView) v.findViewById(R.id.lblRuta)).setText(mRuta.getDescripcion() + "\r\n" + mRuta.getCapturadas().toString() +" REGISTROS" );
        ((TextView) v.findViewById(R.id.cmdProgressbar)).setText("0%");
        ((TextView) v.findViewById(R.id.txtMsg)).setText("");
        ((TextView) v.findViewById(R.id.txtMsg)).setVisibility(View.GONE);

        cmdProgress = v.findViewById(R.id.cmdProgressbar);
        ViewGroup.LayoutParams params = cmdProgress.getLayoutParams();
        params.height = 10;
        //cmdProgress.setHeight(10);
        cmdProgress.setText("0%");
        cmdProgress.setLayoutParams(params);

        //ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.content_send_ruta);

        return v;
    }

    private static Integer nHeight, nTot, nCnt;
    private TextView cmdProgress;
    private void upLoad_listCuentas(List<Lectura> lecturas) {
        mLecturaList = lecturas;
        if (mLecturaList!=null && mLecturaList.size()>0){
            nTot = mLecturaList.size();
            nCnt = 0;
            LogSNE.d("NERUS","upLoad_listCuentas. " + mLecturaList.toString());
            Lectura lectura = mLecturaList.get(nCnt);
            mCuentaViewModel.upLoadLectura(lectura, mUsuario, this);


            String cSubCarpeta = clsFecha.getFechaYMD().substring(0,6);
            String cPref = String.format("%02d", mRuta.getSb()) + String.format("%02d",  mRuta.getSector()) + String.format("%03d",  mRuta.getIdRuta())+"_UP";
            File csvFile = clsUtilities.getOutputCSVFile("LECTURAS",cSubCarpeta,cPref);
            Uri fileUri = clsUtilities.getUriFromFile(getContext(), csvFile);
            //LogSNE.d("NERUS","csvFile. " + csvFile.toString());

            LecturaFileCsvTask lecturaFileCsvTask = new LecturaFileCsvTask(csvFile, fileUri, mLecturaList);
            lecturaFileCsvTask.setCallBacks(new TasksCallBacks() {
                @Override
                public void onRequestBeforeStart(String fromTask) {
                }

                @Override
                public void onProgressUpdate(String fromTask, Integer... progress) {
                }

                @Override
                public void onRequestError(String fromTask, Exception error) {
                }

                @Override
                public void onRequestCancel(String fromTask, Exception error) {
                }

                @Override
                public void onRequestSuccess(String fromTask, Object response) {
                }
            });
            lecturaFileCsvTask.execute();
        }



    }
    public void setObservable() {
        mCuentaViewModel.getLecturasDeRuta(mRuta.getSb(),mRuta.getSector(),mRuta.getIdRuta()).observe(this, new Observer<List<Lectura>>() {
            @Override
            public void onChanged(@Nullable final List<Lectura> lecturas) {
                // Update the cached copy of the subsistemas in the adapter.
                //LogSNE.d("NERUS","getCuentasDeRuta.onChanged " + cuentas.get(0).toString());
                upLoad_listCuentas(lecturas);
            }
        });
    }

    @Override
    public void onRequestBeforeStart(String fromTask) {

    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {

    }

    @Override
    public void onRequestError(String fromTask, Exception error) {

    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {

    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        if (fromTask.equals(LecturaUpLoadTask.TASK_NAME)){
            Double nPer = Double.valueOf(nCnt/nTot*100);
            ViewGroup.LayoutParams params = cmdProgress.getLayoutParams();
            Integer nH = (nCnt+1)*nHeight / nTot;
            params.height = nH;
            cmdProgress.setText(nPer.toString()+ "%");
            //cmdProgress.setHeight(nH);
            cmdProgress.setLayoutParams(params);
            LogSNE.d("NERUS","termino el ciclo. " + response.toString());
            if (nCnt<nTot){
                Lectura lectura = mLecturaList.get(nCnt);
                nCnt=nCnt+1;
                mCuentaViewModel.upLoadLectura(lectura, mUsuario, this);
            }else{
                LogSNE.d("NERUS","termino el ciclo. " + nCnt.toString());
                //mCuentaViewModel.setUpLoadedLecturas(mLecturaList,this);
                //(getView().findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                sendResult(Activity.RESULT_OK);
                //getActivity().getSupportFragmentManager().popBackStack();
                return;
            }
            LogSNE.d("NERUS","ren. " + nCnt.toString());
            LogSNE.d("NERUS","height. " + nHeight.toString());

        }
    }
    private void sendResult( int resultCode){
        if (getTargetFragment()==null){
            return;
        }
        LogSNE.d("NERUS","Enviando result. " + nCnt.toString());
        Intent intent = new Intent();
        intent.putExtra("LECTURAS", (Serializable) mLecturaList);
        getTargetFragment().onActivityResult(clsRequestCodes.REQUEST_UPLOADLECTURAS , resultCode, intent);
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
            boolean done = getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }
}
