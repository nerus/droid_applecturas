package com.nerus.apparquos.tasks;

import android.os.AsyncTask;

import com.nerus.apparquos.daos.OrdenDAO;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.helpers.clsFecha;

import java.util.List;


public class OrdenCRUDTask extends AsyncTask<Orden, Integer, Object> {
    public static final String TASK_NAME="OrdenCRUDTask";
    private TasksCallBacks mCallBacks;
    private OrdenDAO mAsyncObjectDAO;
    private String mAction;
    private List<Orden> mOrdenList=null;
    public OrdenCRUDTask(OrdenDAO objectDAO, String action) {
        super();
        mAsyncObjectDAO = objectDAO;
        mAction=action;
    }
    public OrdenCRUDTask(OrdenDAO objectDAO, String action, List<Orden> list) {
        super();
        mAsyncObjectDAO = objectDAO;
        mAction=action;
        mOrdenList = list;
    }
    public OrdenCRUDTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(Orden... values) {
        if (mAction.equals("INSERT")){
            Orden item = (Orden) values[0];
            mAsyncObjectDAO.insert(item);
        }else if (mAction.equals("UPDATE")){
            Orden item = (Orden) values[0];
            mAsyncObjectDAO.update(item);
        }else if (mAction.equals("SET_UPLOAD")){
            if (mOrdenList!=null && mOrdenList.size()>0){
                for (int i = 0; i < mOrdenList.size(); i++) {
                    Orden item = mOrdenList.get(i);
                    String cFecha = clsFecha.getCurrentDate();
                    mAsyncObjectDAO.setUpLoad(item.getIdOrden(), cFecha);
                }
            }
        }

        return "OK";
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mCallBacks != null ) {
            mCallBacks.onRequestBeforeStart(TASK_NAME);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (mCallBacks != null ) {

            if (result instanceof String) {
                String cResult = (String) result;
                if (cResult.equals("OK")) {
                    mCallBacks.onRequestSuccess(TASK_NAME, "OK");
                } else {
                    mCallBacks.onRequestError(TASK_NAME, new Exception(cResult));
                }
            } else if (result instanceof Exception) {
                mCallBacks.onRequestError(TASK_NAME, (Exception) result);
            } else {
                mCallBacks.onRequestError(TASK_NAME, new Exception("Unknown Error Contacting Host"));
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mCallBacks != null ) {
            mCallBacks.onProgressUpdate(TASK_NAME, values[0],values[1]);
        }

    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
        if (mCallBacks != null ) {
            mCallBacks.onRequestCancel (TASK_NAME, new Exception(o.toString()));
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mCallBacks != null ) {
            mCallBacks.onRequestCancel (TASK_NAME, new Exception("Cancelado"));
        }
    }
}
