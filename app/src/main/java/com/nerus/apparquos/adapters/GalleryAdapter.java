package com.nerus.apparquos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Archivo;
import com.nerus.apparquos.helpers.GlideApp;


import java.time.LocalDate;
import java.util.List;

public final class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemListener mOnItemListener;

    List<Archivo> data ;
    final private  Context mContext;

    public GalleryAdapter(Context context, List<Archivo> data, OnItemListener listener) {
        this.mContext = context;
        this.data = data;
        this.mOnItemListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.photo_list_content, parent, false);
        viewHolder = new ViewHolder(v, mOnItemListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        /*
        GlideApp.with(fragment)
                .load(myUrl)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .into(imageView);*/

        GlideApp.with(mContext)
                .load(data.get(position).getFilePath())
                .thumbnail(0.5f)
                //.override(200,200)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((ViewHolder) holder).mImg);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setData(List<Archivo> archivos) {
        data = archivos;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private final OnItemListener mListener;
        ImageView mImg;


        public ViewHolder(View itemView , OnItemListener onItemListener) {
            super(itemView);
            mListener = onItemListener;
            mImg = (ImageView) itemView.findViewById(R.id.imgItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mListener.onItemClick(pos, null);

        }
    }

}