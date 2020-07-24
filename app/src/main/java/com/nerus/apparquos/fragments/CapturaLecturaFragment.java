package com.nerus.apparquos.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.MapsActivity;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.AnomaliaAdapter;
import com.nerus.apparquos.entities.Anomalia;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Empleado;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.helpers.GpsTracker;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.viewmodels.AnomaliaViewModel;
import com.nerus.apparquos.viewmodels.CuentaViewModel;
import com.nerus.apparquos.viewmodels.LecturaViewModel;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

import java.util.List;

public class CapturaLecturaFragment extends Fragment {
    //private FusedLocationProviderClient mApiClient;
    private GoogleApiClient mApiClient;
    private Location mLocation;
    private static final String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };

    private ViewModelProvider mViewModelProvider;
    private AnomaliaViewModel mAnomaliaViewModel;
    private CuentaViewModel mCuentaViewModel;
    private LecturaViewModel mLecturaViewModel;

    private Integer positionAnomalia = 0;
    private List<Anomalia> mAnomaliasList;
    private Anomalia currentAnomalia;
    private Cuenta mCuenta;
    //private List<Lectura> mLectura;
    private Lectura currentLectura;
    private AnomaliaAdapter mAnomaliaAdapter;
    private LayoutInflater mLayoutInflater;
    //private ParametroViewModel mParametroViewModel;
    private Empleado currentLecturista;

    private Boolean mIsSaved = false;
    private AppArquos mAppArquos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAppArquos = (AppArquos) getActivity().getApplicationContext();
        LogSNE.d("NERUS","onCreate");
        currentLecturista = mAppArquos.getEmpleado();

        Bundle args = getArguments();
        mCuenta = (Cuenta) args.getSerializable("CUENTA");
        clsUtilities.setHomeBack(getActivity(),mCuenta.getLocalizacion());

        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mCuenta.getLocalizacion());

//        mApiClient = LocationServices.getFusedLocationProviderClient(getActivity());

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

        mCuentaViewModel = mViewModelProvider.get(CuentaViewModel.class);
        mAnomaliaViewModel = mViewModelProvider.get(AnomaliaViewModel.class);
        mLecturaViewModel = mViewModelProvider.get(LecturaViewModel.class);
        //mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);
        mIsSaved = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogSNE.d("NERUS","onCreateView");
        mLayoutInflater = inflater;
        final View v = inflater.inflate(R.layout.capturalectura_fragment, container, false);


        ((TextView) v.findViewById(R.id.txtObserva)).setText("");
        ((TextView) v.findViewById(R.id.txtLectura)).setText("");

        Spinner cmbAnom = v.findViewById(R.id.cmbAnomalias);
        cmbAnom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionAnomalia = position;
                currentAnomalia = mAnomaliasList.get(positionAnomalia);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*
        final Button cmdSave = v.findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdSave_onClick(v);
            }
        });
*/

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
    public void onAttach(Context context) {
        super.onAttach(context);
        LogSNE.d("NERUS","onAttach");
    }



    @Override
    public void onStart() {
        super.onStart();
        LogSNE.d("NERUS","onStart");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mCuenta.getDireccion());
        mApiClient.connect();
        this.setObservables(mCuenta.getId_Padron());




        //mAnomaliaViewModel.downloadAnomalias();

        FragmentActivity rootView = getActivity();
        ((TextView) rootView.findViewById(R.id.txtCuenta)).setText(mCuenta.getId_Cuenta().toString());
        ((TextView) rootView.findViewById(R.id.txtMedidor)).setText(mCuenta.getMedidor());
        ((TextView) rootView.findViewById(R.id.txtIdMedidor)).setText(mCuenta.getMedidor());
        ((TextView) rootView.findViewById(R.id.txtLocalizacion)).setText(mCuenta.getLocalizacion());
        ((TextView) rootView.findViewById(R.id.txtDireccion)).setText(mCuenta.getDireccion());
        ((TextView) rootView.findViewById(R.id.txtRazonSocial)).setText(mCuenta.getRazon_social());
        ((TextView) rootView.findViewById(R.id.txtSituacion)).setText(mCuenta.getSituacion());
        ((TextView) rootView.findViewById(R.id.lblMedidor)).setText("MEDIDOR:");


    }

    @Override
    public void onStop() {
        super.onStop();
        LogSNE.d("NERUS","onStop");
        mApiClient.disconnect();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        LogSNE.d("NERUS","onRequestPermissionsResult");
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
        LogSNE.d("NERUS","CapturaLecturaFragment.onPrepareOptionsMenu"  );
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.capturaorden_menu, menu);
        LogSNE.d("NERUS","CapturaLecturaFragment.onCreateOptionsMenu");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            LogSNE.d("NERUS",item.toString());
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
        String cMedidor = ((TextView) rootView.findViewById(R.id.txtMedidor)).getText().toString();
        String cObserva = ((TextView) rootView.findViewById(R.id.txtObserva)).getText().toString();
        Integer nIdAnomalia = currentAnomalia != null ? currentAnomalia.getId_anomalia() : 0;
        nIdAnomalia = nIdAnomalia<0 ? 0 : nIdAnomalia;

        Double nLongitud = 0.0;
        Double nLatitud = 0.0;
        if (mLocation!=null){
            nLongitud = mLocation.getLongitude();
            nLatitud = mLocation.getLatitude();
        }else{
            if (!gt.getGPSEnabled()){
                FragmentManager fm = getActivity(). getSupportFragmentManager();
                final MessageBoxFragment messageBox =  MessageBoxFragment.newInstance("POR FAVOR ACTIVE EL GPS", "");
                messageBox.setTargetFragment(CapturaLecturaFragment.this, clsRequestCodes.REQUEST_MESSAGEBOX);
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


        Integer nIdPersonal = currentLecturista.getIdPersonal();
        String cFecha = clsFecha.getFechaYMDhms();
        String cLectura = ((TextView) rootView.findViewById(R.id.txtLectura)).getText().toString();
        Integer nLectura = 0;
        if (cLectura.length() > 0) {
            nLectura = Integer.parseInt(cLectura);
        }
        mIsSaved = true;
        if (currentLectura == null) {
            currentLectura = new Lectura(mCuenta.getId_Padron(), mCuenta.getId_Padron(), cMedidor, nLectura, nIdAnomalia, cObserva, nIdPersonal, cFecha, nLatitud, nLongitud, 0, "");
            mLecturaViewModel.insert(currentLectura);
        } else {
            currentLectura.setFecha(cFecha);
            currentLectura.setIdAnomalia(nIdAnomalia);
            currentLectura.setIdMedidor(cMedidor);
            currentLectura.setIdPersonal(nIdPersonal);
            currentLectura.setLatitud(nLatitud);
            currentLectura.setLongitud(nLongitud);
            currentLectura.setLectura(nLectura);
            currentLectura.setObservaciones(cObserva);
            mLecturaViewModel.update(currentLectura);
        }

    }
    private void cmdPhotos_onClick(View v) {
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("CUENTA",mCuenta);

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

        intent.putExtra("CUENTA", mCuenta);
        startActivity(intent);
    }

    private void setupLocationRequest() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
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




    private void setObservables(Integer idPadron) {
        mAnomaliaViewModel.getAnomalias().removeObservers(this);
        mAnomaliaViewModel.getAnomalias().observe(this, new Observer<List<Anomalia>>() {
            @Override
            public void onChanged(@Nullable final List<Anomalia> anomalias) {
                // Update the cached copy of the subsistemas in the adapter.
                update_cmbAnomalias(anomalias);
                if (positionAnomalia>0){
                    Spinner cmbAnomalias = getActivity().findViewById(R.id.cmbAnomalias);
                    cmbAnomalias.setSelection(positionAnomalia);
                }
            }
        });
        mLecturaViewModel.getLecturaByIdPadron(idPadron).removeObservers(this);
        mLecturaViewModel.getLecturaByIdPadron(idPadron).observe(this, new Observer<List<Lectura>>() {
            @Override
            public void onChanged(@Nullable final List<Lectura> lectura) {
                // Update the cached copy of the subsistemas in the adapter.
                //mLectura = lectura;
                if (lectura.size()>0){
                    showLectura(lectura.get(0));
                }
                LogSNE.d("NERUS","mLecturaViewModel.getLecturaByIdPadron(idPadron).observe");

            }
        });

    }

    private void showLectura(Lectura lectura) {
        currentLectura = lectura;
        LogSNE.d("NERUS", ".showLectura " + lectura.toString());
        FragmentActivity rootView = getActivity();
        ((TextView) rootView.findViewById(R.id.txtObserva)).setText(lectura.getObservaciones());
        TextView txtLectura = (TextView) rootView.findViewById(R.id.txtLectura);
        txtLectura.setTag("0");
        if (lectura.getLectura()>0){
            txtLectura.setTag(lectura.getIdLectura().toString());
            txtLectura.setText(lectura.getLectura().toString());
            Integer nLectura_New =0;
            if (txtLectura.getText().toString().length()>0){
                nLectura_New = Integer.parseInt(txtLectura.getText().toString());
                if (nLectura_New<=0)
                    txtLectura.setText("");
            }
        }else{
            txtLectura.setText("");
        }
        if (lectura.getIdMedidor().length()>0){
            //LogSNE.d("NERUS", "med=" + lecturaItem.id_medidor+"=med="+mItem.medidor+"=");
            if ( ! (lectura.getIdMedidor().equals( mCuenta.getMedidor()))) {

                ((TextView) rootView.findViewById(R.id.txtMedidor)).setText(lectura.getIdMedidor());
                ((TextView) rootView.findViewById(R.id.lblMedidor)).setText("MEDIDOR\r\n(" + mCuenta.getMedidor() + ")");
            }
        }
        show_IdAnomalia(currentLectura );

        if (mIsSaved){
            if (validaLectura(currentLectura)){
                sendResult(Activity.RESULT_OK);
            }


            //getActivity().getSupportFragmentManager().popBackStack("LIST_CUENTAS",0);
        }

    }

    private void sendResult(int resultCode) {
        if (getTargetFragment()==null){
            return;
        }
        mCuenta.setCapturado(true);
        mCuentaViewModel.setCapturado(mCuenta);
        Intent intent = new Intent();
        intent.putExtra("CUENTA", mCuenta);
        getTargetFragment().onActivityResult(clsRequestCodes.REQUEST_CAPTURALECTURA , resultCode, intent);

        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
            boolean done = getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private void show_IdAnomalia(Lectura lectura) {
        if (lectura!=null && lectura.getIdAnomalia()>0){
            if (mAnomaliasList!=null && mAnomaliasList.size()>0){
                for (int i = 0; i < mAnomaliasList.size(); i++) {
                    Anomalia item = mAnomaliasList.get(i);
                    if (item.getId_anomalia()==lectura.getIdAnomalia()){
                        positionAnomalia=i;
                        Spinner cmbAnom = getActivity().findViewById(R.id.cmbAnomalias);
                        if (positionAnomalia>0 && positionAnomalia<mAnomaliasList.size()){
                            cmbAnom.setSelection(positionAnomalia);
                        }
                        break;
                    }
                }
            }
        }
    }
    private void update_cmbAnomalias(List<Anomalia> anomalias) {
        mAnomaliasList = anomalias;
        if (mAnomaliasList.size() > 0) {
            mAnomaliaAdapter = new AnomaliaAdapter(getContext(), mAnomaliasList);
            mAnomaliaAdapter.setData(mAnomaliasList);
            Spinner cmbAnomalias = getActivity().findViewById(R.id.cmbAnomalias);
            cmbAnomalias.setAdapter(mAnomaliaAdapter);
            show_IdAnomalia(currentLectura);
        }
        LogSNE.d("NERUS", "The onChanged  is: " + mAnomaliasList.toString());
    }
    private boolean validaLectura(Lectura lectura){
        boolean lResult = true;

        Boolean lPegado = mCuenta.getLectura_ant().equals(lectura.getLectura());
        Boolean lMenor = mCuenta.getLectura_ant()>lectura.getLectura() && mCuenta.getLectura_ant()>0 && lectura.getLectura()>0;

        Integer nConsumo = lectura.getLectura()-mCuenta.getLectura_ant();
        String cMsg = "";
        if (nConsumo>0 ) {
            Float nVeces =Math.abs((float)nConsumo/(float)mCuenta.getPromedio());
            LogSNE.d("NERUS", "nVeces is: "+ nVeces.toString() +" promedio is: "+ mCuenta.getPromedio().toString());
            if (nVeces>2){
                cMsg = "Por favor verifique la lectura del medidor.\r\n¿ Desea Continuar ?";
                //Toast toast = Toast.makeText(getActivity(), "Por favor verifique la lectura del medidor.", Toast.LENGTH_LONG);
                //toast.show();
            }
        }else if (lPegado || lMenor){
            //Toast toast = Toast.makeText(getActivity(), "Por favor verifique la lectura.", Toast.LENGTH_LONG);
            //toast.show();
            cMsg = "Por favor verifique la lectura.\r\n¿ Desea Continuar ?";
        }
        LogSNE.d("NERUS", "consumo  is:"+nConsumo.toString() +" = "+ lectura.getLectura().toString() +" - "+mCuenta.getLectura_ant().toString());
        if (cMsg.length()>0){

            FragmentManager fm = getActivity(). getSupportFragmentManager();
            final MessageBoxFragment messageBox =  MessageBoxFragment.newInstance(cMsg, mAppArquos.getEmpleado().getNombre());
            messageBox.setTargetFragment(CapturaLecturaFragment.this, clsRequestCodes.REQUEST_MESSAGEBOX);
            messageBox.setMessageBoxListener(new MessageBoxFragment.MessageBoxListener() {
                @Override
                public void onResultMessageBoxDialog(Boolean successful) {
                    if (successful){
                        sendResult(Activity.RESULT_OK);
                    }
                }
            });
            messageBox.show(fm,"DIALOG_MESSAGEBOX");


            //Snackbar.make(getView(), cMsg, Snackbar.LENGTH_SHORT)
            //        .setAction("Action", null).show();
            return false;
        }else{
            return lResult;
        }

    }


}
