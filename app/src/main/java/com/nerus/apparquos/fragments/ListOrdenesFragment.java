package com.nerus.apparquos.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.MainActivity;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.OnItemListener;
import com.nerus.apparquos.adapters.OrdenAdapter;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.entities.Trabajo;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.OrdenViewModel;
import com.nerus.apparquos.viewmodels.ParametroViewModel;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public final class ListOrdenesFragment extends Fragment implements OnItemListener, TasksCallBacks {
    private ViewModelProvider mViewModelProvider;
    private OrdenViewModel mOrdenViewModel;
    private ParametroViewModel mParametroViewModel;

    private List<Orden> mOrdenList;
    private Trabajo currentTrabajo;
    private Orden currentOrden;
    private Integer currentPosition=0;
    private OrdenAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private LayoutInflater mLayoutInflater;
    private Boolean mLoaded=false;


    private AppArquos mAppArquos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppArquos = (AppArquos) getActivity().getApplicationContext();

        mLoaded=false;
        Bundle args = getArguments();
        currentTrabajo = (Trabajo) args.get("TRABAJO");

        mViewModelProvider = ViewModelProviders.of(this);
        mOrdenViewModel = mViewModelProvider.get(OrdenViewModel.class);
        mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);

        setHasOptionsMenu(true);


    }

    @Override
    public void onStart() {
        super.onStart();

        clsUtilities.setHomeBack(getActivity(),"ORDENES");

        setObservable();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayoutInflater = inflater;
        final View v = mLayoutInflater.inflate(R.layout.listordenes_fragment, container, false);


        mRecyclerView = (RecyclerView) v.findViewById(R.id.listOrdenes);
        mLinearLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mOrdenViewModel.saveLastTrabajo(mTrabajoList.get(currentPosition));
    }

    @Override
    public void onItemClick(Integer position, Object item) {
LogSNE.d("NERUS","ListOrdenesFragment.onItemClick " + mOrdenList.get(position).toString());
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

    }

    @Override
    public void onItemValueChanged(Object itemChanged) {

    }

    @Override
    public void onItemRemoved(Object itemToRemove) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.listordenes_menu, menu);
// Associate searchable configuration with the SearchView
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
                update_list(mOrdenList);
                return false;
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        LogSNE.d("NERUS","GetRutaLecturarFragment.onActivityResult :" +  String.valueOf(resultCode)) ;
        if (requestCode == clsRequestCodes.REQUEST_LISTORDEN){

        }
    }
    private void update_list(List<Orden> ordenes) {
        mOrdenList = ordenes;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(mOrdenList.size()) + " ORDENES - " + currentTrabajo.getDescripcion());

        mAdapter = new OrdenAdapter(mOrdenList, mLayoutInflater, this);
        mRecyclerView.setAdapter(mAdapter);
        if (currentPosition>0){
            mLinearLayoutManager.scrollToPosition(currentPosition);
        }
        if (!mLoaded){
            mParametroViewModel.getLastOrden().observe(this, new Observer<Parametro>() {
                @Override
                public void onChanged(@Nullable final Parametro parametro) {
                    mLoaded=true;
                    // Update the cached copy of the subsistemas in the adapter.
                    if (parametro != null && mOrdenList!=null && mOrdenList.size() > 0) {
                        LogSNE.d("NERUS",parametro.toString());
                        String idOrden = parametro.getValor();
                        Integer nCnt = 0;
                        for (Integer i=0; i<mOrdenList.size(); i++){
                            Orden item = mOrdenList.get(i);

                            if (idOrden.equals(item.getIdOrden())){
                                //LogSNE.d("NERUS","encontrado " +idPadron.toString()+ " " + item.getId_Padron().toString() + " m=" + item.getMedidor());
                                currentPosition = i;
                                currentOrden = item;
                                break;
                            }
                        }
                        if (currentPosition>0){
                            mLinearLayoutManager.scrollToPosition(currentPosition);
                        }
                    }
                }
            });

        }



    }
    public void setObservable() {
        if (currentTrabajo.getIdTipo().equals(1) && currentTrabajo.getIdTrabajo()>0){
            mOrdenViewModel.getOrdenesByTrabajo (mAppArquos.getEmpleado().getIdPersonal(), currentTrabajo.getIdTrabajo()).observe(this, new Observer<List<Orden>>() {
                @Override
                public void onChanged(@Nullable final List<Orden> ordens) {
                    // Update the cached copy of the trabajos in the adapter.
                    update_list(ordens);
                }
            });
        }else if (currentTrabajo.getIdTipo().equals(2) && currentTrabajo.getIdTrabajo()>0){
            mOrdenViewModel.getOrdenesByColonia (mAppArquos.getEmpleado().getIdPersonal(), currentTrabajo.getDescripcion()).observe(this, new Observer<List<Orden>>() {
                @Override
                public void onChanged(@Nullable final List<Orden> ordens) {
                    // Update the cached copy of the trabajos in the adapter.
                    update_list(ordens);
                }
            });
        }else{
            mOrdenViewModel.getOrdenes (mAppArquos.getEmpleado().getIdPersonal()).observe(this, new Observer<List<Orden>>() {
                @Override
                public void onChanged(@Nullable final List<Orden> ordens) {
                    // Update the cached copy of the trabajos in the adapter.
                    update_list(ordens);
                }
            });
        }


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
