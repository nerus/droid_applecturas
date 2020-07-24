package com.nerus.apparquos.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Ruta;

import java.util.ArrayList;
import java.util.List;

public final class CuentaAdapter extends RecyclerView.Adapter<CuentaAdapter.CuentaViewHolder> implements Filterable {
    private List<Cuenta> mCuentaList;
    private LayoutInflater inflater;
    private OnItemListener mOnItemListener;
    private List<Cuenta> dataBak;

    public CuentaAdapter(List<Cuenta> cuentaList, LayoutInflater inflater, OnItemListener onItemListener) {
        this.mCuentaList = cuentaList;
        this.dataBak = cuentaList;
        this.inflater = inflater;
        this.mOnItemListener = onItemListener;
    }
    @Override
    public int getItemViewType(int position) {
        final Cuenta item = mCuentaList.get(position);
        return item.getCapturado() ? 1 : 0;
    }
    @NonNull
    @Override
    public CuentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CuentaViewHolder(inflater, parent, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentaViewHolder holder, int position) {
        Cuenta cuenta = mCuentaList.get(position);
        holder.bind(cuenta);
    }

    @Override
    public int getItemCount() {
        return mCuentaList==null ? 0: mCuentaList.size();
    }

    @Override
    public Filter getFilter() {
        CuentaFilter filter = new CuentaFilter(this, dataBak);
        return filter;
    }
    public void setData(List<Cuenta> cuentas) {
        mCuentaList = cuentas;
        dataBak = cuentas;
        notifyDataSetChanged();
    }
    public void setDataFiltered(List<Cuenta> cuentas) {
        mCuentaList = cuentas;
        notifyDataSetChanged();
    }

    public class CuentaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemListener mOnItemListener;
        private final TextView mDireccion;
        private final TextView mMedidor;
        private final TextView mLocalizacion;
        private final TextView mCuenta;
        private final TextView mSituacion;
        private final Button mCmdDetail;
        private final TextView mRazonSocial;

        public CuentaViewHolder(LayoutInflater inflater, ViewGroup parent, OnItemListener onItemListener) {
            super(inflater.inflate(R.layout.listcuentas_row,parent,false));
            this.mOnItemListener = onItemListener;

            mDireccion = (TextView) itemView.findViewById(R.id.txtDireccion);
            mMedidor = (TextView)   itemView.findViewById(R.id.txtMedidor);
            mLocalizacion = (TextView) itemView.findViewById(R.id.txtLocalizacion);
            mCuenta = (TextView) itemView.findViewById(R.id.txtCuenta);
            mSituacion = (TextView) itemView.findViewById(R.id.txtSituacion);
            mRazonSocial = (TextView) itemView.findViewById(R.id.txtRazonSocial);
            mCmdDetail = (Button) itemView.findViewById(R.id.cmdDetail);
            //itemView.setOnClickListener(this);
            mCmdDetail.setOnClickListener(this);

        }

        public void bind(Cuenta cuenta) {
            if (cuenta!=null){
                mDireccion.setText(cuenta.getDireccion());
                mMedidor.setText(cuenta.getMedidor());
                mLocalizacion.setText(cuenta.getLocalizacion().length() > 5 ? cuenta.getLocalizacion().substring(6) : cuenta.getLocalizacion());
                mCuenta.setText(cuenta.getId_Cuenta().toString());
                mSituacion.setText(cuenta.getSituacion());
                mRazonSocial.setText(cuenta.getRazon_social());
                mLocalizacion.setTextColor(Color.BLACK);
                mMedidor.setTextColor(Color.BLACK);
                if (cuenta.getCapturado()){
                    mLocalizacion.setTextColor(Color.BLUE);
                    mMedidor.setTextColor(Color.BLUE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            //LogSNE.d("NERUS","CuentaViewHolder.onClick " + String.valueOf(getAdapterPosition()));
            int pos = getAdapterPosition();
            mOnItemListener.onItemClick(pos, mCuentaList.get(pos));
        }
    }

    private class CuentaFilter extends Filter {
        private final CuentaAdapter mAdapter;
        private final List<Cuenta> mList;


        private CuentaFilter(CuentaAdapter adapter, List<Cuenta> cuentaList) {
            this.mAdapter = adapter;
            this.mList = cuentaList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            FilterResults filterResults = new FilterResults();
            if (charString.isEmpty()) {
                filterResults.values = mList;
            } else {
                List<Cuenta> filteredList = new ArrayList<>();

                for (Integer i=0; i<mList.size();i++) {
                    Cuenta  rowItem=mList.get(i);
                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or medidor number match
                    if ((rowItem.getMedidor() + rowItem.getDireccion() + rowItem.getRazon_social() + rowItem.getId_Cuenta().toString() ).toLowerCase().contains(charString.toLowerCase())) {
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
            List<Cuenta> filteredList   = (ArrayList<Cuenta>) results.values;
            mAdapter.setDataFiltered(filteredList);
        }
    }
}
