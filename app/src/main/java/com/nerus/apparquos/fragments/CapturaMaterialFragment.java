package com.nerus.apparquos.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputEditText;
import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.MapsActivity;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.AnomaliaAdapter;
import com.nerus.apparquos.entities.Anomalia;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Empleado;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.Material;
import com.nerus.apparquos.entities.MaterialCapturado;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
import com.nerus.apparquos.viewmodels.AnomaliaViewModel;
import com.nerus.apparquos.viewmodels.CuentaViewModel;
import com.nerus.apparquos.viewmodels.LecturaViewModel;

import java.util.List;

public class CapturaMaterialFragment  extends Fragment {
    //private FusedLocationProviderClient mApiClient;
    private GoogleApiClient mApiClient;
    private Location mLocation;
    private static final String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };

    private ViewModelProvider mViewModelProvider;

    private LayoutInflater mLayoutInflater;
    private Empleado currentLecturista;
    private Orden mOrden;
    private MaterialCapturado mMaterial;
    private AppArquos mAppArquos;
    private EditText txtCantidad;
    private EditText txtCodigo;
    private EditText txtObserva;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAppArquos = (AppArquos) getActivity().getApplicationContext();
        LogSNE.d("NERUS","onCreate");
        currentLecturista = mAppArquos.getEmpleado();

        Bundle args = getArguments();
        mOrden = (Orden) args.getSerializable("ORDEN");
        clsUtilities.setHomeBack(getActivity(),mOrden.getIdOrden());


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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogSNE.d("NERUS","onCreateView");
        mLayoutInflater = inflater;
        final View v = inflater.inflate(R.layout.capturamaterial_fragment, container, false);


        ((TextView) v.findViewById(R.id.txtObserva)).setText("");
        ((TextView) v.findViewById(R.id.txtCantidad)).setText("1");

        txtCantidad = (EditText) v.findViewById(R.id.txtCantidad);
        txtCodigo = (EditText) v.findViewById(R.id.txtCodigo);
        txtObserva = (EditText) v.findViewById(R.id.txtObserva);
        ImageView cmdAdd = (ImageView) v.findViewById(R.id.ic_plus);
        cmdAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer value = Integer.valueOf(txtCantidad.getText().toString())+1;
                if (value>99999) value=1;
                txtCantidad.setText(value.toString());
            }
        });
        ImageView cmdMinus = (ImageView) v.findViewById(R.id.ic_minus);
        cmdMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer value = Integer.valueOf(txtCantidad.getText().toString())-1;
                if (value<0) value=1;
                txtCantidad.setText(value.toString());
            }
        });
        txtCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    clsUtilities.hideKeyboard(getActivity());
                    LogSNE.d("NERUS","buscar "+ txtCodigo.getText());
                }
            }
        });
        txtObserva.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    clsUtilities.hideKeyboard(getActivity());

                }
            }
        });
        ImageView cmdSearch = (ImageView) v.findViewById(R.id.ic_search);
        cmdSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSearch(v);
            }
        });
        return v;
    }

    private void onClickSearch(View v) {
        LogSNE.d("NERUS","buscar "+ txtCodigo.getText());
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mOrden.getIdOrden());
        mApiClient.connect();
        //this.setObservables(mCuenta.getId_Padron());




        //mAnomaliaViewModel.downloadAnomalias();

//        FragmentActivity rootView = getActivity();
//        ((TextView) rootView.findViewById(R.id.txtCuenta)).setText(mCuenta.getId_Cuenta().toString());
//        ((TextView) rootView.findViewById(R.id.txtMedidor)).setText(mCuenta.getMedidor());
//        ((TextView) rootView.findViewById(R.id.txtIdMedidor)).setText(mCuenta.getMedidor());
//        ((TextView) rootView.findViewById(R.id.txtLocalizacion)).setText(mCuenta.getLocalizacion());
//        ((TextView) rootView.findViewById(R.id.txtDireccion)).setText(mCuenta.getDireccion());
//        ((TextView) rootView.findViewById(R.id.txtRazonSocial)).setText(mCuenta.getRazon_social());
//        ((TextView) rootView.findViewById(R.id.txtSituacion)).setText(mCuenta.getSituacion());
//        ((TextView) rootView.findViewById(R.id.lblMedidor)).setText("MEDIDOR:");


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
        inflater.inflate(R.menu.capturamaterial_menu, menu);
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


        /*
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
        */

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

    }

    private void showMaterial(Material material) {
/*        currentLectura = lectura;
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
            validaLectura(currentLectura);
            sendResult(Activity.RESULT_OK);

            //getActivity().getSupportFragmentManager().popBackStack("LIST_CUENTAS",0);
        }*/

    }

    private void sendResult(int resultCode) {
        if (getTargetFragment()==null){
            return;
        }

        //mCuenta.setCapturado(true);
        //mCuentaViewModel.setCapturado(mCuenta);
        Intent intent = new Intent();
        intent.putExtra("MATERIAL", mMaterial);
        getTargetFragment().onActivityResult(clsRequestCodes.REQUEST_CAPTURAMATERIAL , resultCode, intent);

        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
            boolean done = getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }



}
