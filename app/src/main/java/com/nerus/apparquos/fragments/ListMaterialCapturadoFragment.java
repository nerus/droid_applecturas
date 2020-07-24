package com.nerus.apparquos.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.OnItemListener;
import com.nerus.apparquos.adapters.MaterialCapturadoAdapter;
import com.nerus.apparquos.entities.Material;
import com.nerus.apparquos.entities.MaterialCapturado;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.MaterialViewModel;


import java.util.List;

public final class ListMaterialCapturadoFragment extends Fragment implements OnItemListener, TasksCallBacks {
    private ViewModelProvider mViewModelProvider;
    private MaterialViewModel mMaterialViewModel;
    //private ParametroViewModel mParametroViewModel;

    private List<MaterialCapturado> mCurrentList;
    private Orden mOrden;
    private MaterialCapturado currentItem;
    private Integer currentPosition=0;
    private MaterialCapturadoAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private LayoutInflater mLayoutInflater;
    private Boolean mLoaded=false;


    private AppArquos mAppArquos;
    private FloatingActionButton fabAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppArquos = (AppArquos) getActivity().getApplicationContext();

        mLoaded=false;
        Bundle args = getArguments();
        mOrden = (Orden) args.get("ORDEN");

        mViewModelProvider = ViewModelProviders.of(this);
        mMaterialViewModel = mViewModelProvider.get(MaterialViewModel.class);
        //mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);

        setHasOptionsMenu(true);


    }

    @Override
    public void onStart() {
        super.onStart();

        clsUtilities.setHomeBack(getActivity(),"MATERIALES");

        setObservable();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayoutInflater = inflater;
        final View v = mLayoutInflater.inflate(R.layout.listmatcapturado_fragment, container, false);


        mRecyclerView = (RecyclerView) v.findViewById(R.id.listMatCapturado);
        mLinearLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        return v;
    }

    private void clickFabAdd(View view) {
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            if (currentItem==null){
                currentItem = new MaterialCapturado(mOrden.getIdOrden(),"0","PIEZA","-- SIN ESPECIFICAR --","0","","");
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("ORDEN",mOrden);
            bundle.putSerializable("MATERIAL",currentItem);

            fragment = new CapturaMaterialFragment();
            //fragment.setTargetFragment(this, clsRequestCodes.REQUEST_PHOTOS);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("CAPTURA_MATERIAL")
                    .commit();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mOrdenViewModel.saveLastTrabajo(mTrabajoList.get(currentPosition));
    }

    @Override
    public void onItemClick(Integer position, Object item) {
LogSNE.d("NERUS","ListMaterialCapturadoFragment.onItemClick " + item.toString());
        currentItem = (MaterialCapturado) item;
/*
        this.currentOrden = (Orden) item; // mOrdenList.get(position);
        this.currentPosition=position;
        if (!this.currentOrden.getIdOrden().equals(mOrdenList.get(position).getIdOrden())){
            //viene de un filtrado buscar la posicion correcta
            this.currentPosition = mOrdenList.indexOf(this.currentOrden);
        }

        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("ORDEN",currentOrden);

            fragment = new CapturaOrdenFragment();
            fragment.setTargetFragment(this, clsRequestCodes.REQUEST_CAPTURAORDEN);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("CAPTURA_ORDEN")
                    .commit();
        }
*/
    }

    @Override
    public void onItemValueChanged(Object itemChanged) {
        clsUtilities.hideKeyboard( getActivity());
        currentItem = (MaterialCapturado) itemChanged;
        mMaterialViewModel.insert(currentItem);
    }

    @Override
    public void onItemRemoved(Object itemToRemove) {
        currentItem = (MaterialCapturado) itemToRemove;
        mMaterialViewModel.delete(currentItem);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.capturamaterial_menu, menu);
// Associate searchable configuration with the SearchView
        /*
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                update_list(mCurrentList);
                return false;
            }
        });
        */

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            LogSNE.d("NERUS","save orden" + item.toString());
            cmdAdd_onClick(item.getActionView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cmdAdd_onClick(View v) {
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
//            if (currentItem==null){
//                currentItem = new MaterialCapturado(mOrden.getIdOrden(),"0","PIEZA","-- SIN ESPECIFICAR --","0","","");
//            }
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("ORDEN",mOrden);
//            bundle.putSerializable("MATERIAL",currentItem);

            fragment = new ListMaterialFragment();
            fragment.setTargetFragment(this, clsRequestCodes.REQUEST_MATERIAL);
            //fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("BUSCAR_MATERIAL")
                    .commit();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        LogSNE.d("NERUS","ListMaterialCapturadoFragment.onActivityResult :" +  String.valueOf(resultCode)) ;
        if (requestCode == clsRequestCodes.REQUEST_MATERIAL){
            Material material = (Material) data.getSerializableExtra("MATERIAL");
            //if (currentItem==null){
            MaterialCapturado item = new MaterialCapturado(mOrden.getIdOrden(),material.getIdMaterial(),material.getUdm(),material.getDescripcion(),"1", clsFecha.getCurrentDate(),"");
            //} else {
                //currentItem.setCantidad("1");
                //currentItem.setObservacion("");
            //}
            mMaterialViewModel.insert(item);
        }
    }
    private void update_list(List<MaterialCapturado> list) {
        mCurrentList = list;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(list.size()) + " MATERIALES - " + mOrden.getIdOrden()+".-"+mOrden.getTrabajo());

        mAdapter = new MaterialCapturadoAdapter(mCurrentList, mLayoutInflater, this);
        mRecyclerView.setAdapter(mAdapter);
        if (currentPosition>0){
            mLinearLayoutManager.scrollToPosition(currentPosition);
        }




    }
    public void setObservable() {
        mMaterialViewModel.getMaterialesByIdOrden(mOrden.getIdOrden()).removeObservers(this);
        mMaterialViewModel.getMaterialesByIdOrden(mOrden.getIdOrden()).observe(this, new Observer<List<MaterialCapturado>>() {
            @Override
            public void onChanged(List<MaterialCapturado> list) {
                update_list(list);
            }
        });

    }

    @Override
    public void onRequestBeforeStart(String fromTask) {

    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {

    }

    @Override
    public void onRequestError(String fromTask, Exception error) {
        setObservable();
    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {

    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        LogSNE.d("NERUS","fromTask:"+fromTask);
        setObservable();
    }
}
