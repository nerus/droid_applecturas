package com.nerus.apparquos.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.MapsActivity;
import com.nerus.apparquos.R;

import com.nerus.apparquos.adapters.CausaAdapter;
import com.nerus.apparquos.entities.Anomalia;
import com.nerus.apparquos.entities.CausaNoEjecucion;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.entities.OrdenCerrada;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.helpers.GpsTracker;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;

import com.nerus.apparquos.viewmodels.CausaNoEjecucionViewModel;
import com.nerus.apparquos.viewmodels.OrdenViewModel;
import com.nerus.apparquos.viewmodels.ParametroViewModel;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class CapturaOrdenFragment extends Fragment {
    //private FusedLocationProviderClient mApiClient;
    private GoogleApiClient mApiClient;
    private Location mLocation;
    private static final String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };

    private ViewModelProvider mViewModelProvider;
    //private ParametroViewModel mParametroViewModel;
    private OrdenViewModel mOrdenViewModel;
    private CausaNoEjecucionViewModel mCausaNoEjecucionViewModel;

    private Orden mOrden;
    private OrdenCerrada mOrdenCerrada;

    private LayoutInflater mLayoutInflater;

    private Boolean mIsSaved = false;
    private OrdenCerrada currentOTCerrada;
    private Integer positionCausa;
    private List<CausaNoEjecucion> mNoEjecucionList;
    private CausaAdapter mCausaAdapter;
    private CausaNoEjecucion currentCausa;
    private Spinner mCmbCausas;
    private Switch mSwEjecutada;
    private AppArquos mAppArquos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAppArquos = (AppArquos) getActivity().getApplicationContext();
        Bundle args = getArguments();
        mOrden = (Orden) args.getSerializable("ORDEN");
        clsUtilities.setHomeBack(getActivity(),mOrden.getTrabajo());

        mApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LogSNE.d("NERUS", "GoogleApiClient.onConnected " );
                        setupLocationRequest();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        LogSNE.d("NERUS", "GoogleApiClient.onConnectionSuspended " );
                    }
                })
                .build();

        //LogSNE.d("NERUS", ".showLectura " + mCuenta.toString());
        mViewModelProvider = ViewModelProviders.of(this);

        mOrdenViewModel = mViewModelProvider.get(OrdenViewModel.class);
        mCausaNoEjecucionViewModel = mViewModelProvider.get(CausaNoEjecucionViewModel.class);
        //mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);
        mIsSaved = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        final View v = inflater.inflate(R.layout.capturaorden_fragment, container, false);


        mCmbCausas = v.findViewById(R.id.cmbCausas);
        mCmbCausas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionCausa = position;
                currentCausa = mNoEjecucionList.get(positionCausa);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSwEjecutada = v.findViewById(R.id.swEjecutada);

        mSwEjecutada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCmbCausas.setEnabled(!isChecked);
                if (isChecked) mCmbCausas.setSelection(0);
            }
        });


        final Button cmdMateriales = v.findViewById(R.id.cmdMateriales);
        cmdMateriales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdMateriales_onClick(v);
            }
        });

        final Button cmdPhotos = v.findViewById(R.id.cmdPhotos);
        cmdPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdPhotos_onClick(v);
            }
        });

        final Button cmdGeoUbica = v.findViewById(R.id.cmdGeoUbica);
        cmdGeoUbica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdGeoUbica_onClick(v);
            }
        });


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mOrden.getTrabajo());
        mApiClient.connect();


        this.setObservables();

        //mCausaNoEjecucionViewModel.downloadCausasNoEjecucion();

        FragmentActivity rootView = getActivity();

        ((TextView) rootView.findViewById(R.id.orden_detail)).setText(mOrden.getObservaA());
        ((TextView) rootView.findViewById(R.id.txtFechaGenero)).setText(mOrden.getFechaGenero());
        ((TextView) rootView.findViewById(R.id.txtGenero)).setText(mOrden.getGenero());
        ((TextView) rootView.findViewById(R.id.txtTrabajo)).setText(mOrden.getTrabajo());
        ((TextView) rootView.findViewById(R.id.txtMedidor)).setText(mOrden.getIdMedidor());
        ((TextView) rootView.findViewById(R.id.txtSituacion)).setText(mOrden.getEstatus());
        ((TextView) rootView.findViewById(R.id.txtIdOrden)).setText(mOrden.getIdOrden());
        ((TextView) rootView.findViewById(R.id.txtCuenta)).setText(mOrden.getIdCuenta().toString());
        ((TextView) rootView.findViewById(R.id.txtDireccion)).setText(mOrden.getDireccion() + ", " + mOrden.getColonia() + ", " + mOrden.getPoblacion());
        ((TextView) rootView.findViewById(R.id.txtLocalizacion)).setText(mOrden.getLocalizacion());
        ((TextView) rootView.findViewById(R.id.txtRazonSocial)).setText(mOrden.getNombre());

        if (mOrdenCerrada == null) {
            ((TextView) rootView.findViewById(R.id.txtIdMedidor)).setText(mOrden.getIdMedidor());
        }else{
            LogSNE.d("NERUS","mOrdenCerrada:"+mOrdenCerrada.toString()  );

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mApiClient.disconnect();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case clsRequestCodes.REQUEST_LOCATION_PERMISSIONS:
                LogSNE.d("NERUS","OK");
                setupLocationRequest();
                break;
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        LogSNE.d("NERUS","CapturaOrdenFragment.onPrepareOptionsMenu"  );
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.capturaorden_menu, menu);
        LogSNE.d("NERUS","CapturaOrdenFragment.onCreateOptionsMenu");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            LogSNE.d("NERUS","save orden" + item.toString());
            cmdSave_onClick(item.getActionView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void cmdSave_onClick(View v) {
        clsUtilities.hideKeyboard(getActivity());

        if (mLocation==null){
            mApiClient.connect();
        }

        GpsTracker gt = new GpsTracker(getActivity());
        mLocation = gt.getLocation();

        FragmentActivity rootView = getActivity();
        String cMedidor = ((TextView) rootView.findViewById(R.id.txtIdMedidor)).getText().toString();
        String cObserva = ((TextView) rootView.findViewById(R.id.txtObserva_b)).getText().toString();
        Integer nIdCausa = currentCausa  != null ? currentCausa.getIdCausa() : 0;
        nIdCausa = nIdCausa<0 ? 0 : nIdCausa;

        Double nLongitud = 0.0;
        Double nLatitud = 0.0;
        if (mLocation!=null){
            nLongitud = mLocation.getLongitude();
            nLatitud = mLocation.getLatitude();
        }else{
            if (!gt.getGPSEnabled()){
                FragmentManager fm = getActivity(). getSupportFragmentManager();
                final MessageBoxFragment messageBox =  MessageBoxFragment.newInstance("POR FAVOR ACTIVE EL GPS", "");
                messageBox.setTargetFragment(CapturaOrdenFragment.this, clsRequestCodes.REQUEST_MESSAGEBOX);
                messageBox.setMessageBoxListener(new MessageBoxFragment.MessageBoxListener() {
                    @Override
                    public void onResultMessageBoxDialog(Boolean successful) {
                        if (successful){

                        }
                    }
                });
                messageBox.show(fm,"DIALOG_MESSAGEBOX");
            }
        }

        Integer nIdPersonal = mAppArquos.getEmpleado().getIdPersonal();
        String cFecha = clsFecha.getFechaYMDhms();
        mIsSaved = true;
        if (mOrdenCerrada == null) {
            mOrdenCerrada = new OrdenCerrada(mOrden.getIdOrden(),mOrden.getIdTrabajo(),mOrden.getTrabajo(),nIdPersonal,cObserva,""
                    ,nLongitud, nLatitud,cMedidor,mOrden.getFechaGenero()
                    ,cFecha,"", mSwEjecutada.isChecked(),nIdCausa,false,"");
            mOrdenViewModel.insertOTCerrada(mOrdenCerrada);
        } else {
            mOrdenCerrada.setFechaRealizo(cFecha);
            mOrdenCerrada.setIdNoEjecucion(nIdCausa);
            mOrdenCerrada.setIdMedidor(cMedidor);
            mOrdenCerrada.setIdEmpleado(nIdPersonal);
            mOrdenCerrada.setLatitud(nLatitud);
            mOrdenCerrada.setLongitud(nLongitud);
            mOrdenCerrada.setEjecutada(mSwEjecutada.isChecked());
            mOrdenCerrada.setObservaB(cObserva);
            mOrdenViewModel.updateOTCerrada(mOrdenCerrada);
        }

    }
    private void cmdPhotos_onClick(View v) {
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("ORDEN",mOrden);

            fragment = new PhotosFragment();
            //fragment.setTargetFragment(this, clsRequestCodes.REQUEST_PHOTOS);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("PHOTOS_GALLERY")
                    .commit();
        }
    }

    private void cmdGeoUbica_onClick(View v) {

        Intent intent = new Intent(getActivity(), MapsActivity.class);

        intent.putExtra("ORDEN", mOrden);
        startActivity(intent);
    }
    private void cmdMateriales_onClick(View v) {
        if (mLocation==null){
            mApiClient.connect();
        }
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("ORDEN",mOrden);

            fragment = new ListMaterialCapturadoFragment();
            //fragment.setTargetFragment(this, clsRequestCodes.REQUEST_PHOTOS);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("LIST_MATERIALCAPTURADO")
                    .commit();
        }
        return;

    }

    private void setupLocationRequest() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(10);
        request.setInterval(0);
        if (clsUtilities.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && clsUtilities.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LOCATION_PERMISSIONS, clsRequestCodes.REQUEST_LOCATION_PERMISSIONS);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                LogSNE.d("NERUS", "mLocation=" + mLocation.toString());
            }
        });
    }




    private void setObservables() {
        mCausaNoEjecucionViewModel.getCausas().observe(this, new Observer<List<CausaNoEjecucion>>() {
            @Override
            public void onChanged(@Nullable final List<CausaNoEjecucion> noEjecucionList) {
                // Update the cached copy of the subsistemas in the adapter.
                update_cmbCausas(noEjecucionList);
//                if (positionCausa>0){
//                    Spinner cmbCausas = getActivity().findViewById(R.id.cmbCausas);
//                    cmbCausas.setSelection(positionCausa);
//                }
            }
        });
        mOrdenViewModel.getOrdenCerrada(mOrden.getIdOrden()).observe(this, new Observer<OrdenCerrada>() {
            @Override
            public void onChanged(@Nullable final OrdenCerrada ordenCerrada) {
                showOrden(ordenCerrada);
            }
        });
    }

    private void showOrden(OrdenCerrada orden) {
        currentOTCerrada = orden;
        if (currentOTCerrada!=null){
            LogSNE.d("NERUS", ".showOrden " + orden.toString());
            FragmentActivity rootView = getActivity();
            ((TextView) rootView.findViewById(R.id.txtObserva_b)).setText(currentOTCerrada.getObservaB());
            ((TextView) rootView.findViewById(R.id.txtIdMedidor)).setText(currentOTCerrada.getIdMedidor());
            ((Switch) rootView.findViewById(R.id.swEjecutada)).setChecked(currentOTCerrada.getEjecutada());

            show_IdCausa(currentOTCerrada );

            if (mIsSaved){
                sendResult(Activity.RESULT_OK);
            }
        }

    }

    private void sendResult(int resultCode) {
        if (getTargetFragment()==null){
            return;
        }
        mOrden.setCapturado(true);
        mOrdenViewModel.setCapturado(mOrden);
        Intent intent = new Intent();
        intent.putExtra("ORDEN", mOrden);
        getTargetFragment().onActivityResult(clsRequestCodes.REQUEST_CAPTURAORDEN , resultCode, intent);

        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
            boolean done = getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private void show_IdCausa(OrdenCerrada ordenCerrada) {
        if (ordenCerrada!=null && ordenCerrada.getIdNoEjecucion()>0){
            if (mNoEjecucionList!=null && mNoEjecucionList.size()>0){
                for (int i = 0; i < mNoEjecucionList.size(); i++) {
                    CausaNoEjecucion item = mNoEjecucionList.get(i);
                    if (item.getIdCausa()==ordenCerrada.getIdNoEjecucion()){
                        positionCausa=i;
                        Spinner cmbCausas = getActivity().findViewById(R.id.cmbCausas);
                        if (positionCausa>0 && positionCausa<mNoEjecucionList.size()){
                            cmbCausas.setSelection(positionCausa);
                        }
                        break;
                    }
                }
            }
        }
    }
    private void update_cmbCausas(List<CausaNoEjecucion> noEjecucionList) {
        mNoEjecucionList = noEjecucionList;
        if (mNoEjecucionList.size() > 0) {
            mCausaAdapter = new CausaAdapter(getContext(), mNoEjecucionList);
            mCausaAdapter.setData(mNoEjecucionList);
            Spinner cmbCausas = getActivity().findViewById(R.id.cmbCausas);
            cmbCausas.setAdapter(mCausaAdapter);
            show_IdCausa(currentOTCerrada);
        }
        LogSNE.d("NERUS", "The onChanged  is: " + mNoEjecucionList.toString());
    }



}
