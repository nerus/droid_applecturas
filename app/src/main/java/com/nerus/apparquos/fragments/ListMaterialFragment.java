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


import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.R;
import com.nerus.apparquos.adapters.MaterialAdapter;

import com.nerus.apparquos.adapters.OnItemListener;
import com.nerus.apparquos.entities.Material;

import com.nerus.apparquos.helpers.clsRequestCodes;
import com.nerus.apparquos.helpers.clsUtilities;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.viewmodels.MaterialViewModel;

import java.util.List;

public final class ListMaterialFragment extends Fragment implements OnItemListener, TasksCallBacks {
    private ViewModelProvider mViewModelProvider;
    private MaterialViewModel mMaterialViewModel;
    //private ParametroViewModel mParametroViewModel;

    private List<Material> mCurrentList;

    private Material currentItem;
    private Integer currentPosition=0;
    private MaterialAdapter mAdapter;
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



    @Override
    public void onDestroy() {
        super.onDestroy();
        //mOrdenViewModel.saveLastTrabajo(mTrabajoList.get(currentPosition));
    }

    @Override
    public void onItemClick(Integer position, Object item) {
LogSNE.d("NERUS","ListMaterialCapturadoFragment.onItemClick " + item.toString());
        currentItem = (Material) item;

        sendResult(Activity.RESULT_OK);
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

    }

    @Override
    public void onItemRemoved(Object itemToRemove) {

    }

    private void sendResult(int resultCode) {
        if (getTargetFragment()==null){
            return;
        }


        Intent intent = new Intent();
        intent.putExtra("MATERIAL", currentItem);
        getTargetFragment().onActivityResult(clsRequestCodes.REQUEST_MATERIAL , resultCode, intent);

        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
            boolean done = getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
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
                update_list(mCurrentList);
                return false;
            }
        });


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
            //cmdSave_onClick(item.getActionView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        LogSNE.d("NERUS","ListMaterialFragment.onActivityResult :" +  String.valueOf(resultCode)) ;
        if (requestCode == clsRequestCodes.REQUEST_LISTMATCAPTURADO){

        }
    }
    private void update_list(List<Material> list) {
        mCurrentList = list;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(list.size()) + "BUSCAR MATERIAL" );

        mAdapter = new MaterialAdapter(mCurrentList, mLayoutInflater, this);
        mRecyclerView.setAdapter(mAdapter);
        if (currentPosition>0){
            mLinearLayoutManager.scrollToPosition(currentPosition);
        }




    }
    public void setObservable() {
        mMaterialViewModel.getMateriales().removeObservers(this);
        mMaterialViewModel.getMateriales().observe(this, new Observer<List<Material>>() {
            @Override
            public void onChanged(List<Material> list) {
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
