package com.nerus.apparquos.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.nerus.apparquos.daos.LecturaDAO;
import com.nerus.apparquos.daos.ParametroDAO;
import com.nerus.apparquos.daos.UsuarioDAO;
import com.nerus.apparquos.entities.Lectura;
import com.nerus.apparquos.entities.Usuario;
import com.nerus.apparquos.helpers.clsFecha;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class LecturaUpLoadTask extends AsyncTask<Lectura, Integer, Object> {
    public static final String TASK_NAME="LecturaUpLoadTask";
    private TasksCallBacks mCallBacks;
    private String mUrlApi;
    private String mTokenApp;
    private String mTokenUser;
    private LecturaDAO mAsyncObjectDAO;
    private ParametroDAO mParametroDAO;

    private String mIdUsuario;
    private String mFootPrint;
    //private Lectura saveLectura;

    private Lectura objectToReturn;
    public LecturaUpLoadTask(String urlApi, String tokenApp, String tokenUser, String idUsuario, String footPrint, LecturaDAO objectDAO, ParametroDAO parametroDAO) {
        super();
        mUrlApi=urlApi;
        mTokenApp=tokenApp;
        mTokenUser=tokenUser;
        mAsyncObjectDAO = objectDAO;
        mParametroDAO = parametroDAO;
        mIdUsuario = idUsuario;
        mFootPrint=footPrint;

    }

    public LecturaUpLoadTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(Lectura... values) {
        Lectura saveLectura = (Lectura) values[0];

        String url = mUrlApi + "/capturas/";
        HttpURLConnection urlConnection = null;
        try {


            HashMap<String, String> params = new HashMap<>();
            params.put("id_personal",saveLectura.getIdPersonal().toString());
            params.put("observacion",saveLectura.getObservaciones());
            params.put("id_padron",saveLectura.getIdPadron().toString());
            params.put("id_anomalia",saveLectura.getIdAnomalia().toString());
            params.put("lectura",saveLectura.getLectura().toString());
            params.put("id_capturo",mIdUsuario);
            params.put("latitud",String.valueOf( saveLectura.getLatitud()));
            params.put("longitud",String.valueOf( saveLectura.getLongitud()));
            params.put("fecha",saveLectura.getFecha());
            params.put("handheld", mFootPrint);
            params.put("id_medidor",saveLectura.getIdMedidor());


            String requestBody = HttpHelper.buildPostParameters(params);
            urlConnection = (HttpURLConnection) HttpHelper.makeRequest("POST", url, mTokenApp, mTokenUser, "application/x-www-form-urlencoded", requestBody);
            InputStream inputStream;
            // get stream
            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            // parse stream
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp, response = "";
            while ((temp = bufferedReader.readLine()) != null) {
                response += temp;
            }
            LogSNE.d("NERUS", "LecturaUpLoadTask The response  is: " + response);
            try {

                JSONObject jsonResult = new JSONObject(response);
                if (jsonResult.has("data") == true) {
                    try {

                        this.publishProgress(1, 1);
                        JSONArray jArray = jsonResult.getJSONArray("data");

                        objectToReturn = saveLectura;

                        //JSONObject oneObject = jArray.getJSONObject(0);
                        //String cFecha = clsFecha.getFechaYMDhms();
                        //mAsyncObjectDAO.update( cFecha , saveLectura.getIdPadron());

                        return "OK";
                    } catch (JSONException je) {
                        return je.getMessage();
                    }

                } else {
                    if (jsonResult.has("error") == true) {
                        String cMsg = jsonResult.getString("error").toString().toUpperCase() + ".-" + jsonResult.getString("descripcion").toString();
                        return cMsg;
                    } else {
                        return jsonResult.toString();
                    }
                }
            } catch (JSONException e) {
                //lError=true;
                LogSNE.d("NERUS", "B.-The JSONException is: " + e.toString());
                //mResponseCallback.get().onRequestError(TASK_NAME, e);
                return e.getMessage();
            }
        } catch (IOException e) {
            return e.toString();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
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
