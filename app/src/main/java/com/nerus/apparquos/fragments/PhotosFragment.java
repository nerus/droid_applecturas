package com.nerus.apparquos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.GalleryAdapter;
import com.nerus.apparquos.adapters.OnItemListener;
import com.nerus.apparquos.entities.Archivo;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;

import java.io.File;
import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public final class PhotosFragment extends Fragment implements OnItemListener {
    private Cuenta mCuenta;
    private Orden mOrden;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    List<Archivo> mArchivoList;
    private String mFolder = "";
    private String mSubFolder = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mCuenta = (Cuenta) args.getSerializable("CUENTA");
        mOrden = (Orden) args.getSerializable("ORDEN");

        if (mCuenta!=null){
            clsUtilities.setHomeBack(getActivity(),mCuenta.getDireccion());
            mFolder = "PADRON";
            mSubFolder = String.format("%03d", mCuenta.getId_Poblacion()) +String.format("%07d",  mCuenta.getId_Cuenta())+"_"+mCuenta.getId_Padron().toString();
        }
        if (mOrden!=null){
            clsUtilities.setHomeBack(getActivity(),mOrden.getDireccion());
            mFolder = "ORDENES";
            mSubFolder = mOrden.getIdOrden();

        }
        LogSNE.d("NERUS","onCreate mFolder:" +  mFolder +" mSubFolder:"+mSubFolder) ;
    }
    @Override
    public void onStart() {
        super.onStart();
        LogSNE.d("NERUS","onStart mFolder:" +  mFolder +" mSubFolder:"+mSubFolder) ;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.photos_fragment,container,false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.photolist);
        assert mRecyclerView != null;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setHasFixedSize(true);
        setupRecyclerView(mRecyclerView);



        FloatingActionButton cmdTakePhoto = v.findViewById(R.id.fabTakePhoto);
        cmdTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Enfoque correctamente...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                cmdTakePhoto_OnClick(view);
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        //LogSNE.d("NERUS","GetRutaLecturarFragment.onActivityResult :" +  String.valueOf(resultCode)) ;
        if (requestCode == clsRequestCodes.REQUEST_CAMERA){
            setupRecyclerView(mRecyclerView);
        }
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        LogSNE.d("NERUS","setupRecyclerView :mFolder:" +  mFolder +" mSubFolder "+mSubFolder) ;

        mArchivoList = clsUtilities.getFileList(mFolder, mSubFolder);
        mAdapter = new GalleryAdapter(getActivity(), mArchivoList, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void cmdTakePhoto_OnClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File cFile = clsUtilities.getOutputMediaFile(mFolder, mSubFolder, mSubFolder+"_");

        Uri file = clsUtilities.getUriFromFile(getContext(), cFile);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        LogSNE.d("NERUS", "file->" + file.toString());
        startActivityForResult(intent, clsRequestCodes.REQUEST_CAMERA);
    }

    @Override
    public void onItemClick(Integer position, Object item) {
        LogSNE.d("NERUS", "onItemClick->" + mArchivoList.get(position).toString());
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("ARCHIVO",mArchivoList.get(position));

            if (mCuenta!=null) bundle.putSerializable("CUENTA",mCuenta);
            if (mOrden!=null) bundle.putSerializable("ORDEN",mOrden);

            fragment = new PhotoDetailFragment();
            //fragment.setTargetFragment(this, clsRequestCodes.REQUEST_PHOTOS);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("PHOTO_DETAIL")
                    .commit();
        }

    }

    @Override
    public void onItemValueChanged(Object itemChanged) {

    }

    @Override
    public void onItemRemoved(Object itemToRemove) {

    }
}
