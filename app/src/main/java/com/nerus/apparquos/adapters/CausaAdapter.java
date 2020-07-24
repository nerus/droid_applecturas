package com.nerus.apparquos.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.CausaNoEjecucion;

import java.util.List;

public class CausaAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<CausaNoEjecucion> data;
    private LayoutInflater inflater;

    public CausaAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        this.context = context;
        this.data = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CausaAdapter(Context context, List<CausaNoEjecucion> anomaliaList) {
        this.context = context;
        this.data = anomaliaList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        //ViewHolder holder;
        CausaNoEjecucion noEjecucion = (CausaNoEjecucion) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
        }
        TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
        TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);

        if (position == 0) {
            lblDescripcion.setText(noEjecucion.getDescripcion());
            lblRegistros.setText("Seleccione una Causa de NO ejecución.");
            lblDescripcion.setTextColor(Color.GRAY);

        } else {
            lblDescripcion.setText(noEjecucion.getIdCausa().toString() + ".-" + noEjecucion.getDescripcion());
            lblRegistros.setText(noEjecucion.getObservaciones());
            lblDescripcion.setTextColor(Color.BLACK);
        }


        return convertView;
    }

    public void setData(List<CausaNoEjecucion> noEjecuciones) {
        data = noEjecuciones;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getIdCausa();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CausaNoEjecucion noEjecucion = (CausaNoEjecucion) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
        }
        TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
        TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);

        if (position == 0) {
            lblDescripcion.setText(noEjecucion.getDescripcion());
            lblRegistros.setText("Seleccione una Causa de NO ejecución.");
            lblDescripcion.setTextColor(Color.GRAY);

        } else {
            lblDescripcion.setText(noEjecucion.getIdCausa().toString() + ".-" + noEjecucion.getDescripcion());
            lblRegistros.setText("");
            lblDescripcion.setTextColor(Color.BLACK);
        }


        return convertView;
        //return getCustomView(position, convertView, parent);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public boolean isEnabled(int position) {
        return true; // position>0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

}
