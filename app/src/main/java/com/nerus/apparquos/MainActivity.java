package com.nerus.apparquos;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.nerus.apparquos.entities.Empleado;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.fragments.AboutFragment;
import com.nerus.apparquos.fragments.ConfigurarFragment;
import com.nerus.apparquos.fragments.LoginEmpleadoFragment;
import com.nerus.apparquos.fragments.MainFragment;

import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.viewmodels.AnomaliaViewModel;
import com.nerus.apparquos.viewmodels.CausaNoEjecucionViewModel;
import com.nerus.apparquos.viewmodels.EmpleadoViewModel;
import com.nerus.apparquos.viewmodels.ParametroViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.os.ParcelUuid;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class MainActivity extends AppCompatActivity  {
//    private ViewModelProvider mViewModelProvider;
//    private ParametroViewModel mParametroViewModel;
//    //public Empleado mEmpleado;
//    public AppArquos mAppArquos;
//
    private FragmentManager fm;
//    private AnomaliaViewModel mAnomaliaViewModel;
//    private CausaNoEjecucionViewModel mCausaViewModel;
//    private EmpleadoViewModel mEmpleadoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogSNE.d("NERUS","MainActivity.onCreate ");
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //mAppArquos = (AppArquos) getApplicationContext();

        for (int i=1; i<=4; i++){
            lookPermissions();
        }


        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_main);
        if (fragment == null) {
            fragment = new MainFragment();
            fm.beginTransaction()
                    .add(R.id.content_main,fragment)
                    .commit();
        }
/*
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
*/
    }
    @Override
    public void onStart() {
        super.onStart();
        LogSNE.d("NERUS","MainActivity.onStart ");

        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mCuenta.getDireccion());


        lookPermissions();






    }
    @Override
    public void onResume() {
        super.onResume();
        LogSNE.d("NERUS","MainActivity.onResume ");

        lookPermissions();
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = apiAvailability
                    .getErrorDialog(this, errorCode, clsRequestCodes.REQUEST_ERROR
                            , new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    finish();
                                }
                            });
            errorDialog.show();
        }

    }


    private void showConfigurar(){
        FragmentManager fm =  getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            fragment = new ConfigurarFragment();
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("content_main")
                    .commit();
        }
    }
    private void showLoginEmpleado(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            fragment = new LoginEmpleadoFragment();
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("content_main")
                    .commit();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            LogSNE.d("NERUS",item.toString());
            return true;
        }else if (id == R.id.action_about) {
            LogSNE.d("NERUS",item.toString());
            Fragment fragment = fm.findFragmentById(R.id.content_main);
            if (fragment == null) {
                LogSNE.d("NERUS if",item.toString());
                fragment = new AboutFragment();
                fm.beginTransaction()
                        .add(R.id.content_main,fragment)
                        .addToBackStack("content_main")
                        .commit();
            }else{
                LogSNE.d("NERUS else",item.toString());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */


    /*
    implements NavigationView.OnNavigationItemSelectedListener
    @Override
    public boolean onNavigationItemSelected( MenuItem menuItem) {

        int id = menuItem.getItemId();
        LogSNE.d("NERUS",menuItem.toString());


        return true;
    }
    */

    @TargetApi(Build.VERSION_CODES.M)
    private void lookPermissions(){
        if (clsUtilities.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && clsUtilities.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(clsUtilities.STORAGE_PERMISSIONS, clsRequestCodes.REQUEST_STORAGE_PERMISSIONS);
            return;
        }
        if (clsUtilities.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && clsUtilities.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(clsUtilities.LOCATION_PERMISSIONS, clsRequestCodes.REQUEST_LOCATION_PERMISSIONS);
            return;
        }
        if (clsUtilities.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(clsUtilities.CAMERA_PERMISSIONS, clsRequestCodes.REQUEST_CAMERA_PERMISSIONS);
            return;
        }
        if (clsUtilities.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(clsUtilities.INTERNET_PERMISSIONS, clsRequestCodes.REQUEST_INTERNET_PERMISSIONS);
            return;
        }
    }

    public void displayEmpleadoLogged(String nombre) {
        TextView lblEmpleadoLogged = findViewById(R.id.lblEmpleadoLogged);
        lblEmpleadoLogged.setText(nombre);
    }
}
