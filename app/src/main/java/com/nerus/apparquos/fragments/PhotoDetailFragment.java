package com.nerus.apparquos.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.GalleryAdapter;
import com.nerus.apparquos.entities.Archivo;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.helpers.clsUtilities;

//-----------------------------------------------------
//This use that library to Zoom in Zoom Out the Image
//https://github.com/chrisbanes/PhotoView
//-----------------------------------------------------
public final class PhotoDetailFragment extends Fragment {
    private Archivo mArchivo;
    private Orden mOrden;
    private Cuenta mCuenta;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mArchivo = (Archivo) args.getSerializable("ARCHIVO");
        mCuenta = (Cuenta) args.getSerializable("CUENTA");
        mOrden = (Orden) args.getSerializable("ORDEN");

    }
    @Override
    public void onStart() {
        super.onStart();
        if(mCuenta!=null) clsUtilities.setHomeBack(getActivity(),mCuenta.getDireccion());
        if(mOrden!=null) clsUtilities.setHomeBack(getActivity(),mOrden.getDireccion());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.photo_detail_fragment,container,false);
        ImageView imageView = v.findViewById(R.id.imgItem);

        Glide.with(getActivity()).load(mArchivo.getFilePath())
                .thumbnail(0.5f)
                //.override(200,200)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        return v;
    }
}
