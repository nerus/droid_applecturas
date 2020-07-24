package com.nerus.apparquos.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.nerus.apparquos.daos.CuentaDAO;
import com.nerus.apparquos.daos.LecturaDAO;
import com.nerus.apparquos.daos.RutaDAO;
import com.nerus.apparquos.entities.Cuenta;
import com.nerus.apparquos.entities.Ruta;
import com.nerus.apparquos.helpers.clsFecha;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
public class CuentasDownloadTask extends AsyncTask<String, Integer, Object> {
    public static final String TASK_NAME="CuentasDownloadTask";
    private final LecturaDAO mLecturaDAO;
    private TasksCallBacks mCallBacks;
    private Ruta mObjectToReturn;
    private String mUrlApi;
    private String mTokenApp;
    private String mTokenUser;
    private RutaDAO mRutaDAO;
    private CuentaDAO mAsyncObjectDAO;
    private String mSb;
    private String mSec;
    private String mRuta;
    private Ruta mRutaToDownload;
    public CuentasDownloadTask(String urlApi, String tokenApp, String tokenUser, RutaDAO rutaDAO, CuentaDAO objectDAO, LecturaDAO lecturaDAO, Ruta rutaToDownload) {
        super();
        mUrlApi=urlApi;
        mTokenApp=tokenApp;
        mTokenUser=tokenUser;
        mAsyncObjectDAO = objectDAO;
        mRutaDAO = rutaDAO;
        mRutaToDownload = rutaToDownload;
        mLecturaDAO = lecturaDAO;
    }

    public CuentasDownloadTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(String... values) {
        mSb  =  values[0];
        mSec =  values[1];
        mRuta =  values[2];
        String nOrderBy =  values[3];
        String cRFC =  values[4];
        String url = mUrlApi + "/cuentas/"+mSb+"/"+mSec+"/"+mRuta;
        if (cRFC.equals("CMA020822651")){
            url = mUrlApi + "/lista_cuentas/"+mSb+"/"+mSec+"/"+mRuta+"/"+nOrderBy;
        }
        LogSNE.d("NERUS", "The url  is: " + url);
        HttpURLConnection urlConnection = null;
        try {


            HashMap<String, String> params = new HashMap<>();
            //params.put("username",username);
            //params.put("password",contrasena);
            //params.put("handheld",handheld);

            String requestBody = HttpHelper.buildPostParameters(params);
            urlConnection = (HttpURLConnection) HttpHelper.makeRequest("GET", url, mTokenApp, mTokenUser, "application/x-www-form-urlencoded", requestBody);
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
            //LogSNE.d("NERUS", "The response  is: " + response);
            try {

                JSONObject jsonResult = new JSONObject(response);
                if (jsonResult.has("data") == true) {
                    try {
                        JSONArray jArray = jsonResult.getJSONArray("data");
                        LogSNE.d("NERUS", "The response  is: " + jArray.toString());

                        String cObs = "Descargada "+ clsFecha.getCurrentDate();

                        List<Cuenta> cuentaList = new ArrayList<Cuenta>();






                        for (Integer i = 0; i < jArray.length(); i++) {
                            this.publishProgress(((i + 1) * 100) / jArray.length(), jArray.length());
                            JSONObject oneObject = jArray.getJSONObject(i);

                            // Pulling items from the array
                            Cuenta content = new Cuenta(
                                     oneObject.getInt("id_padron")
                                    ,oneObject.getInt("idrow")
                                    ,Integer.valueOf(mSb)
                                    ,Integer.valueOf(mSec)
                                    ,Integer.valueOf(mRuta)
                                    ,oneObject.getInt("id_padron")
                                    ,oneObject.getInt("id_cuenta")
                                    ,oneObject.getInt("id_poblacion")
                                    ,oneObject.getString("razon_social")
                                    ,oneObject.getString("direccion")
                                    ,oneObject.getString("clase_usuario")
                                    ,oneObject.getString("giro")
                                    ,oneObject.getString("medidor")
                                    ,oneObject.getString("localizacion")
                                    ,oneObject.getString("ruta")
                                    ,oneObject.getString("servicio")
                                    ,oneObject.getString("situacion")
                                    ,oneObject.getString("tarifa")
                                    ,oneObject.getString("tipo_calculo")
                                    ,oneObject.getInt("id_giro")
                                    ,oneObject.getInt("id_tipocalculo")
                                    ,oneObject.getInt("lectura_ant")
                                    ,oneObject.getInt("promedio")
                                    ,oneObject.getDouble("latitud")
                                    ,oneObject.getDouble("longitud")
                                    ,false
                            );
                            cuentaList.add(content);
                            /*
                            try {
                                mAsyncObjectDAO.insert(content);
                            }catch (Exception e){
                                LogSNE.d("NERUS", "Error at InsertDAO :" + e.getMessage());
                            }*/

                            //LogSNE.d("NERUS", "Object :" + i.toString() + ".-" + content.toString());
                        }
                        try {
                            mAsyncObjectDAO.insertAll(cuentaList);
                            mLecturaDAO.deleteLecturasByRuta(Integer.valueOf( mSb),Integer.valueOf(mSec),mRutaToDownload.getIdRuta());
                            mRutaToDownload.setObservaciones( cObs);
                            //mRutaDAO.update(mRutaToDownload);
                        }catch (Exception e){
                            LogSNE.d("NERUS", "Error at InsertDAO :" + e.getMessage());
                        }
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
                    mCallBacks.onRequestSuccess(TASK_NAME, mRutaToDownload);

                    /*try {

                        //LogSNE.d("NERUS", "The Responce is: " + Responce.toString());
                        JSONObject jsonResult = new JSONObject(Responce);
                        if (jsonResult.has("error") == false) {

                            mCallBacks.onRequestSuccess(TASK_NAME, "OK");
                        } else {
                            String cMsg = jsonResult.getString("error").toString().toUpperCase() + ".-" + jsonResult.getString("descripcion").toString();
                            mCallBacks.onRequestError(TASK_NAME, new Exception(cMsg));
                        }



                    } catch (Exception e) {
                        //LogSNE.d("NERUS", "The Exception is: " + e.toString());
                        mCallBacks.onRequestError(TASK_NAME, e);
                    } finally {
                    }*/

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
