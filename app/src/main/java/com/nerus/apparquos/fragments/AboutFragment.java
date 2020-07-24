package com.nerus.apparquos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.Ruta;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.tasks.LecturaFileCsvTask;
import com.nerus.apparquos.tasks.LecturaUpLoadTask;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.CuentaViewModel;
import com.nerus.apparquos.viewmodels.LecturaViewModel;

import org.w3c.dom.Text;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public final class AboutFragment extends Fragment  {
    private LayoutInflater mLayoutInflater;

    public static AboutFragment newInstance(String usrName){
        Bundle args = new Bundle();
        args.putString("USERNAME",usrName);
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

    }

    @Override
    public void onStart() {
        super.onStart();

        clsUtilities.setHomeBack(getActivity(),"Acerca de...");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayoutInflater = inflater;
        final View v = mLayoutInflater.inflate(R.layout.about_fragment, container, false);

        final String cFecha = clsFecha.getFechaYMD();
        TextView textView = v.findViewById(R.id.dateAbout);

        textView.setText("2013-"+ cFecha.substring(0,4) +" Soluciones Nerus SA de CV");

        return v;
    }

}
