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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.MainActivity;
import com.nerus.apparquos.MapsActivity;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.CuentaAdapter;
import com.nerus.apparquos.adapters.OnItemListener;
import com.nerus.apparquos.adapters.TrabajoAdapter;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.entities.Trabajo;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.OrdenViewModel;
import com.nerus.apparquos.viewmodels.ParametroViewModel;

import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
import com.nerus.apparquos.viewmodels.UsuarioViewModel;

public final class ListResumenOTFragment extends Fragment implements OnItemListener, TasksCallBacks {
    private ViewModelProvider mViewModelProvider;
    private OrdenViewModel mOrdenViewModel;
    private ParametroViewModel mParametroViewModel;

    private List<Trabajo> mTrabajoList;
    private Trabajo currentTrabajo;
    private Integer positionTrabajo=0;
    private TrabajoAdapter mAdapterTrabajo;
    private RecyclerView mRVTrabajo;
    private LinearLayoutManager mLLayoutManagerTrabajos;
    private LayoutInflater mLayoutInflater;
    private Boolean mLoaded=false;


    private AppArquos mAppArquos;
    private Usuario mUsuario=null;
    private UsuarioViewModel mUsuarioViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAppArquos = (AppArquos) getActivity().getApplicationContext();
        mLoaded=false;

        mViewModelProvider = ViewModelProviders.of(this);
        mOrdenViewModel = mViewModelProvider.get(OrdenViewModel.class);
        mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);
        mUsuarioViewModel = mViewModelProvider.get(UsuarioViewModel.class);


        //setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        clsUtilities.setHomeBack(getActivity(),"RESUMEN");

        setObservable();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayoutInflater = inflater;
        final View v = mLayoutInflater.inflate(R.layout.listresumen_fragment, container, false);


        mRVTrabajo = (RecyclerView) v.findViewById(R.id.listTrabajos);
        mLLayoutManagerTrabajos = new LinearLayoutManager(container.getContext());
        mRVTrabajo.setLayoutManager(mLLayoutManagerTrabajos);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mOrdenViewModel.saveLastTrabajo(mTrabajoList.get(positionTrabajo));
    }

    @Override
    public void onItemClick(Integer position, Object item) {
LogSNE.d("NERUS","ListResumenOTFragment.onItemClick " + mTrabajoList.get(position).toString());
        this.currentTrabajo =  (Trabajo) item; // mTrabajoList.get(position);
        this.positionTrabajo=position;

        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("TRABAJO",currentTrabajo);

            fragment = new ListOrdenesFragment();
            fragment.setTargetFragment(this, clsRequestCodes.REQUEST_LISTORDEN);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("LIST_ORDENES")
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
        inflater.inflate(R.menu.resumenot_menu, menu);
//// Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView();
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getActivity().getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        // listening to search query text change
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // filter recycler view when query submitted
//                mAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                mAdapter.getFilter().filter(query);
//                return false;
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download) {

            mOrdenViewModel.downloadOrdenes(mAppArquos.getEmpleado().getIdPersonal(),this);
            return true;
        }
        if (id == R.id.action_upload) {

            if (mTrabajoList!=null && mTrabajoList.size()>0 && mTrabajoList.get(0).getCapturadas()>0){
                String cUsario = "";
                if (mUsuario!=null) cUsario=mUsuario.getUsuario();

                FragmentManager fm = getActivity(). getSupportFragmentManager();
                final LoginUserArquosFragment loginUserArquos =  LoginUserArquosFragment.newInstance( cUsario );
                loginUserArquos.setTargetFragment(ListResumenOTFragment.this, clsRequestCodes.REQUEST_LOGINUSRARQUOS);
                loginUserArquos.setLoginUserArquosListener(new LoginUserArquosFragment.LoginUserArquosListener() {
                    @Override
                    public void onResultLoginUserArquosDialog (Boolean successful, Usuario usuario) {
                        if (successful){
                            LogSNE.d("NERUS", "Usuario:" + usuario.toString());
                            subirOrdenes(usuario);
                        }else{
                            Snackbar.make(getView(), "Debe capturar un usuario y contraseña de Arquos®", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
                loginUserArquos.show(fm,"DIALOG_LOGINUSERARQUOS");

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void subirOrdenes(Usuario usuario) {
        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            Trabajo dataJob = mTrabajoList.get(0);
            bundle.putSerializable("TRABAJO",dataJob);
            bundle.putSerializable("USUARIO",usuario);

            fragment = new UpLoadOrdenesFragment();
            fragment.setTargetFragment(this, clsRequestCodes.REQUEST_UPLOADLECTURAS);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("UPLOAD_ORDENES")
                    .commit();
        }
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
    private void update_listTrabajos(List<Trabajo> trabajos) {
        mTrabajoList = trabajos;
        mAdapterTrabajo = new TrabajoAdapter(mTrabajoList, mLayoutInflater, this);
        mRVTrabajo.setAdapter(mAdapterTrabajo);
        if (positionTrabajo>0){
            mLLayoutManagerTrabajos.scrollToPosition(positionTrabajo);
        }
        if ( false && !mLoaded){
            mParametroViewModel.getLastTrabajo().observe(this, new Observer<Parametro>() {
                @Override
                public void onChanged(@Nullable final Parametro parametro) {
                    mLoaded=true;
                    // Update the cached copy of the subsistemas in the adapter.
                    if (parametro != null && mTrabajoList!=null && mTrabajoList.size() > 0) {
                        LogSNE.d("NERUS",parametro.toString());
                        Integer idTrabajo = Integer.valueOf(parametro.getValor());
                        Integer nCnt = 0;
                        for (Integer i=0; i<mTrabajoList.size(); i++){
                            Trabajo item = mTrabajoList.get(i);

                            if (idTrabajo.equals(item.getIdTrabajo())){
                                //LogSNE.d("NERUS","encontrado " +idPadron.toString()+ " " + item.getId_Padron().toString() + " m=" + item.getMedidor());
                                positionTrabajo = i;
                                currentTrabajo = item;
                                break;
                            }
                        }
                        if (positionTrabajo>0){
                            mLLayoutManagerTrabajos.scrollToPosition(positionTrabajo);
                        }
                    }
                }
            });

        }



    }

    public void setObservable() {
        mOrdenViewModel.getResumenByTrabajo (mAppArquos.getEmpleado().getIdPersonal()).removeObservers(this);
        mOrdenViewModel.getResumenByTrabajo (mAppArquos.getEmpleado().getIdPersonal()).observe(this, new Observer<List<Trabajo>>() {
            @Override
            public void onChanged(@Nullable final List<Trabajo> trabajos) {
                // Update the cached copy of the trabajos in the adapter.
                update_listTrabajos(trabajos);
                getView().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            }
        });
        mUsuarioViewModel.getUsuario().observe(this,new Observer<Usuario>(){
            @Override
            public void onChanged(@Nullable final Usuario usuario) {
                mUsuario = usuario;
            }

        });
        /*
        mOrdenViewModel.getResumenByColonia (Integer.valueOf(mEmpleado.getValor())).observe(this, new Observer<List<Trabajo>>() {
            @Override
            public void onChanged(@Nullable final List<Trabajo> trabajos) {
                // Update the cached copy of the trabajos in the adapter.
                update_listColonias(trabajos);
            }
        });
        */
    }

    @Override
    public void onRequestBeforeStart(String fromTask) {
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {

    }

    @Override
    public void onRequestError(String fromTask, Exception error) {
        //setObservable();
    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {

    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        LogSNE.d("NERUS","fromTask:"+fromTask);
        //setObservable();
    }
}
