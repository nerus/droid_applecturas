package com.nerus.apparquos.tasks;

import android.os.AsyncTask;

import com.nerus.apparquos.entities.MaterialCapturado;
import com.nerus.apparquos.entities.Orden;
import com.nerus.apparquos.entities.OrdenCerrada;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

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

public class MaterialUpLoadTask extends AsyncTask<OrdenCerrada, Integer, Object> {
    public static final String TASK_NAME="MaterialUpLoadTask";
    private TasksCallBacks mCallBacks;
    private String mUrlApi;
    private String mTokenApp;
    private String mTokenUser;
    private String mFootPrint;
    private String mXmlToSend;
    private Integer mIdPersonal;
    private String mIdUsuario;
    private List<MaterialCapturado> mObjectToReturn;

    //private Lectura saveLectura;



    public MaterialUpLoadTask(String urlApi, String tokenApp, String tokenUser, String footPrint, String xmlToSend, Integer idPersonal, String idUsuario) {
        super();
        mUrlApi=urlApi;
        mTokenApp=tokenApp;
        mTokenUser=tokenUser;
        mXmlToSend = xmlToSend;
        mIdPersonal = idPersonal;
        mIdUsuario = idUsuario;
        mFootPrint=footPrint;
    }

    public MaterialUpLoadTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(OrdenCerrada... values) {


        String url = mUrlApi + "/materiales/";
        HttpURLConnection urlConnection = null;
        try {


            HashMap<String, String> params = new HashMap<>();
            params.put("id_personal",mIdPersonal.toString());
            params.put("id_capturo",mIdUsuario);
            params.put("handheld", mFootPrint);
            params.put("xmlTables",mXmlToSend);
            params.put("alias","UPLOAD_MATERIALES");


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
            LogSNE.d("NERUS", "MaterialUpLoadTask The response  is: " + response);
            try {

                JSONObject jsonResult = new JSONObject(response);
                if (jsonResult.has("data") == true) {
                    try {

                        JSONArray jArray = jsonResult.getJSONArray("data");
                        LogSNE.d("NERUS", "The response  is: " + jArray.toString());
                        mObjectToReturn = new ArrayList<MaterialCapturado>();

                        for (Integer i = 0; i < jArray.length(); i++) {
                            this.publishProgress(i + 1, jArray.length());
                            JSONObject oneObject = jArray.getJSONObject(i);

                            String cFecha = clsFecha.getFechaYMDhms();
                            MaterialCapturado item = new MaterialCapturado(
                                    oneObject.getString("udm")
                                    ,oneObject.getString("id_articulo")
                                    ,oneObject.getString("costo")
                                    ,oneObject.getString("descripcion")
                                    ,oneObject.getString("existencia")
                                    ,""
                                    ,""
                            );
                            mObjectToReturn.add(item);


                            LogSNE.d("NERUS", "Object :" + i.toString() + ".-" + item.toString());
                        }

                        //mAsyncObjectDAO.updateData(mObjectToReturn);

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
                    mCallBacks.onRequestSuccess(TASK_NAME, mObjectToReturn);
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
