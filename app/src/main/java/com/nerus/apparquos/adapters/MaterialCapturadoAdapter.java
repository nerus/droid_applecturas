package com.nerus.apparquos.adapters;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Material;
import com.nerus.apparquos.entities.MaterialCapturado;
import com.nerus.apparquos.helpers.clsUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MaterialCapturadoAdapter extends RecyclerView.Adapter<MaterialCapturadoAdapter.ItemViewHolder> implements Filterable {
    private List<MaterialCapturado> mCurrentList;
    private LayoutInflater inflater;
    private OnItemListener mOnItemListener;
    private List<MaterialCapturado> dataBak;
    private boolean isLoading = true;

    public MaterialCapturadoAdapter(List<MaterialCapturado> list, LayoutInflater inflater, OnItemListener onItemListener) {
        this.mCurrentList = list;
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
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listmatcapturado_row,parent,false);
        return new ItemViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        MaterialCapturado item = mCurrentList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mCurrentList==null ? 0: mCurrentList.size();
    }

    @Override
    public Filter getFilter() {
        ListFilter filter = new ListFilter(this, dataBak);
        return filter;
    }
    public void setData(List<MaterialCapturado> list) {
        mCurrentList = list;
        dataBak = list;
        notifyDataSetChanged();
    }
    public void setDataFiltered(List<MaterialCapturado> list ) {
        mCurrentList = list;
        notifyDataSetChanged();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemListener mOnItemListener;
        private final ConstraintLayout back;
        private final TextView mDescripcion;
        //private final TextView mObservacion;
        private final TextView mCantidad;
        private final TextView mUdm;
        //private final TextView mIdMaterial;

        public ItemViewHolder(View view, OnItemListener onItemListener) {
            super(view);
            isLoading=true;
            this.mOnItemListener = onItemListener;
            back = (ConstraintLayout) view.findViewById(R.id.row);
            mDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);

            //mObservacion = (TextView) view.findViewById(R.id.txtObserva);
            mCantidad = (TextView) view.findViewById(R.id.txtCantidad);
            //mIdMaterial = (TextView) view.findViewById(R.id.txtMedidor);
            mUdm = (TextView) view.findViewById(R.id.txtUdm);

            itemView.setOnClickListener(this);
            /*
            mCantidad.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    clsUtilities.LogSNE.d("NERUS","mCantidad.addTextChangedListener ");
                    if (!isLoading){
                        Integer value = Integer.valueOf(mCantidad.getText().toString());
                        //updateCantidad(value);
                    }
                }
            });

            mCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    int targetId = v.getId();
                    clsUtilities.LogSNE.d("NERUS","mCantidad.setOnFocusChangeListener "+ hasFocus);
                    if (targetId == R.id.txtCantidad &&  !hasFocus){
                        clsUtilities.LogSNE.d("NERUS","mCantidad.setOnFocusChangeListener ");
                        Integer value = Integer.valueOf(mCantidad.getText().toString());
                        updateCantidad(value);
                    }
                }
            });*/
            ImageView cmdAdd = (ImageView) view.findViewById(R.id.ic_plus);
            cmdAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer value = Integer.valueOf(mCantidad.getText().toString())+1;
                    if (value>99999) value=1;
                    updateCantidad(value);
                }
            });
            ImageView cmdMinus = (ImageView) view.findViewById(R.id.ic_minus);
            cmdMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer value = Integer.valueOf(mCantidad.getText().toString())-1;
                    if (value<=0) value=1;
                    updateCantidad(value);
                }
            });

            ImageView cmdRemove = (ImageView) view.findViewById(R.id.ic_remove);
            cmdRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    mOnItemListener.onItemRemoved(mCurrentList.get(pos));
                    mCurrentList.remove(pos);
                    setData(mCurrentList);
                }
            });
            isLoading=false;
        }
        private void updateCantidad(Integer value){
            int pos = getAdapterPosition();
            if (pos>=0){
                mCantidad.setText(value.toString());
                mCurrentList.get(pos).setCantidad(value.toString());
                mOnItemListener.onItemValueChanged(mCurrentList.get(pos));
                setData(mCurrentList);
            }
        }
        public void bind(MaterialCapturado item) {
            if (item!=null){
                mDescripcion.setText(item.getIdMaterial() +".-"+ item.getDescripcion());
                //mObservacion.setText(item.getObservacion()+"CAPTURADO:"+ item.getFechaCaptura());
                mCantidad.setText(item.getCantidad());
                mUdm.setText(item.getUdm());
                //mIdMaterial.setText(item.getIdMaterial());
            }
        }

        @Override
        public void onClick(View v) {
            //LogSNE.d("NERUS","TrabajoViewHolder.onClick " + String.valueOf(getAdapterPosition()));
            int pos = getAdapterPosition();
            mOnItemListener.onItemClick(pos, mCurrentList.get(pos));
        }
    }

    private class ListFilter extends Filter {
        private final MaterialCapturadoAdapter mAdapter;
        private final List<MaterialCapturado> mList;


        private ListFilter(MaterialCapturadoAdapter adapter, List<MaterialCapturado> list) {
            this.mAdapter = adapter;
            this.mList = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            FilterResults filterResults = new FilterResults();
            if (charString.isEmpty()) {
                filterResults.values = mList;
            } else {
                List<MaterialCapturado> filteredList = new ArrayList<>();

                for (Integer i=0; i<mList.size();i++) {
                    MaterialCapturado  rowItem = mList.get(i);
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
            List<MaterialCapturado> filteredList   = (ArrayList<MaterialCapturado>) results.values;
            mAdapter.setDataFiltered(filteredList);
        }
    }
}