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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.CuentaAdapter;
import com.nerus.apparquos.adapters.OnItemListener;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.entities.Ruta;
import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.viewmodels.CuentaViewModel;
import com.nerus.apparquos.viewmodels.ParametroViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public final class ListCuentasFragment extends Fragment implements OnItemListener {
    private ViewModelProvider mViewModelProvider;
    private CuentaViewModel mCuentaViewModel;
    private ParametroViewModel mParametroViewModel;

    private List<Cuenta> mCuentaList;
    private Cuenta currentCuenta;
    private Integer currentPosition=0;
    private CuentaAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private LayoutInflater mLayoutInflater;
    private Ruta mRuta;
    private Boolean mLoaded=false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoaded=false;

        Bundle args = getArguments();
        mRuta = (Ruta) args.get("RUTA");

        mViewModelProvider = ViewModelProviders.of(this);
        mCuentaViewModel = mViewModelProvider.get(CuentaViewModel.class);
        mParametroViewModel = mViewModelProvider.get(ParametroViewModel.class);
        //this.setObservable();


        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        clsUtilities.setHomeBack(getActivity(),mRuta.getDescripcion());
        this.setObservable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayoutInflater = inflater;
        final View v = mLayoutInflater.inflate(R.layout.listcuentas_fragment, container, false);


        mRecyclerView = (RecyclerView) v.findViewById(R.id.listCuentas);
        mLinearLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //LogSNE.d("NERUS","SALVAR LA RUTA "+ mRutaList.get(positionRuta).getDescripcion());
        mCuentaViewModel.saveLastCuenta(mCuentaList.get(currentPosition));
    }

    @Override
    public void onItemClick(Integer position, Object item) {
LogSNE.d("NERUS","ListCuentasFragment.onItemClick " + mCuentaList.get(position).toString());
        this.currentCuenta = (Cuenta) item; //  mCuentaList.get(position);
        this.currentPosition=position;
        if (this.currentCuenta.getId_Padron()!=mCuentaList.get(position).getId_Padron()){
            //viene de un filtrado buscar la posicion correcta
            this.currentPosition =            mCuentaList.indexOf(this.currentCuenta);
        }

        FragmentManager fm = getActivity(). getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_getruta);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("CUENTA",currentCuenta);

            fragment = new CapturaLecturaFragment();
            fragment.setTargetFragment(this, clsRequestCodes.REQUEST_CAPTURALECTURA);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("LIST_CUENTAS")
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
        inflater.inflate(R.menu.listcuentas_menu, menu);
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
                LogSNE.d("NERUS","onQueryTextSubmit-query="+query);
                mAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed

                LogSNE.d("NERUS","onQueryTextChange-query="+query);
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                update_listCuentas(mCuentaList);
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
        if (requestCode == clsRequestCodes.REQUEST_CAPTURALECTURA){
            Cuenta cuenta = (Cuenta) data.getSerializableExtra("CUENTA");
            mCuentaList.get(currentPosition).setCapturado(true);
            mAdapter.setData(mCuentaList);
        }
    }
    private void update_listCuentas(List<Cuenta> cuentas) {
        mCuentaList = cuentas;
        mAdapter = new CuentaAdapter(mCuentaList, mLayoutInflater, this);
        mRecyclerView.setAdapter(mAdapter);
        if (currentPosition>0){
            mLinearLayoutManager.scrollToPosition(currentPosition);
        }
        if (!mLoaded){
            mParametroViewModel.getLastCuenta().observe(this, new Observer<Parametro>() {
                @Override
                public void onChanged(@Nullable final Parametro parametro) {
                    mLoaded=true;
                    // Update the cached copy of the subsistemas in the adapter.
                    if (parametro != null && mCuentaList!=null && mCuentaList.size() > 0) {
                        LogSNE.d("NERUS",parametro.toString());
                        Integer idPadron = Integer.valueOf(parametro.getValor());
                        Integer nCnt = 0;
                        for (Integer i=0; i<mCuentaList.size(); i++){
                            Cuenta item = mCuentaList.get(i);

                            if (idPadron.equals(item.getId_Padron())){
                                LogSNE.d("NERUS","encontrado " +idPadron.toString()+ " " + item.getId_Padron().toString() + " m=" + item.getMedidor());
                                currentPosition = i;
                                currentCuenta = item;
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
        mCuentaViewModel.getCuentasDeRuta(mRuta.getSb(),mRuta.getSector(),mRuta.getIdRuta()).observe(this, new Observer<List<Cuenta>>() {
            @Override
            public void onChanged(@Nullable final List<Cuenta> cuentas) {
                // Update the cached copy of the subsistemas in the adapter.
                //LogSNE.d("NERUS","getCuentasDeRuta.onChanged " + cuentas.get(0).toString());
                update_listCuentas(cuentas);
            }
        });
    }
}
