package com.nerus.apparquos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.entities.OrdenCerrada;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mApiClient;
    private Location mLocation;
    private Marker mMarkerAqui;
    private Marker mMarkerBuzon;
    private Marker mMarkerMedidor;
    private Cuenta mCuenta;
    private Orden mOrden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        Intent intent = getIntent();
        mCuenta = (Cuenta) intent.getSerializableExtra ("CUENTA");
        mOrden = (Orden) intent.getSerializableExtra ("ORDEN");

        if (mCuenta!=null) {
            this.setTitle(mCuenta.getDireccion());
            ((TextView) findViewById(R.id.txtDireccion)).setText(mCuenta.getDireccion());
        }
        if (mOrden!=null) {
            this.setTitle(mOrden.getDireccion());
            ((TextView) findViewById(R.id.txtDireccion)).setText(mOrden.getDireccion());
        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);



        mApiClient = new GoogleApiClient
                .Builder(this)
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

        final Button cmdAqui = findViewById(R.id.cmdAqui);
        cmdAqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdAqui_onClick(v);
            }
        });

        final Button cmdMedidor = findViewById(R.id.cmdMedidor);
        cmdMedidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdMedidor_onClick(v);
            }
        });

        final Button cmdBuzon = findViewById(R.id.cmdBuzon);
        cmdBuzon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuzon_onClick(v);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mApiClient.connect();
        cmdAqui_onClick(null);
    }
    @Override
    public void onResume() {
        super.onResume();
        cmdAqui_onClick(null);
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

    @TargetApi(Build.VERSION_CODES.M)
    private void setupLocationRequest() {
        if (mApiClient.isConnected()){
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setNumUpdates(1);
            request.setInterval(0);
            if (clsUtilities.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && clsUtilities.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(clsUtilities.LOCATION_PERMISSIONS, clsRequestCodes.REQUEST_LOCATION_PERMISSIONS);
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
    }

    private void cmdAqui_onClick(View v) {
        setupLocationRequest();
        if (mLocation!=null){
            LatLng aqui = new LatLng( mLocation.getLatitude(), mLocation.getLongitude());
            if (mMarkerAqui!=null){
                mMarkerAqui.remove();
            }
            mMarkerAqui = mMap.addMarker(new MarkerOptions().position(aqui).title("Aqui estoy"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(aqui));
        }
    }
    private void cmdBuzon_onClick(View v) {
        setupLocationRequest();
        if (mLocation!=null){
            LatLng aqui = new LatLng( mLocation.getLatitude(), mLocation.getLongitude());
            if (mMarkerBuzon!=null){
                mMarkerBuzon.remove();
            }
            mMarkerBuzon = mMap.addMarker(new MarkerOptions()
                    .position(aqui)
                    .title("Aqui esta el Buzón")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );

            mMap.moveCamera(CameraUpdateFactory.newLatLng(aqui));
        }
    }

    private void cmdMedidor_onClick(View v) {
        setupLocationRequest();
        if (mLocation!=null){
            LatLng aqui = new LatLng( mLocation.getLatitude(), mLocation.getLongitude());
            if (mMarkerMedidor!=null){
                mMarkerMedidor.remove();
            }
            String cTit = "Medidor:";
            if (mCuenta!=null) {
                cTit += mCuenta.getMedidor();
            }
            if (mOrden!=null) {
                cTit += mOrden.getIdMedidor();
            }
            mMarkerMedidor = mMap.addMarker(new MarkerOptions()
                    .position(aqui)
                    .title( cTit )
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );

            mMap.moveCamera(CameraUpdateFactory.newLatLng(aqui));
        }
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //23.751963,-99.137865
        LatLng aqui = new LatLng(23.7519,-99.137865);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(aqui));
        mMap.setMinZoomPreference(18.0f);
        mMap.setMaxZoomPreference(25.0f);
        //cmdAqui_onClick(null);
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
