package com.nerus.apparquos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Subsistema;

import java.util.ArrayList;
import java.util.List;

public class SubsistemaAdapter extends ArrayAdapter<Subsistema> {
    private Context context;
    private List data;
    private LayoutInflater inflater;

    public SubsistemaAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SubsistemaAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SubsistemaAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
    }

    public SubsistemaAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SubsistemaAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.data=objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SubsistemaAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        } else {
            return true;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        //View row = inflater.inflate(R.layout.spinner_rows, parent, false);

        /***** Get each Model object from Arraylist ********/
        Subsistema tempValues = (Subsistema) data.get(position);
        ViewHolder holder;


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
            holder = new ViewHolder();
            holder.lblDescripcion = (TextView) convertView.findViewById(R.id.lblDescripcion);
            holder.lblRegistros = (TextView) convertView.findViewById(R.id.lblRegistros);
            holder.position = position;
            holder.value = tempValues.getSb();
            convertView.setTag(R.string.item_tag, holder);
        } else
            holder = (ViewHolder) convertView.getTag(R.string.item_tag);

        // Set values for spinner each row
        holder.lblDescripcion.setText(tempValues.getDescripcion());
        holder.lblRegistros.setText(tempValues.getRegistros() + " Registros");
        holder.value = tempValues.getSb();
        holder.position = position;

        if (position == 0) {
            // Set the hint text color gray
            holder.lblDescripcion.setTextColor(Color.GRAY);
            holder.lblRegistros.setText("Seleccione un Subsistema");
            //holder.lblRegistros.setVisibility(View.GONE);
        } else {
            holder.lblDescripcion.setTextColor(Color.BLACK);
        }



        return convertView;
    }

    public void setData(List<Subsistema> subsistemas) {
        data = subsistemas;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView lblRegistros;
        TextView lblDescripcion;
        int position;
        public Integer value;
    }

}
