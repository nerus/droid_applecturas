package com.nerus.apparquos.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.nerus.apparquos.daos.LecturaDAO;
import com.nerus.apparquos.daos.ParametroDAO;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.helpers.clsFecha;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class LecturaFileCsvTask extends AsyncTask<Lectura, Integer, Object> {
    public static final String TASK_NAME="LecturaFileCsvTask";
    private final File mFileCsv;
    private final List<Lectura> mLecturaList;
    private final Uri mFileUri;
    private TasksCallBacks mCallBacks;

    private Lectura objectToReturn;


    public LecturaFileCsvTask(File csvFile, Uri fileUri, List<Lectura> lecturaList) {
        super();
        mFileCsv = csvFile;
        mLecturaList = lecturaList;
        mFileUri = fileUri;
    }

    public LecturaFileCsvTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(Lectura... values) {

        try {
            mFileCsv.createNewFile();
            FileOutputStream fos = new FileOutputStream(mFileCsv.toString(), true);

            FileWriter fw = new FileWriter(fos.getFD());
//LogSNE.d("NERUS","mFileCsv: "+mFileCsv.toString());
            fw.append('"');
            fw.append("ID_PADRON");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("POB");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("CUENTA");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("MEDIDOR");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("FECHA");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("LECTURA");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("ANOMALIA");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("LATITUD");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("LONGITUD");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("PERSONAL");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("MOBILE");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("OBSERVACION");
            fw.append('"');
            fw.append(',');


            fw.append('\n');
            for (Integer i = 0; i < mLecturaList.size(); i++) {

                Lectura  item = mLecturaList.get(i);
                fw.append('"');
                fw.append(item.getIdPadron().toString());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append("0");
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append("0");
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getIdMedidor());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getFecha());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getLectura().toString());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getIdAnomalia().toString());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(String.valueOf(item.getLatitud()));
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(String.valueOf(item.getLongitud()));
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getIdPersonal().toString());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(clsFecha.getFootPrint());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getObservaciones());
                fw.append('"');
                fw.append(',');

                fw.append('\n');

            }

            fw.flush();
            fw.close();
            fos.close();
            return "OK";
        } catch (IOException e) {
            LogSNE.d("NERUS", "LecturaFileCsvTask IOException:"+ e.toString());
            return e.getMessage();
        }
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
                mCallBacks.onRequestError(TASK_NAME, new Exception("Unknown Error. Contact your Admin"));
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
