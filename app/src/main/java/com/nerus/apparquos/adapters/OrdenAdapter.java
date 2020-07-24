package com.nerus.apparquos.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Orden;

import java.util.ArrayList;
import java.util.List;

public class OrdenAdapter extends RecyclerView.Adapter<OrdenAdapter.OrdenViewHolder> implements Filterable {
    private List<Orden> mOrdenList;
    private LayoutInflater inflater;
    private OnItemListener mOnItemListener;
    private List<Orden> dataBak;

    public OrdenAdapter(List<Orden> list, LayoutInflater inflater, OnItemListener onItemListener) {
        this.mOrdenList = list;
        this.dataBak = list;
        this.inflater = inflater;
        this.mOnItemListener = onItemListener;
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @NonNull
    @Override
    public OrdenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listordenes_row,parent,false);
        return new OrdenViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdenViewHolder holder, int position) {
        Orden orden = mOrdenList.get(position);
        holder.bind(orden);
    }

    @Override
    public int getItemCount() {
        return mOrdenList==null ? 0: mOrdenList.size();
    }

    @Override
    public Filter getFilter() {
        OrdenFilter filter = new OrdenFilter(this, dataBak);
        return filter;
    }
    public void setData(List<Orden> ordenes) {
        mOrdenList = ordenes;
        dataBak = ordenes;
        notifyDataSetChanged();
    }
    public void setDataFiltered(List<Orden> ordenes) {
        mOrdenList = ordenes;
        notifyDataSetChanged();
    }
    public class OrdenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemListener mOnItemListener;
        private final ConstraintLayout back;
        private final TextView mDireccion;
        private final TextView mMedidor;
        private final TextView mLocalizacion;
        private final TextView mCuenta;
        private final TextView mSituacion;
        private final TextView mRazonSocial;
        private final TextView mIdOrden;
        private final TextView mTrabajo;

        public OrdenViewHolder(View view, OnItemListener onItemListener) {
            super(view);
            this.mOnItemListener = onItemListener;
            back = (ConstraintLayout) view.findViewById(R.id.row);
            mDireccion = (TextView) view.findViewById(R.id.txtDireccion);
            mMedidor = (TextView) view.findViewById(R.id.txtMedidor);
            mLocalizacion = (TextView) view.findViewById(R.id.txtLocalizacion);
            mCuenta = (TextView) view.findViewById(R.id.txtCuenta);
            mSituacion = (TextView) view.findViewById(R.id.txtSituacion);
            mRazonSocial = (TextView) view.findViewById(R.id.txtRazonSocial);
            mIdOrden = (TextView) view.findViewById(R.id.txtIdOrden);
            mTrabajo = (TextView) view.findViewById(R.id.txtTrabajo);

            itemView.setOnClickListener(this);
        }

        public void bind(Orden orden) {
            if (orden!=null){
                mDireccion.setText(orden.getDireccion() + ", " + orden.getColonia() + ", " + orden.getPoblacion());
                mRazonSocial.setText(orden.getNombre());
                mMedidor.setText(orden.getIdMedidor());
                mLocalizacion.setText(orden.getLocalizacion());  //.length() > 10 ? orden.getLocalizacion().substring(11) : orden.getLocalizacion());
                mCuenta.setText(orden.getIdCuenta().toString());
                mSituacion.setText(orden.getEstatus());
                mIdOrden.setText(orden.getIdOrden());
                mTrabajo.setText(orden.getTrabajo());
                if (orden.getCapturado()){
                    back.setBackgroundColor(Color.rgb(225,245,254));
                }else{
                    back.setBackgroundColor(Color.rgb(255,255,255));
                }

            }
        }

        @Override
        public void onClick(View v) {
            //LogSNE.d("NERUS","TrabajoViewHolder.onClick " + String.valueOf(getAdapterPosition()));
            int pos = getAdapterPosition();
            mOnItemListener.onItemClick(pos, mOrdenList.get(pos));
        }
    }

    private class OrdenFilter extends Filter {
        private final OrdenAdapter mAdapter;
        private final List<Orden> mList;


        private OrdenFilter(OrdenAdapter adapter, List<Orden> ordenList) {
            this.mAdapter = adapter;
            this.mList = ordenList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            FilterResults filterResults = new FilterResults();
            if (charString.isEmpty()) {
                filterResults.values = mList;
            } else {
                List<Orden> filteredList = new ArrayList<>();

                for (Integer i=0; i<mList.size();i++) {
                    Orden  rowItem = mList.get(i);
                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or medidor number match
                    if ((rowItem.getNombre() + rowItem.getDireccion()+rowItem.getPoblacion()+rowItem.getColonia() +rowItem.getIdOrden()+rowItem.getIdMedidor()+rowItem.getIdCuenta() ).toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(rowItem);
                    }
                }
                if (filteredList.size()>0) {
                    filterResults.values = filteredList;
                }else{
                    filterResults.values = mList;
                }
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Orden> filteredList   = (ArrayList<Orden>) results.values;
            mAdapter.setDataFiltered(filteredList);
        }
    }
}