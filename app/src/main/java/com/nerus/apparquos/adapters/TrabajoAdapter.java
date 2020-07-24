package com.nerus.apparquos.adapters;

import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Trabajo;
import com.nerus.apparquos.fragments.ListResumenOTFragment;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

import java.util.ArrayList;
import java.util.List;

public class TrabajoAdapter extends RecyclerView.Adapter<TrabajoAdapter.TrabajoViewHolder> implements Filterable {
    private List<Trabajo> mTrabajoList;
    private LayoutInflater inflater;
    private OnItemListener mOnItemListener;
    private List<Trabajo> dataBak;

    public TrabajoAdapter(List<Trabajo> list, LayoutInflater inflater, OnItemListener onItemListener) {
        this.mTrabajoList = list;
        this.dataBak = list;
        this.inflater = inflater;
        this.mOnItemListener = onItemListener;
    }
    @Override
    public int getItemViewType(int position) {
        Trabajo item = mTrabajoList.get(position);
        int nType = 0;
        if (item.getIdTipo().equals(1) && item.getIdTrabajo()<0){
            nType = 1;
        }else if (item.getIdTipo().equals(2) && item.getIdTrabajo()<0){
            nType = 2;
        }
        return nType;
    }
    @NonNull
    @Override
    public TrabajoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==0){
            view = inflater.inflate(R.layout.listresumen_row,parent,false);
        }else{
            view = inflater.inflate(R.layout.listresumen_headsection,parent,false);
        }

        return new TrabajoViewHolder(view, viewType, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrabajoViewHolder holder, int position) {
        Trabajo trabajo = mTrabajoList.get(position);
        holder.bind(trabajo);
    }

    @Override
    public int getItemCount() {
        return mTrabajoList==null ? 0: mTrabajoList.size();
    }

    @Override
    public Filter getFilter() {
        TrabajoFilter filter = new TrabajoFilter(this, dataBak);
        return filter;
    }
    public void setData(List<Trabajo> trabajos) {
        mTrabajoList = trabajos;
        dataBak = trabajos;
        notifyDataSetChanged();
    }

    public class TrabajoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemListener mOnItemListener;
        private final TextView mDescripcion;
        private final TextView mRegistros;
        //private final TextView mId;
        private final TextView mCapturadas;
        private final TextView mEnviadas;
        //private final ConstraintLayout mCTBackground;
        public TrabajoViewHolder(View view, int viewType, OnItemListener onItemListener){
            super(view);
            this.mOnItemListener = onItemListener;

            mDescripcion = (TextView) itemView.findViewById(R.id.lblDescripcion);
            mRegistros = (TextView)   itemView.findViewById(R.id.lblRegistros);
            mCapturadas = (TextView)   itemView.findViewById(R.id.txtRegCapt);
            mEnviadas = (TextView)   itemView.findViewById(R.id.txtRegSend);
            //mId = (TextView)   itemView.findViewById(R.id.txtId);
            //mCTBackground =   itemView.findViewById(R.id.backgroud);

            itemView.setOnClickListener(this);

        }
        /*
        public TrabajoViewHolder(LayoutInflater inflater, ViewGroup parent, OnItemListener onItemListener) {
            super(inflater.inflate(R.layout.listresumen_row,parent,false));
            this.mOnItemListener = onItemListener;

            mDescripcion = (TextView) itemView.findViewById(R.id.lblDescripcion);
            mRegistros = (TextView)   itemView.findViewById(R.id.lblRegistros);
            mId = (TextView)   itemView.findViewById(R.id.txtId);
            mCTBackground =   itemView.findViewById(R.id.backgroud);

            itemView.setOnClickListener(this);
        }
        */

        public void bind(Trabajo trabajo) {
            if (trabajo!=null){
                LogSNE.d("NERUS",trabajo.toString());
                mDescripcion.setText(trabajo.getDescripcion());
                mRegistros.setText(trabajo.getRegistros().toString() + " Registros");
                if (trabajo.getCapturadas() != null)
                mCapturadas.setText(trabajo.getCapturadas().toString());
                if (trabajo.getEnviadas() != null)
                mEnviadas.setText(trabajo.getEnviadas().toString());
//                if (trabajo.getIdTrabajo()>0) {
//                    mCapturadas.setText(trabajo.getCapturadas().toString());
//                    mEnviadas.setText(trabajo.getEnviadas().toString());
//                }
                //mCTBackground.setBackgroundColor(Color.WHITE);
                if (trabajo.getIdTrabajo()<0 || trabajo.getIdTipo()==2){
                    //mId.setText("");
                }
                if (trabajo.getIdTrabajo()<0){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        mDescripcion.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                    mDescripcion.setGravity(Gravity.CENTER_HORIZONTAL);
                    //mCTBackground.setBackgroundColor(Color.GRAY);
                }else{
                    //mId.setText("ID:"+trabajo.getIdTrabajo().toString());
                }


            }
        }

        @Override
        public void onClick(View v) {
            //LogSNE.d("NERUS","TrabajoViewHolder.onClick " + String.valueOf(getAdapterPosition()));
            int pos = getAdapterPosition();
            mOnItemListener.onItemClick(pos, mTrabajoList.get(pos));
        }
    }

    private class TrabajoFilter extends Filter {
        private final TrabajoAdapter mAdapter;
        private final List<Trabajo> mList;


        private TrabajoFilter(TrabajoAdapter adapter, List<Trabajo> trabajoList) {
            this.mAdapter = adapter;
            this.mList = trabajoList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            FilterResults filterResults = new FilterResults();
            if (charString.isEmpty()) {
                filterResults.values = mList;
            } else {
                List<Trabajo> filteredList = new ArrayList<>();

                for (Integer i=0; i<mList.size();i++) {
                    Trabajo  rowItem = mList.get(i);
                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or medidor number match
                    if ((rowItem.getDescripcion() ).toLowerCase().contains(charString.toLowerCase())) {
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
            List<Trabajo> filteredList   = (ArrayList<Trabajo>) results.values;
            mAdapter.setData(filteredList);
        }
    }
}