package com.nerus.apparquos.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.UsuarioViewModel;

public class MessageBoxFragment extends DialogFragment   {

    private String mMensaje="¿ Desea continuar ?";
    private String mName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public static MessageBoxFragment newInstance(String msg, String name){
        Bundle args = new Bundle();
        args.putString("MENSAJE",msg);
        args.putString("NOMBRE",name);
        MessageBoxFragment fragment = new MessageBoxFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public interface MessageBoxListener {
        //void onFinishLoginUserArquosDialog(Boolean successful, String user, String pwd);
        void onResultMessageBoxDialog(Boolean successful);
    }
    private MessageBoxListener mMessageBoxListener;
    public MessageBoxFragment setMessageBoxListener(MessageBoxListener listener) {
        mMessageBoxListener = listener;
        return this;
    }


    private TextView txtMensaje ;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.message_box_fragment,null);


        mMensaje = (String) getArguments().get("MENSAJE");
        mName = (String) getArguments().get("NOMBRE");

        txtMensaje =  v.findViewById(R.id.txtMessage);
        txtMensaje.setText(mMensaje);
        ((TextView) v.findViewById(R.id.lblCaption)).setText(mName);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle("ARQUOS®");
        builder.setPositiveButton("ACEPTAR",  new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                mMessageBoxListener.onResultMessageBoxDialog(true);
            }
        });
        builder.setNegativeButton("CANCELAR",  new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                mMessageBoxListener.onResultMessageBoxDialog(false);
            }
        });
        builder.setCancelable(false);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return  dialog;
    }



}
