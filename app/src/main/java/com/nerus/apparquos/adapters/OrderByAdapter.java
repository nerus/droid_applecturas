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
import com.nerus.apparquos.entities.OrderBy;

import java.util.List;

public class OrderByAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<OrderBy> data;
    private LayoutInflater inflater;

    public OrderByAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        this.context = context;
        this.data = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public OrderByAdapter(Context context, List<OrderBy> list) {
        this.context = context;
        this.data = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        //ViewHolder holder;
        OrderBy item = (OrderBy) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
        }
        TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
        TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);

        if (position == 0) {
            lblDescripcion.setText(item.getDescripcion());
            lblRegistros.setText("Seleccione uno...");
            lblDescripcion.setTextColor(Color.GRAY);

        } else {
            lblDescripcion.setText(item.getIdOrder().toString() + ".-" + item.getDescripcion());
            lblRegistros.setText("");
            lblRegistros.setVisibility(View.GONE);
            lblDescripcion.setTextColor(Color.BLACK);
        }


        return convertView;
    }

    public void setData(List<OrderBy> list) {
        data = list;
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
        return data.get(position).getIdOrder();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderBy item = (OrderBy) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
        }
        TextView lblDescripcion = convertView.findViewById(R.id.lblDescripcion);
        TextView lblRegistros = convertView.findViewById(R.id.lblRegistros);

        if (position == 0) {
            lblDescripcion.setText(item.getDescripcion());
            lblRegistros.setText("Seleccione uno...");
            lblDescripcion.setTextColor(Color.GRAY);

        } else {
            lblDescripcion.setText(item.getIdOrder().toString() + ".-" + item.getDescripcion());
            lblRegistros.setText("");
            lblRegistros.setVisibility(View.GONE);
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
