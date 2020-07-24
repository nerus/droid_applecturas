package com.nerus.apparquos.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Update;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.OrderByAdapter;
import com.nerus.apparquos.adapters.RutaAdapter;
import com.nerus.apparquos.adapters.SectorAdapter;
import com.nerus.apparquos.adapters.SubsistemaAdapter;

import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.OrderBy;
import com.nerus.apparquos.entities.Ruta;
import com.nerus.apparquos.entities.Sector;
import com.nerus.apparquos.entities.Subsistema;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.tasks.CuentasDownloadTask;
import com.nerus.apparquos.tasks.OrderByDownloadTask;
import com.nerus.apparquos.tasks.RutasDownloadTask;
import com.nerus.apparquos.tasks.SectoresDownloadTask;
import com.nerus.apparquos.tasks.SubsistemasDownloadTask;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.CuentaViewModel;
import com.nerus.apparquos.viewmodels.RutaViewModel;
import com.nerus.apparquos.viewmodels.SectorViewModel;
import com.nerus.apparquos.viewmodels.SubsistemaViewModel;

import java.util.ArrayList;
import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class GetRutasFragment extends Fragment implements AdapterView.OnItemSelectedListener, TasksCallBacks {
    private ViewModelProvider mViewModelProvider;
    private SubsistemaViewModel mSubViewModel;
    private SectorViewModel mSecViewModel;
    private RutaViewModel mRutaViewModel;
    private CuentaViewModel mCuentaViewModel;

    private List<Ruta> mRutaList;
    private Ruta currentRuta;
    private SubsistemaAdapter mSubsistemaAdapter;
    private SectorAdapter mSectorAdapter;
    private RutaAdapter mRutaAdapter;
    private OrderByAdapter mOrderByAdapter;

    private Integer currentSub, currentSec, positionRuta=0;

    //private RelativeLayout mRelativeLayout;
    private ProgressBar mProgress;
    private Toolbar mToolbar;
    private AppArquos mAppArquos;
    private OrderBy currentOrderBy=null;
    private Integer positionOrderBy=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clsUtilities.setHomeBack(getActivity(),"Bajar Rutas Toma de Lectura");

        mAppArquos = (AppArquos) getActivity().getApplicationContext();
        mViewModelProvider = ViewModelProviders.of(this);

        mSubViewModel = mViewModelProvider.get(SubsistemaViewModel.class);
        mSecViewModel = mViewModelProvider.get(SectorViewModel.class);
        mRutaViewModel = mViewModelProvider.get(RutaViewModel.class);
        mCuentaViewModel = mViewModelProvider.get(CuentaViewModel.class);

        /*
        mSubViewModel.getAllSubsitemas().observe(this, new Observer<List<Subsistema>>() {
            @Override
            public void onChanged(@Nullable final List<Subsistema> subsistemas) {
                // Update the cached copy of the subsistemas in the adapter.
                update_spnSb(subsistemas);
            }
        });
        */



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.getruta_fragment, container, false);

        //mRelativeLayout =  v.findViewById(R.id.rlDownloaded);
        mProgress =  v.findViewById(R.id.progressBar);
        mProgress.setVisibility(View.GONE);
        //mRelativeLayout.setVisibility(View.GONE);

        Spinner cmbSub = v.findViewById(R.id.spnSub);
        cmbSub.setOnItemSelectedListener(this);

        Spinner cmbSec = v.findViewById(R.id.spnSec);
        cmbSec.setOnItemSelectedListener(this);

        Spinner cmbRuta = v.findViewById(R.id.spnRuta);
        cmbRuta.setOnItemSelectedListener(this);

        Spinner cmbOrderBy = v.findViewById(R.id.spnOrderBy);
        cmbOrderBy.setOnItemSelectedListener(this);
        cmbOrderBy.setVisibility(View.INVISIBLE);

        ((View ) v.findViewById(R.id.lblOrderBy)).setVisibility(View.INVISIBLE);
        ((View ) v.findViewById(R.id.rlOrderBy)).setVisibility(View.INVISIBLE);

        if (mAppArquos.getEmpresa().getRFC().equals("CMA020822651")){

            cmbOrderBy.setVisibility(View.VISIBLE);
            ((View ) v.findViewById(R.id.lblOrderBy)).setVisibility(View.VISIBLE);
            ((View ) v.findViewById(R.id.rlOrderBy)).setVisibility(View.VISIBLE);
        }


        final Button cmdPadron = v.findViewById(R.id.cmdPadron);
        cmdPadron.setText("RECIBIR CUENTAS");
        cmdPadron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdGetPadron_onClick(v);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSubViewModel.downloadSubsistemas(this);
        if (mAppArquos.getEmpresa().getRFC().equals("CMA020822651")){
            mSubViewModel.downloadOrderBy(this);
        }

    }

    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            LogSNE.d("NERUS", "onOptionsItemSelected "+ item.toString());
            switch (item.getItemId()) {
                case android.R.id.home:
                    // todo: goto back activity from here

                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
                        boolean done = getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        */
    private void cmdGetPadron_onClick(View v) {
        String cMsg = "";
//        LogSNE.d("NERUS", "currentRuta "+ currentRuta.toString());
        /*
        if (currentRuta!=null && currentSub>0 && currentSec>0 && currentRuta.getIdRuta()>0){

        }else
        * */

        if (currentSub<=0){
            cMsg = "Debe seleccionar un Subsistema";
        }else if (currentSec<=0){
            cMsg = "Debe seleccionar un Sector";
        }else if (currentRuta==null || currentRuta.getIdRuta()<=0 ){
            cMsg = "Debe seleccionar una Ruta";
        }else if ( mAppArquos.getEmpresa().getRFC().equals("CMA020822651")) {
            LogSNE.d("NERUS", mAppArquos.getEmpresa().getRFC());
            if (currentOrderBy==null || currentOrderBy.getIdOrder()<=0 ){
                cMsg = "Debe seleccionar un Orden de Lista";
            }
        }
        if (cMsg.length()>0){
            Snackbar.make(v, cMsg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            //mRelativeLayout.setVisibility(View.VISIBLE);
            Integer nOrderBy=0;
            if (currentOrderBy!=null){
                nOrderBy = currentOrderBy.getIdOrder();
            }
            mProgress.setVisibility(View.VISIBLE);
            mProgress.setIndeterminate(true);
            mCuentaViewModel.downloadCuentasDeRuta (currentRuta, nOrderBy, this);
            //v.setEnabled(false);
        }

    }

    /*
    @Nullable
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        LogSNE.d("NERUS", "onNavigationItemSelected");


        return true;
    }
    */


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view != null) {

            if (parent.getId() == R.id.spnSub) {
                SubsistemaAdapter.ViewHolder item = (SubsistemaAdapter.ViewHolder) view.getTag(R.string.item_tag);
                LogSNE.d("NERUS", "spnSub.onItemSelected " + item.value.toString());
                currentSub = item.value;
                if (item.value>=0){
                /*
                mSecViewModel.getSectores(item.value).observe(this, new Observer<List<Sector>>() {
                    @Override
                    public void onChanged(@Nullable final List<Sector> sectores) {
                        // Update the cached copy of the sectores in the adapter.
                        update_spnSec(sectores);
                    }
                });
                */

                mSecViewModel.downloadSectoresDelSb(item.value, this);
                positionRuta = 0;
                currentRuta=null;
                    //mRelativeLayout.setVisibility(View.GONE);
                }
            }else if (parent.getId() == R.id.spnSec) {
                SectorAdapter.ViewHolder item = (SectorAdapter.ViewHolder) view.getTag(R.string.item_tag);
                LogSNE.d("NERUS", "spnSec.onItemSelected " + item.value.toString() );
                currentSec = item.value;
                currentRuta=null;
                positionRuta = 0;
                if (item.value>=0) {


                    mRutaViewModel.getRutas(currentSub, currentSec).observe(this, new Observer<List<Ruta>>() {
                        @Override
                        public void onChanged(@Nullable final List<Ruta> rutas) {
                            // Update the cached copy of the sectores in the adapter.
                            update_spnRuta(rutas);
                        }
                    });


                    mRutaViewModel.downloadRutasDelSbySec(currentSub, currentSec, this);
                    //mRelativeLayout.setVisibility(View.GONE);
                }
            }else if (parent.getId() == R.id.spnRuta) {
                currentRuta = (Ruta) mRutaAdapter.getItem(position);
                positionRuta = position;
            }else if (parent.getId() == R.id.spnOrderBy) {
                currentOrderBy = (OrderBy) mOrderByAdapter.getItem(position);
                positionOrderBy = position;
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private void update_spnSb(List<Subsistema> subsistemas) {
        LogSNE.d("NERUS", "The onChanged  is: " + subsistemas.toString());
        mSubsistemaAdapter = new SubsistemaAdapter(getContext(), R.layout.spinner_row, subsistemas);
        mSubsistemaAdapter.setData(subsistemas);
        Spinner cmbSub = getActivity().findViewById(R.id.spnSub);
        cmbSub.setAdapter(mSubsistemaAdapter);
        if (subsistemas.size()==2){
            cmbSub.setSelection(1);
        }
    }
    private void update_spnOrderBy(List<OrderBy> list) {
        mOrderByAdapter = new OrderByAdapter(getContext(), R.layout.spinner_row, list);
        mOrderByAdapter.setData(list);
        Spinner cmbOrderBy = getActivity().findViewById(R.id.spnOrderBy);
        cmbOrderBy.setAdapter(mOrderByAdapter);
    }

    private void update_spnSec(List<Sector> sectores) {
        LogSNE.d("NERUS", "The onChanged  is: " + sectores.toString());
        mSectorAdapter = new SectorAdapter(getContext(), R.layout.spinner_row, sectores);
        mSectorAdapter.setData(sectores);
        Spinner cmbSec = getActivity().findViewById(R.id.spnSec);
        cmbSec.setAdapter(mSectorAdapter);
    }

    private void update_spnRuta(List<Ruta> rutas) {
        LogSNE.d("NERUS", "The onChanged  is: " + rutas.toString());
        mRutaList = rutas;
        if (mRutaList.size()>0){
            mRutaAdapter = new RutaAdapter(getContext(), R.layout.ruta_row, mRutaList);
            mRutaAdapter.setData(mRutaList);
            Spinner cmbRuta = getActivity().findViewById(R.id.spnRuta);
            cmbRuta.setAdapter(mRutaAdapter);
            if (mRutaList.size()>positionRuta){
                cmbRuta.setSelection(positionRuta);
            }
        }
        mProgress.setVisibility(View.GONE);
    }
    private void updateRutaDownloaded(Ruta ruta) {

            //RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.rlDownloaded);
            //TextView txtDescripcion = (TextView) getActivity().findViewById(R.id.lblDescripcion);
            TextView txtObservaciones = (TextView) getActivity().findViewById(R.id.txtObservaciones);
            TextView txtRegistros = (TextView) getActivity().findViewById(R.id.lblTotRegistros);
//            TextView txtMedidos = (TextView) getActivity().findViewById(R.id.txtMedidos);
//            TextView txtPromedios = (TextView) getActivity().findViewById(R.id.txtPromedios);
//            TextView txtFijos = (TextView) getActivity().findViewById(R.id.txtFijos);
//            TextView txtCapturados = (TextView) getActivity().findViewById(R.id.txtRegCapt);
//            TextView txtEnviados = (TextView) getActivity().findViewById(R.id.txtRegSend);

        txtObservaciones.setText(ruta.getObservaciones());

//        txtDescripcion.setText(ruta.getDescripcion());
//
            txtRegistros.setText(ruta.getRegistros().toString());
//            txtMedidos.setText(ruta.getMedidos().toString());
//            txtPromedios.setText(ruta.getPromedios().toString());
//            txtFijos.setText(ruta.getFijos().toString());
//            txtCapturados.setText(ruta.getCapturadas().toString());
//            txtEnviados.setText(ruta.getEnviadas().toString());
        txtObservaciones.setTextColor(Color.BLUE);
        txtRegistros.setTextColor(Color.BLUE);
    }


    @Override
    public void onRequestBeforeStart(String fromTask) {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {

    }

    @Override
    public void onRequestError(String fromTask, Exception error) {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {

    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        mProgress.setVisibility(View.GONE);
        if (fromTask.equals(SubsistemasDownloadTask.TASK_NAME)){
            List<Subsistema> subsistemas = (List<Subsistema>) response;
            update_spnSb(subsistemas);
        }
        if (fromTask.equals(OrderByDownloadTask.TASK_NAME)){
            List<OrderBy> list = (List<OrderBy>) response;
            update_spnOrderBy(list);
        }
        if (fromTask.equals(SectoresDownloadTask.TASK_NAME)){
            List<Sector> sectores = (List<Sector>) response;
            update_spnSec(sectores);
        }
        if (fromTask.equals(RutasDownloadTask.TASK_NAME)){
            List<Ruta> rutas = (List<Ruta>) response;
            update_spnRuta(rutas);
        }
        if (fromTask.equals(CuentasDownloadTask.TASK_NAME)){
            Ruta ruta = (Ruta) response;
            updateRutaDownloaded(ruta);

            mRutaAdapter.notifyDataSetChanged();
        }
    }

}
