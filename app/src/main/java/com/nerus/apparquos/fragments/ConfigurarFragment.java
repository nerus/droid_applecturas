package com.nerus.apparquos.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Empresa;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.tasks.EmpresaDownloadTask;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.ParametroViewModel;

import java.util.ArrayList;
import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

public final class ConfigurarFragment extends Fragment implements TasksCallBacks {
    private LayoutInflater mLayoutInflater;
    private TextView mUrl;
    private TextView mRFC;
    private TextView mLicence;

    private TextView mlblRFC;
    private TextView mlblRazonSocial;
    private TextView mlblDireccion;
    private TextView mlblFecha;


    private ViewModelProvider mViewModelProvider;
    private ParametroViewModel mParametroViewModel;
    private AppArquos mAppArquos;
    private Button mBtnValidar;
    private Boolean mswSaveAndExit = false;


    public static ConfigurarFragment newInstance(String usrName){
        Bundle args = new Bundle();
        args.putString("USERNAME",usrName);
        ConfigurarFragment fragment = new ConfigurarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mAppArquos = (AppArquos) getActivity().getApplicationContext();
        mViewModelProvider = ViewModelProviders.of(this);


        mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayoutInflater = inflater;
        final View v = mLayoutInflater.inflate(R.layout.configurar_fragment, container, false);

        mUrl = v.findViewById(R.id.txtURL);
        mRFC = v.findViewById(R.id.txtRFC);
        mLicence = v.findViewById(R.id.txtLicence);
        mBtnValidar = v.findViewById(R.id.cmdValidar);

        mlblRFC = v.findViewById(R.id.lblRFC);
        mlblRazonSocial = v.findViewById(R.id.lblRazonSocial);
        mlblDireccion = v.findViewById(R.id.lblDireccion);
        mlblFecha = v.findViewById(R.id.lblFecha);

        mBtnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_cmdValidar(v);

            }
        });

        mUrl.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus){
                    // TODO: the editText has just been left
                    LogSNE.d("NERUS", "onFocusChange: " + mUrl.getText().toString());
                }

            }
        });
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();

        clsUtilities.setHomeBack(getActivity(),"Configurar...");
        //todo DEJAR AL ULTIMO EL RFC AL CUAL SE ESTA COMPILANDO

        mUrl.setText("http://192.168.1.1:2506/api");  //CAEV ISLA
        mRFC.setText("CAE0106293R4");

        mUrl.setText("http://192.168.1.1:2506/api");  //CAEV ISLA
        mRFC.setText("CAE0106293R4");


        mUrl.setText("http://192.168.1.1:2506/api");  //BANDERILLA
        mRFC.setText("CMA0601166V1");

        mUrl.setText("http://192.168.1.200:2506/api");  //COATEPEC
        mRFC.setText("CMA020822651");

        mUrl.setText("http://192.168.2.4:2506/api");  //SIMAS RC
        mRFC.setText("SIA930831P15");

        mUrl.setText("http://192.168.1.1:2506/api");  //CAEV MTZ DE LA TORRE
        mRFC.setText("CAE0106293R4");

        mUrl.setText("http://nerus.sytes.net:2506/api");  //SERVER NERUS
        mRFC.setText("OOM980122HN0");

        this.setObservable();


    }



    @Override
    public void onPause() {
        super.onPause();
        LogSNE.d("NERUS","ON PAUSE: " + mUrl.getText().toString());

        //mParametroViewModel.saveParametro(url);
        //mParametroViewModel.saveParametro(rfc);
        //mParametroViewModel.saveParametro(licence);



    }
    private void onClick_cmdValidar(View v){
        mswSaveAndExit=true;
        clsUtilities.hideKeyboard(getActivity());
        getEmpresa(mUrl.getText().toString());
    }
    private void setObservable() {
        mParametroViewModel.getRFC().observe(this, new Observer<Parametro>() {
            @Override
            public void onChanged(@Nullable final Parametro rfc) {
                // Update the cached copy of.
                if (rfc!=null){
                    mRFC.setText(rfc.getValor());
                }
            }
        });
        mParametroViewModel.getApiUrl().observe(this, new Observer<Parametro>() {
            @Override
            public void onChanged(@Nullable final Parametro url) {
                // Update the cached copy of.
                if (url!=null){
                    mUrl.setText(url.getValor());
                    getEmpresa(mUrl.getText().toString());
                }
            }
        });
        mParametroViewModel.getLicence().observe(this, new Observer<Parametro>() {
            @Override
            public void onChanged(@Nullable final Parametro lic) {
                // Update the cached copy of.
                if (lic != null) {
                    mLicence.setText(lic.getValor());
                }
            }
        });

    }

    private void getEmpresa(String sUrl){
        EmpresaDownloadTask empresaDownloadTask = new EmpresaDownloadTask(sUrl,mAppArquos.getTokenApp(),mAppArquos.getTokenUser());
        empresaDownloadTask.setCallBacks(this);
        empresaDownloadTask.execute();
    }

    @Override
    public void onRequestBeforeStart(String fromTask) {

    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {

    }

    @Override
    public void onRequestError(String fromTask, Exception error) {
        mUrl.setError("URL no valida.");
        mlblFecha.setText("SIN CONEXION");
    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {

    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        if (response instanceof Empresa) {
            Empresa empresa = (Empresa) response;
            if (mRFC.getText().toString().trim().equals(empresa.getRFC())) {
                mlblDireccion.setText(empresa.getDireccion());
                mlblRazonSocial.setText(empresa.getDescripcion());
                mlblRFC.setText(empresa.getRFC());
                if (mswSaveAndExit) saveConfiguracion();
            }else{
                mRFC.setError("RFC no autorizado.");
            }
            mlblFecha.setText(empresa.getFecha());
        }
    }

    private void saveConfiguracion() {

            String cValidar =  clsFecha.getSHA1(mRFC.getText().toString().trim()+getString(R.string.appkey));
            mLicence.setText(cValidar);
            Parametro url = new Parametro("API_URL",mUrl.getText().toString(),"URL de la Api de Datos","http://192.168.1.1:2506/api","");
            Parametro rfc = new Parametro("RFC",mRFC.getText().toString().trim(),"RFC del Organismo Operador","CMA0601166V1","");
            Parametro licence = new Parametro("TOKEN_APP",mLicence.getText().toString(),"TOKEN DE LICENCIA DE USO","","");
            List<Parametro> list = new ArrayList();
            list.add(url);
            list.add(rfc);
            list.add(licence);
            mParametroViewModel.saveParametros(list);
            getActivity().getSupportFragmentManager().popBackStack();

    }
}
