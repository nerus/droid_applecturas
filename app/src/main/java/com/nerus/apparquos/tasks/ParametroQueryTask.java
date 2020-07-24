package com.nerus.apparquos.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.nerus.apparquos.daos.ParametroDAO;
import com.nerus.apparquos.entities.AppArquosRepository;
import com.nerus.apparquos.entities.Parametro;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

public class ParametroQueryTask extends AsyncTask<String, Integer, Object> {
    public static final String TASK_NAME="ParametroQueryTask";
    private TasksCallBacks mCallBacks;
    private ParametroDAO mAsyncObjectDAO;
    private Parametro objectToReturn;
    public ParametroQueryTask(ParametroDAO objectDAO) {
        super();
        mAsyncObjectDAO = objectDAO;
    }

    public ParametroQueryTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(String... values) {
        String name =  values[0];
        LogSNE.d("NERUS","NAME:"+name);
        objectToReturn = mAsyncObjectDAO.getParametroOfName(name);
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
                    mCallBacks.onRequestSuccess(TASK_NAME, objectToReturn);
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
