package com.nerus.apparquos.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Ruta;

import java.util.ArrayList;
import java.util.List;

public class RutaAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<Ruta> data;
    private LayoutInflater inflater;

    public RutaAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        this.context = context;
        this.data=objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public RutaAdapter(Context context, List<Ruta> rutaList) {
        this.context = context;
        this.data=rutaList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
/*
        Ruta rutaItem = (Ruta) getItem(position);
        convertView = inflater.inflate(R.layout.spinner_row, parent, false);
        TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
        TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);

        if (position==0){
            lblDescripcion.setText(rutaItem.getDescripcion());
            lblRegistros.setText("Seleccione una Ruta");
            lblDescripcion.setTextColor(Color.GRAY);

        }else{
            lblDescripcion.setText(rutaItem.getDescripcion());
            lblRegistros.setText(rutaItem.getRegistros().toString() + " Registros");
            lblDescripcion.setTextColor(Color.BLACK);
        }

        return convertView;
*/
        return getCustomView(position, convertView, parent);
        //return getDropDownView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
/*
        Ruta rutaItem = (Ruta) getItem(position);
        if (convertView==null){
            convertView = inflater.inflate(R.layout.ruta_row, parent, false);
        }

        TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
        TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);
        TextView txtEnviados = convertView.findViewById(R.id.txtRegSend);
        TextView txtCapturados  = convertView.findViewById(R.id.txtRegCapt);
        TextView txtFijos = convertView.findViewById(R.id.txtFijos);
        TextView txtMedidos = convertView.findViewById(R.id.txtMedidos);
        TextView txtPromedios = convertView.findViewById(R.id.txtPromedios);
        TextView txtObservaciones = convertView.findViewById(R.id.txtObservaciones);

        ((TextView) convertView.findViewById(R.id.hdCapt)).setVisibility(View.INVISIBLE);
        ((TextView) convertView.findViewById(R.id.hdSend)).setVisibility(View.INVISIBLE);
        ((TextView) convertView.findViewById(R.id.hdFijos)).setVisibility(View.INVISIBLE);
        ((TextView) convertView.findViewById(R.id.hdMedidos)).setVisibility(View.INVISIBLE);
        ((TextView) convertView.findViewById(R.id.hdMedidos)).setVisibility(View.INVISIBLE);
        ((TextView) convertView.findViewById(R.id.hdPromedios)).setVisibility(View.INVISIBLE);
        ((TextView) convertView.findViewById(R.id.hdFijos)).setVisibility(View.INVISIBLE);
        ((TextView) convertView.findViewById(R.id.hdTotal)).setVisibility(View.INVISIBLE);

        if (position==0){
            lblDescripcion.setTextColor(Color.GRAY);
            lblDescripcion.setText(rutaItem.getDescripcion());
            txtObservaciones.setText("Seleccione una Ruta");

            lblRegistros.setVisibility(View.INVISIBLE);
            txtEnviados.setVisibility(View.INVISIBLE);
            txtCapturados.setVisibility(View.INVISIBLE);
            txtFijos.setVisibility(View.INVISIBLE);
            txtMedidos.setVisibility(View.INVISIBLE);
            txtPromedios.setVisibility(View.INVISIBLE);

        }else{
            lblDescripcion.setTextColor(Color.BLACK);
            lblDescripcion.setText(rutaItem.getDescripcion());
            txtObservaciones.setText(rutaItem.getObservaciones());
            lblRegistros.setText(rutaItem.getRegistros().toString());
            txtEnviados.setText(rutaItem.getEnviadas().toString());
            txtCapturados.setText(rutaItem.getCapturadas().toString());
            txtFijos.setText(rutaItem.getFijos().toString());
            txtMedidos.setText(rutaItem.getMedidos().toString());
            txtPromedios.setText(rutaItem.getPromedios().toString());

            lblRegistros.setVisibility(View.VISIBLE);
            txtEnviados.setVisibility(View.VISIBLE);
            txtCapturados.setVisibility(View.VISIBLE);
            txtFijos.setVisibility(View.VISIBLE);
            txtMedidos.setVisibility(View.VISIBLE);
            txtPromedios.setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.hdCapt)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.hdSend)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.hdFijos)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.hdMedidos)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.hdMedidos)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.hdPromedios)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.hdFijos)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.hdTotal)).setVisibility(View.VISIBLE);

        }

        return convertView;
*/
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        //ViewHolder holder;
        Ruta rutaItem = (Ruta) getItem(position);
        if (getItemViewType(position)==0){
            //if (convertView==null){
                convertView = inflater.inflate(R.layout.spinner_row, parent, false);
            //}
            TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
            TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);

            if (position==0){
                lblDescripcion.setText(rutaItem.getDescripcion());
                lblRegistros.setText("Seleccione una Ruta");
                lblDescripcion.setTextColor(Color.GRAY);

            }else{
                lblDescripcion.setText(rutaItem.getDescripcion());
                lblRegistros.setText(rutaItem.getRegistros().toString() + " Registros");
                lblDescripcion.setTextColor(Color.BLACK);
            }


            /*
                holder = new ViewHolder();
                holder.isHint=true;
                holder.mHintHolder = new ViewHolder.HintHolder();
                holder.mHintHolder.lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
                holder.mHintHolder.lblRegistros = convertView.findViewById(R.id.lblRegistros);
                convertView.setTag(R.string.item_tag,holder);
            }else{
                holder = (ViewHolder) convertView.getTag(R.string.item_tag);
            }
            holder.position=position;
            holder.value=-1;
            */
        }else{
            //if (convertView==null) {
                convertView = inflater.inflate(R.layout.ruta_row, parent, false);
            //}
            TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
            TextView lblRegistros = convertView.findViewById(R.id.lblTotRegistros);
            TextView txtEnviados = convertView.findViewById(R.id.txtRegSend);
            TextView txtCapturados  = convertView.findViewById(R.id.txtRegCapt);
            TextView txtFijos = convertView.findViewById(R.id.txtFijos);
            TextView txtMedidos = convertView.findViewById(R.id.txtMedidos);
            TextView txtPromedios = convertView.findViewById(R.id.txtPromedios);
            TextView txtObservaciones = convertView.findViewById(R.id.txtObservaciones);

            ((TextView) convertView.findViewById(R.id.hdCapt)).setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.hdSend)).setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.hdFijos)).setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.hdMedidos)).setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.hdMedidos)).setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.hdPromedios)).setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.hdFijos)).setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.hdTotal)).setVisibility(View.INVISIBLE);

            if (position==0){
                lblDescripcion.setTextColor(Color.GRAY);
                lblDescripcion.setText(rutaItem.getDescripcion());
                txtObservaciones.setText("Seleccione una Ruta");

                lblRegistros.setVisibility(View.INVISIBLE);
                txtEnviados.setVisibility(View.INVISIBLE);
                txtCapturados.setVisibility(View.INVISIBLE);
                txtFijos.setVisibility(View.INVISIBLE);
                txtMedidos.setVisibility(View.INVISIBLE);
                txtPromedios.setVisibility(View.INVISIBLE);

            }else{
                lblDescripcion.setTextColor(Color.BLACK);
                lblDescripcion.setText(rutaItem.getDescripcion());
                txtObservaciones.setText(rutaItem.getObservaciones());
                lblRegistros.setText(rutaItem.getRegistros().toString());
                txtEnviados.setText(rutaItem.getEnviadas().toString());
                txtCapturados.setText(rutaItem.getCapturadas().toString());
                txtFijos.setText(rutaItem.getFijos().toString());
                txtMedidos.setText(rutaItem.getMedidos().toString());
                txtPromedios.setText(rutaItem.getPromedios().toString());

                lblRegistros.setVisibility(View.VISIBLE);
                txtEnviados.setVisibility(View.VISIBLE);
                txtCapturados.setVisibility(View.VISIBLE);
                txtFijos.setVisibility(View.VISIBLE);
                txtMedidos.setVisibility(View.VISIBLE);
                txtPromedios.setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.hdCapt)).setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.hdSend)).setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.hdFijos)).setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.hdMedidos)).setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.hdMedidos)).setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.hdPromedios)).setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.hdFijos)).setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.hdTotal)).setVisibility(View.VISIBLE);

            }
            /*
                holder = new ViewHolder();
                holder.isHint=false;
                holder.mRowHolder = new ViewHolder.RowHolder();
                holder.mRowHolder.lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
                holder.mRowHolder.lblRegistros = convertView.findViewById(R.id.lblRegistros);
                holder.mRowHolder.txtEnviados = convertView.findViewById(R.id.txtRegSend);
                holder.mRowHolder.txtCapturados  = convertView.findViewById(R.id.txtRegCapt);
                holder.mRowHolder.txtFijos = convertView.findViewById(R.id.txtFijos);
                holder.mRowHolder.txtMedidos = convertView.findViewById(R.id.txtMedidos);
                holder.mRowHolder.txtPromedios = convertView.findViewById(R.id.txtPromedios);
                holder.mRowHolder.txtObservaciones = convertView.findViewById(R.id.txtObservaciones);
                convertView.setTag(R.string.item_tag,holder);
            }else{
                holder = (ViewHolder) convertView.getTag(R.string.item_tag);
            }
            holder.position=position;
            holder.value=rutaItem.getIdRuta();
            */
        }
        /*
        if (holder.isHint){
            holder.mHintHolder.lblDescripcion.setText(rutaItem.getDescripcion());
            holder.mHintHolder.lblRegistros.setText("Seleccione una Ruta");
            holder.mHintHolder.lblDescripcion.setTextColor(Color.GRAY);
        }else{
            holder.mRowHolder.lblDescripcion.setText(rutaItem.getDescripcion());
            holder.mRowHolder.lblRegistros.setText(rutaItem.getRegistros().toString());
            holder.mRowHolder.txtEnviados.setText(rutaItem.getEnviadas().toString());
            holder.mRowHolder.txtCapturados.setText(rutaItem.getCapturadas().toString());
            holder.mRowHolder.txtFijos.setText(rutaItem.getFijos().toString());
            holder.mRowHolder.txtMedidos.setText(rutaItem.getMedidos().toString());
            holder.mRowHolder.txtPromedios.setText(rutaItem.getPromedios().toString());
            holder.mRowHolder.txtObservaciones.setText(rutaItem.getObservaciones());
        }
        */




        return convertView;
    }

    public void setData(List<Ruta> rutas) {
        data = rutas;
        notifyDataSetChanged();
    }
    public void updateSelected(Ruta ruta) {

        View convertView =null;
        TextView txtDescripcion = (TextView) convertView.findViewById(R.id.lblDescripcion);
        TextView txtObservaciones = (TextView) convertView.findViewById(R.id.txtObservaciones);
        TextView txtRegistros = (TextView) convertView.findViewById(R.id.lblRegistros);
        TextView txtMedidos = (TextView) convertView.findViewById(R.id.txtMedidos);
        TextView txtPromedios = (TextView) convertView.findViewById(R.id.txtPromedios);
        TextView txtFijos = (TextView) convertView.findViewById(R.id.txtFijos);
        TextView txtCapturados = (TextView) convertView.findViewById(R.id.txtRegCapt);
        TextView txtEnviados = (TextView) convertView.findViewById(R.id.txtRegSend);

        txtDescripcion.setText(ruta.getDescripcion());
        txtObservaciones.setText(ruta.getObservaciones());
        txtRegistros.setText(ruta.getRegistros().toString());
        txtMedidos.setText(ruta.getMedidos().toString());
        txtPromedios.setText(ruta.getPromedios().toString());
        txtFijos.setText(ruta.getFijos().toString());
        txtCapturados.setText(ruta.getCapturadas().toString());
        txtEnviados.setText(ruta.getEnviadas().toString());
        txtDescripcion.setTextColor(Color.GREEN);
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
        return data.get(position).getIdRuta();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public int getItemViewType(int position) {
        return  position==0 ? 0 : 1;  //IGNORE_ITEM_VIEW_TYPE; //
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



/*
    private static final class ViewHolder {

        protected static boolean isHint;
        protected static HintHolder mHintHolder;
        protected static RowHolder mRowHolder;
        protected static Integer position;
        protected static Integer value;

        public static final class HintHolder {

            protected static TextView lblDescripcion;
            protected static TextView lblRegistros;

        }
        public static final class RowHolder {

            protected static TextView lblDescripcion;
            protected static TextView lblRegistros;
            protected static TextView txtObservaciones;
            protected static TextView txtMedidos;
            protected static TextView txtPromedios;
            protected static TextView txtFijos;
            protected static TextView txtEnviados;
            protected static TextView txtCapturados;

        }
    }
*/
}
