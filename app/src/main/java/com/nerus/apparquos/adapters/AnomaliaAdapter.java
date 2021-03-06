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
import com.nerus.apparquos.entities.Anomalia;


import java.util.List;

public class AnomaliaAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<Anomalia> data;
    private LayoutInflater inflater;

    public AnomaliaAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        this.context = context;
        this.data = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AnomaliaAdapter(Context context, List<Anomalia> anomaliaList) {
        this.context = context;
        this.data = anomaliaList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        //ViewHolder holder;
        Anomalia anomaliaItem = (Anomalia) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
        }
        TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
        TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);

        if (position == 0) {
            lblDescripcion.setText(anomaliaItem.getDescripcion());
            lblRegistros.setText("Seleccione una Anomalía");
            lblDescripcion.setTextColor(Color.GRAY);

        } else {
            lblDescripcion.setText(anomaliaItem.getId_anomalia().toString() + ".-" + anomaliaItem.getDescripcion());
            lblRegistros.setText(anomaliaItem.getObservaciones());
            lblDescripcion.setTextColor(Color.BLACK);
        }


        return convertView;
    }

    public void setData(List<Anomalia> anomalias) {
        data = anomalias;
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
        return data.get(position).getId_anomalia();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Anomalia anomaliaItem = (Anomalia) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
        }
        TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
        TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);

        if (position == 0) {
            lblDescripcion.setText(anomaliaItem.getDescripcion());
            lblRegistros.setText("Seleccione una Anomalía");
            lblDescripcion.setTextColor(Color.GRAY);

        } else {
            lblDescripcion.setText(anomaliaItem.getId_anomalia().toString() + ".-" + anomaliaItem.getDescripcion());
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
