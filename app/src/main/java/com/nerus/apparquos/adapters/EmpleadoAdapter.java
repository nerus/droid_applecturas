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
import com.nerus.apparquos.entities.Empleado;
import com.nerus.apparquos.entities.Usuario;

import java.util.List;

public class EmpleadoAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<Empleado> data;
    private LayoutInflater inflater;

    public EmpleadoAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        this.context = context;
        this.data = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public EmpleadoAdapter(Context context, List<Empleado> empleadoList) {
        this.context = context;
        this.data = empleadoList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        //ViewHolder holder;
        Empleado empleado = (Empleado) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
        }
        TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
        TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);

        if (position == 0 && empleado.getIdPersonal()<=0 ) {
            lblDescripcion.setText(empleado.getNombre());
            lblRegistros.setText("Seleccione uno...");
            lblDescripcion.setTextColor(Color.GRAY);

        } else {
            lblDescripcion.setText(empleado.getNombre());
            lblRegistros.setText(empleado.getMail());
            lblDescripcion.setTextColor(Color.BLACK);
        }


        return convertView;
    }

    public void setData(List<Empleado> empleados) {
        data = empleados;
        notifyDataSetChanged();
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

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
        return data.get(position).getIdPersonal();
    }

    @Override
    public boolean hasStableIds() {
        return false;
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

    @Override
    public boolean isEnabled(int position) {
        return true; // position>0;
    }

}
