package com.nerus.apparquos.adapters;

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
import com.nerus.apparquos.entities.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ItemViewHolder> implements Filterable {
    private List<Material> mCurrentList;
    private LayoutInflater inflater;
    private OnItemListener mOnItemListener;
    private List<Material> dataBak;

    public MaterialAdapter(List<Material> list, LayoutInflater inflater, OnItemListener onItemListener) {
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
        View view = inflater.inflate(R.layout.listmaterial_row,parent,false);
        return new ItemViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Material item = mCurrentList.get(position);
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
    public void setData(List<Material> list) {
        mCurrentList = list;
        dataBak = list;
        notifyDataSetChanged();
    }
    public void setDataFiltered(List<Material> list ) {
        mCurrentList = list;
        notifyDataSetChanged();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemListener mOnItemListener;
        private final ConstraintLayout back;
        private final TextView mDescripcion;
        private final TextView mUdm;

        public ItemViewHolder(View view, OnItemListener onItemListener) {
            super(view);
            this.mOnItemListener = onItemListener;
            back = (ConstraintLayout) view.findViewById(R.id.row);
            mDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
            mUdm = (TextView) view.findViewById(R.id.txtUdm);

            itemView.setOnClickListener(this);
        }

        public void bind(Material item) {
            if (item!=null){
                mDescripcion.setText(item.getIdMaterial() +".-"+ item.getDescripcion());
                mUdm.setText(item.getUdm());
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
        private final MaterialAdapter mAdapter;
        private final List<Material> mList;


        private ListFilter(MaterialAdapter adapter, List<Material> list) {
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
                List<Material> filteredList = new ArrayList<>();

                for (Integer i=0; i<mList.size();i++) {
                    Material  rowItem = mList.get(i);
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
            List<Material> filteredList   = (ArrayList<Material>) results.values;
            mAdapter.setDataFiltered(filteredList);
        }
    }
}