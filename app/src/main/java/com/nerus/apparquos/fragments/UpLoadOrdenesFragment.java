package com.nerus.apparquos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.MaterialCapturado;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.entities.OrdenCerrada;
import com.nerus.apparquos.entities.Ruta;
import com.nerus.apparquos.entities.Trabajo;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

import com.nerus.apparquos.tasks.LecturaUpLoadTask;
import com.nerus.apparquos.tasks.MaterialFileCsvTask;
import com.nerus.apparquos.tasks.MaterialUpLoadTask;
import com.nerus.apparquos.tasks.OrdenCRUDTask;
import com.nerus.apparquos.tasks.OrdenFileCsvTask;
import com.nerus.apparquos.tasks.OrdenUpLoadTask;
import com.nerus.apparquos.tasks.TasksCallBacks;

import com.nerus.apparquos.viewmodels.MaterialViewModel;
import com.nerus.apparquos.viewmodels.OrdenViewModel;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public final class UpLoadOrdenesFragment extends Fragment implements TasksCallBacks {
    private ViewModelProvider mViewModelProvider;
    private OrdenViewModel mOrdenViewModel;
    private MaterialViewModel mMaterialViewModel;
    //private LecturaViewModel mLecturaViewModel;

    private List<OrdenCerrada> mOrdenCerradaList;

    private LayoutInflater mLayoutInflater;
    private Usuario mUsuario;
    private Trabajo mTrabajo;
    private AppArquos mAppArquos;
    private TextView txtMsg;
    private List<Orden> mOTEnviadas;
    private List<MaterialCapturado> mMaterialCapturadoList;
    private List<MaterialCapturado> mMaterialEnviado;
    private boolean finishTask=false;

    /*
    public static UpLoadOrdenesFragment newInstance(String usrName){
        Bundle args = new Bundle();
        args.putString("USERNAME",usrName);
        UpLoadOrdenesFragment fragment = new UpLoadOrdenesFragment();
        fragment.setArguments(args);
        return fragment;
    }
    */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mTrabajo = (Trabajo) args.getSerializable("TRABAJO");
        mUsuario = (Usuario) args.getSerializable("USUARIO");
        mViewModelProvider = ViewModelProviders.of(this);
        mOrdenViewModel = mViewModelProvider.get(OrdenViewModel.class);
        mMaterialViewModel = mViewModelProvider.get(MaterialViewModel.class);
        mAppArquos = (AppArquos) getActivity().getApplicationContext();

    }

    @Override
    public void onStart() {
        super.onStart();

        clsUtilities.setHomeBack(getActivity(),"Subiendo Ordenes de Trabajo.");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        //int width = displayMetrics.widthPixels;

        nHeight  = (int) (displayMetrics.heightPixels * 0.70); // ((LinearLayout) getView().findViewById(R.id.lyContainer)) .getHeight() ;

        mOTEnviadas = null;
        mMaterialEnviado = null;

        this.setObservable();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayoutInflater = inflater;
        final View v = mLayoutInflater.inflate(R.layout.uploadlecturas_fragment, container, false);

        ((TextView) v.findViewById(R.id.lblRuta)).setText( "SUBIR ORDENES\r\n" + mTrabajo.getCapturadas().toString() +" REGISTROS" );
        ((TextView) v.findViewById(R.id.cmdProgressbar)).setText("0%");

        txtMsg = v.findViewById(R.id.txtMsg);

        txtMsg.setText("");
        txtMsg.setVisibility(View.GONE);

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
    private void upLoad_listOrdenes(List<OrdenCerrada> list) {
        mOrdenCerradaList = list;
        if (mOrdenCerradaList!=null && mOrdenCerradaList.size()>0){
            nTot = mOrdenCerradaList.size();
            nCnt = 0;
            LogSNE.d("NERUS","upLoad_listCuentas. " + mOrdenCerradaList.toString());

            String cSubCarpeta = clsFecha.getFechaYMD().substring(0,6);
            //String cPref = String.format("%02d", mRuta.getSb()) + String.format("%02d",  mRuta.getSector()) + String.format("%03d",  mRuta.getIdRuta())+"_UP";
            File csvFile = clsUtilities.getOutputCSVFile("ORDENES",cSubCarpeta,"ORDENES");
            Uri fileUri = clsUtilities.getUriFromFile(getContext(), csvFile);
            //LogSNE.d("NERUS","csvFile. " + csvFile.toString());

            OrdenFileCsvTask fileCsvTask = new OrdenFileCsvTask(csvFile, fileUri, mOrdenCerradaList);
            fileCsvTask.setCallBacks(this);
            fileCsvTask.execute();
        }
    }
    private void upLoad_listMateriales(List<MaterialCapturado> list) {
        mMaterialCapturadoList = list;
        if (mMaterialCapturadoList!=null && mMaterialCapturadoList.size()>0){
            nTot = mMaterialCapturadoList.size();
            nCnt = 0;
            LogSNE.d("NERUS","upLoad_listMateriales. " + mMaterialCapturadoList.toString());

            String cSubCarpeta = clsFecha.getFechaYMD().substring(0,6);
            //String cPref = String.format("%02d", mRuta.getSb()) + String.format("%02d",  mRuta.getSector()) + String.format("%03d",  mRuta.getIdRuta())+"_UP";
            File csvFile = clsUtilities.getOutputCSVFile("MATERIALES",cSubCarpeta,"MATERIAL");
            Uri fileUri = clsUtilities.getUriFromFile(getContext(), csvFile);
            //LogSNE.d("NERUS","csvFile. " + csvFile.toString());

            MaterialFileCsvTask fileCsvTask = new MaterialFileCsvTask(csvFile, fileUri, mMaterialCapturadoList);
            fileCsvTask.setCallBacks(this);
            fileCsvTask.execute();
        }
    }

    public void setObservable() {
        mMaterialViewModel.getMaterialUsado( mAppArquos.getEmpleado().getIdPersonal() ).observe(this, new Observer<List<MaterialCapturado>>() {
            @Override
            public void onChanged(@Nullable final List<MaterialCapturado> list) {
                // Update the cached copy of the subsistemas in the adapter.
                //LogSNE.d("NERUS","getCuentasDeRuta.onChanged " + cuentas.get(0).toString());
                upLoad_listMateriales(list);
            }
        });
        mOrdenViewModel.getOrdenesCerradas( mAppArquos.getEmpleado().getIdPersonal() ).observe(this, new Observer<List<OrdenCerrada>>() {
            @Override
            public void onChanged(@Nullable final List<OrdenCerrada> list) {
                // Update the cached copy of the subsistemas in the adapter.
                //LogSNE.d("NERUS","getCuentasDeRuta.onChanged " + cuentas.get(0).toString());
                upLoad_listOrdenes(list);
            }
        });
    }

    @Override
    public void onRequestBeforeStart(String fromTask) {
        txtMsg.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = cmdProgress.getLayoutParams();
        params.height = 10;
        //cmdProgress.setHeight(10);
        cmdProgress.setText("0%");
        cmdProgress.setLayoutParams(params);

        if (fromTask.equals(OrdenFileCsvTask.TASK_NAME)){
            txtMsg.setText("GENERANDO ARCHIVO CSV DE ORDENES");
        }
        if (fromTask.equals(OrdenUpLoadTask.TASK_NAME)){
            txtMsg.setText("ENVIANDO ORDENES");
        }
        if (fromTask.equals(MaterialFileCsvTask.TASK_NAME)){
            txtMsg.setText("GENERANDO ARCHIVO CSV DE MATERIALES");
        }
        if (fromTask.equals(MaterialUpLoadTask.TASK_NAME)){
            txtMsg.setText("ENVIANDO MATERIALES");
        }
        if (fromTask.equals(OrdenCRUDTask.TASK_NAME)){
            txtMsg.setText("CAMBIANDO A ENVIADAS");
        }

    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {
        nCnt = progress[0];
        nTot = progress[1];
        Double nPer = Double.valueOf(nCnt/nTot*100);
        ViewGroup.LayoutParams params = cmdProgress.getLayoutParams();
        Integer nH = (nCnt+1)*nHeight / nTot;
        params.height = nH;
        cmdProgress.setText(nPer.toString()+ "%");
        cmdProgress.setLayoutParams(params);
        if (fromTask.equals(OrdenFileCsvTask.TASK_NAME)){
            txtMsg.setText("GENERANDO ARCHIVO CSV DE ORDENES");
        }
        if (fromTask.equals(OrdenUpLoadTask.TASK_NAME)){
            txtMsg.setText("ENVIANDO ORDENES");
        }
        if (fromTask.equals(MaterialFileCsvTask.TASK_NAME)){
            txtMsg.setText("GENERANDO ARCHIVO CSV DE MATERIALES");
        }
        if (fromTask.equals(MaterialUpLoadTask.TASK_NAME)){
            txtMsg.setText("ENVIANDO MATERIALES");
        }
        if (fromTask.equals(OrdenCRUDTask.TASK_NAME)){
            txtMsg.setText("CAMBIANDO A ENVIADAS");
        }
    }

    @Override
    public void onRequestError(String fromTask, Exception error) {

    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {

    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        if (fromTask.equals(OrdenFileCsvTask.TASK_NAME)){
            String xmlToSend = (String) response;
            Integer idPersonal = mAppArquos.getEmpleado().getIdPersonal();
            mOrdenViewModel.upLoadOrdenes(xmlToSend, idPersonal, mUsuario.getIdUsuario(), this);
        }
        if (fromTask.equals(OrdenUpLoadTask.TASK_NAME)){

            mOTEnviadas = (List<Orden>) response;
            mOrdenViewModel.setOTEnviadas(mOTEnviadas, this);


            /*
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
            */
        }
        if (fromTask.equals(MaterialFileCsvTask.TASK_NAME)){
            String xmlToSend = (String) response;
            Integer idPersonal = mAppArquos.getEmpleado().getIdPersonal();
            mMaterialViewModel.upLoadMateriales(xmlToSend, idPersonal, mUsuario.getIdUsuario(), this);

        }
        if (fromTask.equals(MaterialUpLoadTask.TASK_NAME)){
            mMaterialEnviado = (List<MaterialCapturado>) response;
            sendResult(Activity.RESULT_OK);
        }
        if (fromTask.equals(OrdenCRUDTask.TASK_NAME)){
            finishTask = true;
            sendResult(Activity.RESULT_OK);
        }

    }
    private void sendResult( int resultCode){
        LogSNE.d("NERUS","Enviando result. " + nCnt.toString());
        if (mMaterialEnviado!=null && mOTEnviadas!=null && finishTask){
            if (getTargetFragment()==null){
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("ORDENES", (Serializable) mOTEnviadas);
            getTargetFragment().onActivityResult(clsRequestCodes.REQUEST_UPLOADLECTURAS , resultCode, intent);
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
                boolean done = getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        }

    }
}
