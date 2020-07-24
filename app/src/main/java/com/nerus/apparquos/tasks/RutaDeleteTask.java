package com.nerus.apparquos.tasks;

import android.os.AsyncTask;

import com.nerus.apparquos.daos.CuentaDAO;
import com.nerus.apparquos.daos.LecturaDAO;
import com.nerus.apparquos.daos.RutaDAO;
import com.nerus.apparquos.entities.Ruta;


public class RutaDeleteTask extends AsyncTask<Ruta, Integer, Object> {
    public static final String TASK_NAME="RutaDeleteTask";
    private final LecturaDAO mAsyncRutaDAO;
    private TasksCallBacks mCallBacks;
    private CuentaDAO mAsyncObjectDAO;
    public RutaDeleteTask(CuentaDAO objectDAO, LecturaDAO rutaDAO) {
        super();
        mAsyncObjectDAO = objectDAO;
        mAsyncRutaDAO =rutaDAO;
    }

    public RutaDeleteTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(Ruta... values) {
        Ruta ruta = (Ruta) values[0];
        mAsyncRutaDAO.deleteLecturasByRuta(ruta.getSb(),ruta.getSector(),ruta.getIdRuta());
        mAsyncObjectDAO.deleteByID(ruta.getIdRuta());
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
