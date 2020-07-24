package com.nerus.apparquos.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.AnomaliaAdapter;
import com.nerus.apparquos.adapters.EmpleadoAdapter;
import com.nerus.apparquos.entities.Anomalia;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Empleado;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.tasks.EmpleadosDownloadTask;
import com.nerus.apparquos.tasks.EmpresaDownloadTask;
import com.nerus.apparquos.tasks.LoginEmpleadoTask;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.AnomaliaViewModel;
import com.nerus.apparquos.viewmodels.CuentaViewModel;
import com.nerus.apparquos.viewmodels.EmpleadoViewModel;
import com.nerus.apparquos.viewmodels.LecturaViewModel;
import com.nerus.apparquos.viewmodels.ParametroViewModel;

import java.util.ArrayList;
import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class LoginEmpleadoFragment extends Fragment implements TasksCallBacks {
    private ViewModelProvider mViewModelProvider;

    private EmpleadoViewModel mEmpleadosViewModel;
    private ParametroViewModel mParametroViewModel;

    private Integer positionEmpleado=0;
    private List<Empleado> mEmpleadoList;
    private Empleado currentEmpleado;
    private Empleado empleadoLogged;
    private Boolean mIsLogged = false;
    private EmpleadoAdapter mEmpleadoAdapter;
    private LayoutInflater mLayoutInflater;
    private Button mCmdLogin;
    private AppArquos mAppArquos;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clsUtilities.setHomeBack(getActivity(),"INICIAR SESION");
        mAppArquos = (AppArquos) getActivity().getApplicationContext();
        empleadoLogged = mAppArquos.getEmpleado();

        mViewModelProvider = ViewModelProviders.of(this);

        mEmpleadosViewModel = mViewModelProvider.get(EmpleadoViewModel.class);
        mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);
        mIsLogged = false;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        final View v = inflater.inflate(R.layout.login_empleado_fragment, container, false);


        Spinner cmbEmpleados = v.findViewById(R.id.cmbEmpleados);
        cmbEmpleados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionEmpleado=position;
                currentEmpleado = mEmpleadoList.get(positionEmpleado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCmdLogin = v.findViewById(R.id.cmdAceptar);
        mCmdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdLogin_onClick(v);
            }
        });

        return v;
    }

    private void cmdLogin_onClick(View v) {
        mCmdLogin.setEnabled(false);
        clsUtilities.hideKeyboard(getActivity());
        FragmentActivity rootView = getActivity();
        String cPassword = ((TextView) rootView.findViewById(R.id.txtPassword)).getText().toString();
        Integer idPersonal=0;

        if (currentEmpleado!=null){
            idPersonal=currentEmpleado.getIdPersonal();
        }

        if (idPersonal>0){
            if (cPassword.length()>0){

                String validar = clsFecha.getSHA1(cPassword);
                mIsLogged = true;
                mEmpleadosViewModel.getAuthorization(currentEmpleado,validar, this);

            }else{
                Toast toast = Toast.makeText(v.getContext(), "Debe capturar la contraseña.", Toast.LENGTH_LONG);
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(v.getContext(), "Seleccione un empleado y capture su contraseña", Toast.LENGTH_LONG);
            toast.show();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Iniciar Sesión");



        //this.setObservable();

        mEmpleadosViewModel.downloadEmpleados(this);


    }

    private void setObservable() {
        mEmpleadosViewModel.getEmpleados().observe(this, new Observer<List<Empleado>>() {
            @Override
            public void onChanged(@Nullable final List<Empleado> empleados) {
                // Update the cached copy of the subsistemas in the adapter.
                update_cmbEmpleados(empleados);
            }
        });

    }


    private void show_IdEmpleado(Empleado empleado) {
        Integer idPersonal=0;
        if (empleado!=null){
            idPersonal=empleado.getIdPersonal();
        }

        if (idPersonal>0){
            if (mEmpleadoList!=null && mEmpleadoList.size()>0){
                for (int i = 0; i < mEmpleadoList.size(); i++) {
                    Empleado item = mEmpleadoList.get(i);
                    if (item.getIdPersonal()==idPersonal){
                        positionEmpleado=i;
                        Spinner cmbAnom = getActivity().findViewById(R.id.cmbEmpleados);
                        if (positionEmpleado>0 && positionEmpleado<mEmpleadoList.size()){
                            cmbAnom.setSelection(positionEmpleado);
                        }
                        break;
                    }
                }
            }
        }
    }
    private void update_cmbEmpleados(List<Empleado> empleados) {
        mEmpleadoList = empleados;
        if (mEmpleadoList.size() > 0) {
            mEmpleadoAdapter = new EmpleadoAdapter(getContext(), mEmpleadoList);
            mEmpleadoAdapter.setData(mEmpleadoList);
            Spinner cmbEmpleados = getActivity().findViewById(R.id.cmbEmpleados);
            cmbEmpleados.setAdapter(mEmpleadoAdapter);
            show_IdEmpleado(currentEmpleado);
        }
        /*
        if (mIsLogged){
            String cSaludo = clsFecha.getSalute(currentEmpleado.getNombre());
            Snackbar.make(getView(), cSaludo, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            getActivity().getSupportFragmentManager().popBackStack();

        }*/

        //LogSNE.d("NERUS", "The onChanged  is: " + mAnomaliasList.toString());
    }


    @Override
    public void onRequestBeforeStart(String fromTask) {

    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {

    }

    @Override
    public void onRequestError(String fromTask, Exception error) {
        LogSNE.d("NERUS",error.getMessage());
        Empleado empleado = new Empleado(-1,"SIN CONEXION","","",0,0);
        List<Empleado> list = new ArrayList<Empleado>();
        list.add(empleado);
        update_cmbEmpleados(list);
        mCmdLogin.setEnabled(true);
    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {

    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        if (fromTask.equals(EmpleadosDownloadTask.TASK_NAME)){
            List<Empleado> empleados = (List<Empleado>) response;
            update_cmbEmpleados(empleados);
        }
        if (fromTask.equals(LoginEmpleadoTask.TASK_NAME)){
            Empleado empleado = (Empleado) response;
            mAppArquos.setEmpleado(empleado);
            mAppArquos.setTokenUser(empleado.getToken());

            String cSaludo = clsFecha.getSalute(empleado.getNombre());
            Snackbar.make(getView(), cSaludo, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }

    }
}
