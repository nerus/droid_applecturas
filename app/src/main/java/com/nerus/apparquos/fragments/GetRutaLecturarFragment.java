package com.nerus.apparquos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrinterId;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.RutaAdapter;
import com.nerus.apparquos.adapters.SectorAdapter;
import com.nerus.apparquos.adapters.SubsistemaAdapter;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.entities.Ruta;
import com.nerus.apparquos.entities.Sector;
import com.nerus.apparquos.entities.Subsistema;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.tasks.LoginUserArquosTask;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.CuentaViewModel;
import com.nerus.apparquos.viewmodels.ParametroViewModel;
import com.nerus.apparquos.viewmodels.RutaViewModel;
import com.nerus.apparquos.viewmodels.SectorViewModel;
import com.nerus.apparquos.viewmodels.SubsistemaViewModel;
import com.nerus.apparquos.viewmodels.UsuarioViewModel;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class GetRutaLecturarFragment extends Fragment implements AdapterView.OnItemSelectedListener , TasksCallBacks {
    private ViewModelProvider mViewModelProvider;
    private CuentaViewModel mCuentaViewModel;
    private UsuarioViewModel mUsuarioViewModel;

    private List<Ruta> mRutaList;
    private Ruta currentRuta;
    private RutaAdapter mRutaAdapter;

    private Integer positionRuta = 0;
    //private Usuario mUsuario;
    private String cCurrentAccion = "";
    private ParametroViewModel mParametroViewModel;


    private Boolean mLoaded = false;
    private Usuario mUsuario;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mViewModelProvider = ViewModelProviders.of(this);
        mUsuarioViewModel = mViewModelProvider.get(UsuarioViewModel.class);
        mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);
        mCuentaViewModel = mViewModelProvider.get(CuentaViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        clsUtilities.setHomeBack(getActivity(),"Iniciar Captura");


        this.setObservable();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //LogSNE.d("NERUS","SALVAR LA RUTA "+ mRutaList.get(positionRuta).getDescripcion());
        if (positionRuta>0){
            mCuentaViewModel.saveLastRuta(mRutaList.get(positionRuta));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.getruta_lecturar_fragment, container, false);


        Spinner cmbRuta = v.findViewById(R.id.spnRuta);
        cmbRuta.setOnItemSelectedListener(this);

        final Button cmdPadron = v.findViewById(R.id.cmdPadron);
        cmdPadron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdPadron_onClick(v);
            }
        });

        final Button cmdDelete = v.findViewById(R.id.cmdDeleteRuta);
        cmdDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdDelete_onClick(v);
            }
        });

        final Button cmdSubirRuta = v.findViewById(R.id.cmdSubirRuta);
        cmdSubirRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdSubirRuta_onClick(v);
            }
        });

        return v;
    }

    private void cmdSubirRuta_onClick(View v) {
        String cMsg = "";
        if (currentRuta != null && currentRuta.getIdRuta() > 0) {
            LogSNE.d("NERUS", "cmdSubirRuta_onClick " + currentRuta.toString());
            String cUsario = "";
            if (mUsuario!=null) cUsario=mUsuario.getUsuario();

            FragmentManager fm = getActivity(). getSupportFragmentManager();
            final LoginUserArquosFragment loginUserArquos =  LoginUserArquosFragment.newInstance( cUsario );
            loginUserArquos.setTargetFragment(GetRutaLecturarFragment.this, clsRequestCodes.REQUEST_LOGINUSRARQUOS);
            loginUserArquos.setLoginUserArquosListener(new LoginUserArquosFragment.LoginUserArquosListener() {
                @Override
                public void onResultLoginUserArquosDialog (Boolean successful, Usuario usuario) {
                    if (successful){
                        LogSNE.d("NERUS", "Usuario:" + usuario.toString());
                        cCurrentAccion = "SUBIR_RUTA";
                        subirRuta(currentRuta, usuario);
                    }else{
                        Snackbar.make(getView(), "Debe capturar un usuario y contraseña de Arquos®", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
            loginUserArquos.show(fm,"DIALOG_LOGINUSERARQUOS");

        } else if (currentRuta == null || currentRuta.getIdRuta() < 0) {
            cMsg = "Debe seleccionar una Ruta";
        }
        if (cMsg.length() > 0) {
            Snackbar.make(v, cMsg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }


    private void cmdPadron_onClick(View v) {
        String cMsg = "";
        if (currentRuta != null && currentRuta.getIdRuta() > 0) {
            LogSNE.d("NERUS", "cmdPadron_onClick " + currentRuta.toString());
            FragmentManager fm = getActivity(). getSupportFragmentManager();

            Fragment fragment = fm.findFragmentById(R.id.content_getruta);
            if (fragment == null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("RUTA",currentRuta);

                fragment = new ListCuentasFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction()
                        .replace(R.id.content_main,fragment)
                        .addToBackStack("CAPTURAR_LECTURA")
                        .commit();
            }
        } else if (currentRuta == null || currentRuta.getIdRuta() < 0) {
            cMsg = "Debe seleccionar una Ruta";
        }
        if (cMsg.length() > 0) {
            Snackbar.make(v, cMsg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    private void cmdDelete_onClick(View v) {
        String cMsg = "";
        if (currentRuta != null && currentRuta.getIdRuta() > 0) {
            LogSNE.d("NERUS", "cmdDelete_onClick " + currentRuta.toString());
            FragmentManager fm = getActivity(). getSupportFragmentManager();
            String cUsario = "";
            if (mUsuario!=null) cUsario=mUsuario.getUsuario();
            final LoginUserArquosFragment loginUserArquos =  LoginUserArquosFragment.newInstance(cUsario);
            loginUserArquos.setTargetFragment(GetRutaLecturarFragment.this, clsRequestCodes.REQUEST_LOGINUSRARQUOS);
            loginUserArquos.setLoginUserArquosListener(new LoginUserArquosFragment.LoginUserArquosListener() {
                @Override
                public void onResultLoginUserArquosDialog (Boolean successful, Usuario usuario) {
                    if (successful){
                        cCurrentAccion = "ELIMINAR_RUTA";
                        Snackbar.make(getView(), "Eliminando ruta "+usuario.getNombre(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        eliminarRuta(currentRuta);
                    }else{
                        Snackbar.make(getView(), "Debe capturar un usuario y contraseña de Arquos®", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
            loginUserArquos.show(fm,"DIALOG_LOGINUSERARQUOS");

        } else if (currentRuta == null || currentRuta.getIdRuta() < 0) {
            cMsg = "Debe seleccionar una Ruta";
        }
        if (cMsg.length() > 0) {
            Snackbar.make(v, cMsg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view != null) {
            if (parent.getId() == R.id.spnRuta) {
                currentRuta = (Ruta) mRutaAdapter.getItem(position);
                positionRuta = position;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void update_spnRuta(List<Ruta> rutas) {
        LogSNE.d("NERUS", "The onChanged  is: " + rutas.toString());
        mRutaList = rutas;
        if (mRutaList.size() > 0) {
            mRutaAdapter = new RutaAdapter(getContext(), mRutaList);
            mRutaAdapter.setData(mRutaList);
            Spinner cmbRuta = getActivity().findViewById(R.id.spnRuta);
            cmbRuta.setAdapter(mRutaAdapter);
        }
        if (!mLoaded){
            mParametroViewModel.getLastRuta().observe(this, new Observer<Parametro>() {
                @Override
                public void onChanged(@Nullable final Parametro parametro) {
                    mLoaded=true;
                    // Update the cached copy of the subsistemas in the adapter.
                    if (parametro != null && mRutaList!=null && mRutaList.size() > 0) {
                        LogSNE.d("NERUS",parametro.toString());
                        Integer idRuta = Integer.valueOf(parametro.getValor());
                        Integer nCnt = 0;
                        for (Integer i=0; i<mRutaList.size(); i++){
                            Ruta item = mRutaList.get(i);
                            if (idRuta.equals(item.getIdRuta())){
                                positionRuta=i;
                                currentRuta=item;
                                break;
                            }
                        }
                        if (positionRuta>0){
                            Spinner cmbRuta = getActivity().findViewById(R.id.spnRuta);
                            cmbRuta.setSelection(positionRuta);
                        }
                    }
                }
            });

        }

        if (cCurrentAccion == "ELIMINAR_RUTA"){
            cCurrentAccion ="";
            Snackbar.make(getView(), "Ruta eliminada", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
    }
    public void setObservable() {
        mCuentaViewModel.getRutas().observe(this, new Observer<List<Ruta>>() {
            @Override
            public void onChanged(@Nullable final List<Ruta> rutas) {
                // Update the cached copy of the subsistemas in the adapter.
                update_spnRuta(rutas);
                if (positionRuta>0){
                    Spinner cmbRuta = getActivity().findViewById(R.id.spnRuta);
                    cmbRuta.setSelection(positionRuta);
                }
            }
        });
        mUsuarioViewModel.getUsuario().observe(this,new Observer<Usuario>(){
            @Override
            public void onChanged(@Nullable final Usuario usuario) {
                mUsuario = usuario;
            }

        });


    }

    private void eliminarRuta(Ruta currentRuta) {
        positionRuta = 0;
        mCuentaViewModel.deleteRuta(currentRuta);
    }
    private void subirRuta(Ruta currentRuta, Usuario usuario) {
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("RUTA",currentRuta);
            bundle.putSerializable("USUARIO",usuario);

            fragment = new UpLoadLecturasFragment();
            fragment.setTargetFragment(this, clsRequestCodes.REQUEST_UPLOADLECTURAS);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("UPLOAD_LECTURA")
                    .commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=Activity.RESULT_OK){
            return;
        }
        LogSNE.d("NERUS","GetRutaLecturarFragment.onActivityResult :" +  String.valueOf(resultCode)) ;
        if (requestCode == clsRequestCodes.REQUEST_UPLOADLECTURAS){
            List<Lectura> lecturas = (List<Lectura>) data.getSerializableExtra("LECTURAS");
            mCuentaViewModel.setUpLoadedLecturas(lecturas,this);
            //TODO Eliminar las lecturas subidas
        }
    }


    @Override
    public void onRequestBeforeStart(String fromTask) {

    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {

    }

    @Override
    public void onRequestError(String fromTask, Exception error) {
        LogSNE.d("NERUS",fromTask+".onRequestError");
    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {

    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        LogSNE.d("NERUS",fromTask+".onRequestSuccess");
    }
}
