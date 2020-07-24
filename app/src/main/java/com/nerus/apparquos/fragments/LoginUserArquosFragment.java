package com.nerus.apparquos.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Usuario;

import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.UsuarioViewModel;

import java.sql.BatchUpdateException;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

public class LoginUserArquosFragment extends DialogFragment implements TasksCallBacks {

    @Override
    public void onRequestBeforeStart(String fromTask) {

    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {

    }

    @Override
    public void onRequestError(String fromTask, Exception error) {
        mLoginUserArquosListener.onResultLoginUserArquosDialog(false,null);
    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {

    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        LogSNE.d("NERUS", "onFinishLoginUserArquosDialog.onRequestSuccess " + fromTask );
        mUsuario = (Usuario) response;
        mLoginUserArquosListener.onResultLoginUserArquosDialog(true, mUsuario);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public static LoginUserArquosFragment newInstance(String usrName){
        Bundle args = new Bundle();
        args.putString("USERNAME",usrName);
        LoginUserArquosFragment fragment = new LoginUserArquosFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public interface LoginUserArquosListener {
        //void onFinishLoginUserArquosDialog(Boolean successful, String user, String pwd);
        void onResultLoginUserArquosDialog(Boolean successful, Usuario usuario);
    }
    private LoginUserArquosListener mLoginUserArquosListener;
    public LoginUserArquosFragment setLoginUserArquosListener(LoginUserArquosListener loginUserArquosListener) {
        mLoginUserArquosListener = loginUserArquosListener;
        return this;
    }

    private boolean mIsLogged=false;
    private ViewModelProvider mViewModelProvider;
    private UsuarioViewModel mUsuarioViewModel;

    private TextView txtUser ;
    private TextView txtPwd ;
    private Usuario mUsuario;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.login_user_arquos_fragment,null);

        mViewModelProvider = ViewModelProviders.of(this);
        mUsuarioViewModel = mViewModelProvider.get(UsuarioViewModel.class);

        String cUserName = (String) getArguments().get("USERNAME");

        txtUser =  v.findViewById(R.id.txtUsername);
        txtPwd = v.findViewById(R.id.txtPassword);
        txtUser.setText(cUserName);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle("Usuario ARQUOS®");
        builder.setPositiveButton("ACEPTAR",  new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                String cUser = txtUser.getText().toString();
                String cPwd = txtPwd.getText().toString();
                if (cUser.length()>0 && cPwd.length()>0){
                    String cValidar =  clsFecha.getSHA1(cUser+cPwd);
                    loginUserArquos(cUser,cValidar);
                }else{
                    mLoginUserArquosListener.onResultLoginUserArquosDialog(false,null);
                }
            }
        });
        /*builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLoginUserArquosListener.onResultLoginUserArquosDialog(false,null);
            }
        });*/
        builder.setNegativeButton("CANCELAR",null);
        builder.setCancelable(false);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return  dialog;
    }
    private void loginUserArquos(String user, String pwd) {
        mUsuarioViewModel.getAuthorization(user,pwd,this);
    }




}
