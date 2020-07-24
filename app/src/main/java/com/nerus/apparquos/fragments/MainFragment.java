package com.nerus.apparquos.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.MainActivity;
import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Empleado;
import com.nerus.apparquos.entities.Empresa;
import com.nerus.apparquos.entities.Material;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.tasks.EmpresaDownloadTask;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.AnomaliaViewModel;
import com.nerus.apparquos.viewmodels.CausaNoEjecucionViewModel;
import com.nerus.apparquos.viewmodels.EmpleadoViewModel;
import com.nerus.apparquos.viewmodels.MaterialViewModel;
import com.nerus.apparquos.viewmodels.ParametroViewModel;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

import java.math.BigInteger;

public class MainFragment extends Fragment implements TasksCallBacks {
    private static Integer nCount =1;
    private AppArquos mAppArquos;
    private TextView mlblRFC;
    private TextView mlblRazonSocial;
    private TextView mlblDireccion;
    private TextView mlblFecha;
    private ViewModelProvider mViewModelProvider;
    private ParametroViewModel mParametroViewModel;
    private AnomaliaViewModel mAnomaliaViewModel;
    private CausaNoEjecucionViewModel mCausaViewModel;
    private EmpleadoViewModel mEmpleadoViewModel;
    private Boolean askConfigurar = false;
    private MaterialViewModel mMaterialViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAppArquos = (AppArquos) getActivity().getApplicationContext();
        LogSNE.d("NERUS", "MainFragment.onCreate" );

        mViewModelProvider = ViewModelProviders.of(this);
        mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);
        mAnomaliaViewModel = mViewModelProvider.get(AnomaliaViewModel.class);
        mCausaViewModel = mViewModelProvider.get(CausaNoEjecucionViewModel.class);
        mEmpleadoViewModel = mViewModelProvider.get(EmpleadoViewModel.class);
        mMaterialViewModel= mViewModelProvider.get(MaterialViewModel.class);
        //lookPermissions();
    }
    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Arquos®");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        LogSNE.d("NERUS", "MainFragment.onStart "+ nCount.toString() );
        this.setObservables();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogSNE.d("NERUS", "MainFragment.onResume " + nCount.toString() );


        if (askConfigurar){
            //this.showConfigurar();
        }else{
            //downloadEmpresa();
        }

        /*
        if (mAppArquos.getEmpleado()==null){
            this.showLoginEmpleado();
        }
        */
        LogSNE.d("NERUS","Count: " + nCount.toString() );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogSNE.d("NERUS", "MainFragment.onCreateView" );
        View v = inflater.inflate(R.layout.main_fragment,container,false);
        mlblRFC = v.findViewById(R.id.lblRFC);
        mlblRazonSocial = v.findViewById(R.id.lblRazonSocial);
        mlblDireccion = v.findViewById(R.id.lblDireccion);
        mlblFecha = v.findViewById(R.id.lblFecha);

        Button cmdGetRutas = v.findViewById(R.id.cmdRutas);
        cmdGetRutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LogSNE.d("NERUS", "cmdGetRutas.onClick: " );
                if (mAppArquos.getEmpleado()!=null && mAppArquos.getEmpleado().getIdPersonal()>0 && mAppArquos.getEmpleado().getAbcLecturas()==1){
                    FragmentManager fm = getActivity(). getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.content_getruta);
                    if (fragment == null) {
                        fragment = new GetRutasFragment();
                        fm.beginTransaction()
                                .replace(R.id.content_main,fragment)
                                .addToBackStack("content_main")
                                .commit();
                    }
                }else if (mAppArquos.getEmpleado()==null || mAppArquos.getEmpleado().getIdPersonal()<=0 ) {
                    Snackbar.make(getView(), "Debe tener sesion iniciada un trabajador.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if (mAppArquos.getEmpleado().getAbcLecturas()!=1){
                    Snackbar.make(getView(), "El trabajador no tiene acceso a esta opción.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

/*
        Button cmdEmpleado = v.findViewById(R.id.cmdEmpleado);
        cmdEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LogSNE.d("NERUS", "cmdGetRutas.onClick: " );
                showLoginEmpleado();
            }
        });
*/

        Button cmdCaptura = v.findViewById(R.id.cmdCaptura);
        cmdCaptura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LogSNE.d("NERUS", "cmdGetRutas.onClick: " );

                if (mAppArquos.getEmpleado()!=null && mAppArquos.getEmpleado().getIdPersonal()>0 && mAppArquos.getEmpleado().getAbcLecturas()==1){
                    FragmentManager fm = getActivity(). getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.content_getruta);
                    if (fragment == null) {
                        fragment = new GetRutaLecturarFragment();
                        fm.beginTransaction()
                                .replace(R.id.content_main,fragment)
                                .addToBackStack("content_main")
                                .commit();
                    }
                }else if (mAppArquos.getEmpleado()==null || mAppArquos.getEmpleado().getIdPersonal()<=0 ) {
                    Snackbar.make(getView(), "Debe tener sesion iniciada un trabajador.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if (mAppArquos.getEmpleado().getAbcLecturas()!=1){
                    Snackbar.make(getView(), "El trabajador no tiene acceso a esta opción.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        Button cmdGetOrdenes = v.findViewById(R.id.cmdGetOrdenes);
        cmdGetOrdenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LogSNE.d("NERUS", "cmdGetRutas.onClick: " );
                if (mAppArquos.getEmpleado()!=null && mAppArquos.getEmpleado().getIdPersonal()>0 && mAppArquos.getEmpleado().getAbcOrdenes()==1){
                    FragmentManager fm = getActivity(). getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.content_getruta);
                    if (fragment == null) {
                        fragment = new ListResumenOTFragment();
                        fm.beginTransaction()
                                .replace(R.id.content_main,fragment)
                                .addToBackStack("content_main")
                                .commit();
                    }
                }else if (mAppArquos.getEmpleado()==null || mAppArquos.getEmpleado().getIdPersonal()<=0 ) {
                    Snackbar.make(getView(), "Debe tener sesion iniciada un trabajador.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if (mAppArquos.getEmpleado().getAbcOrdenes()!=1){
                    Snackbar.make(getView(), "El trabajador no tiene acceso a esta opción.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        return v;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mAppArquos.getEmpleado()==null){
            menu.getItem(1).setTitle("Iniciar Sesion");
        }else{
            final String fullname = mAppArquos.getEmpleado().getNombre();
            menu.getItem(1).setTitle("Cerrar Sesion de " + fullname.split(" ")[0]);
        }

        LogSNE.d("NERUS","MainFragment.onPrepareOptionsMenu"  );
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        //menu.getItem(0).setTitle("Cerrar Session");
        LogSNE.d("NERUS","MainFragment.onCreateOptionsMenu");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //LogSNE.d("NERUS",item.toString());
            showConfigurar();
            return true;
        }else if (id == R.id.action_login) {
            showLoginEmpleado();
            return true;
        }else if (id == R.id.action_about) {

            FragmentManager fm = getActivity(). getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.content_getruta);
            if (fragment == null) {
                fragment = new AboutFragment();
                fm.beginTransaction()
                        .replace(R.id.content_main,fragment)
                        .addToBackStack("content_main")
                        .commit();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showConfigurar() {
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            fragment = new ConfigurarFragment();
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("content_main")
                    .commit();
        }

    }

    private void showLoginEmpleado() {
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            fragment = new LoginEmpleadoFragment();
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("content_main")
                    .commit();
        }
    }

    private void downloadEmpresa(){
        EmpresaDownloadTask empresaDownloadTask = new EmpresaDownloadTask(mAppArquos.getUrlApi(), mAppArquos.getTokenApp(), mAppArquos.getTokenUser());
        empresaDownloadTask.setCallBacks(this);
        empresaDownloadTask.execute();
    }
    private void setObservables() {
        mEmpleadoViewModel.getEmpleadoLogged().removeObservers(this);
        mEmpleadoViewModel.getEmpleadoLogged().observe(this, new Observer<Empleado>() {
            @Override
            public void onChanged(@Nullable final Empleado empleado) {
                // Update the cached copy of the subsistemas in the adapter.
                LogSNE.d("NERUS","getEmpleadoLogged: ");
                if (empleado!=null){
                    mAppArquos.setEmpleado(empleado);
                    ((MainActivity) getActivity()).displayEmpleadoLogged(empleado.getNombre());
                    mAppArquos.setTokenUser(empleado.getToken());


                }else{
                    mAppArquos.setEmpleado(null);
                    mAppArquos.setTokenUser("");
                    ((MainActivity) getActivity()).displayEmpleadoLogged("");
                }
            }
        });
        mParametroViewModel.getApiUrl().removeObservers(this);
        mParametroViewModel.getApiUrl().observe(this, new Observer<Parametro>() {
            @Override
            public void onChanged(@Nullable final Parametro parametro) {
                // Update the cached copy of the subsistemas in the adapter.
                nCount = nCount + 1;
                if (parametro!=null){
                    mAppArquos.setUrlApi( parametro.getValor() );
                    LogSNE.d("NERUS","setUrlApi: "+parametro.getValor());
                    askConfigurar=false;
                    downloadEmpresa();
                }else{
                    LogSNE.d("NERUS","LANZAR ACTIVITY DE CONFIGURACON");
                    //showConfigurar();
                    askConfigurar=true;
                }
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
        if (response instanceof Empresa) {
            Empresa empresa = (Empresa) response;
            //if (mRFC.getText().toString().equals(empresa.getRFC())) {
                mlblDireccion.setText(empresa.getDireccion());
                mlblRazonSocial.setText(empresa.getDescripcion());
                mlblRFC.setText(empresa.getRFC());
            //}else{
            //    mRFC.setError("RFC no autorizado.");
            //}
            mlblFecha.setText(empresa.getFecha());
            String cValidar =  clsFecha.getSHA1(empresa.getRFC()+getString(R.string.appkey));
            mAppArquos.setTokenApp(cValidar);
            mAppArquos.setEmpresa(empresa);

            mMaterialViewModel.getMaterial("-1").removeObservers(this);
            mMaterialViewModel.getMaterial("-1").observe(this, new Observer<Material>() {
                @Override
                public void onChanged(@Nullable final Material material) {
                    // Update the cached copy of the subsistemas in the adapter.
                    nCount = nCount + 1;
                    if (material!=null){
                        long cFecha = Long.valueOf( material.getFechaDownloaded().replace("-","").replace(":","").replace(" ","") );
                        long cHoy = Long.valueOf( clsFecha.getCurrentDate().replace("-","").replace(":","").replace(" ","") );
                        long dif = cHoy - cFecha ;
                        LogSNE.d("NERUS","HOY: "+cHoy+" down: " + cFecha + " dife: "+ dif);
                        //bajar cats si ya pasaron mas de 10 minutos
                        if (dif>1000){
                            downLoadMateriales();
                            mAnomaliaViewModel.downloadAnomalias();
                            mCausaViewModel.downloadCausasNoEjecucion();
                        }
                    }else{
                        LogSNE.d("NERUS","es null material-primera vez que se baja el cat");
                        downLoadMateriales();
                        mAnomaliaViewModel.downloadAnomalias();
                        mCausaViewModel.downloadCausasNoEjecucion();
                    }
                }
            });

        }
    }

    private void downLoadMateriales(){
        mMaterialViewModel.downloadMateriales(this);
    }
}
