package com.nerus.apparquos.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.nerus.apparquos.daos.SectorDAO;
import com.nerus.apparquos.entities.Sector;

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
public class SectoresDownloadTask extends AsyncTask<String, Integer, Object> {
    public static final String TASK_NAME="SectoresDownloadTask";
    private TasksCallBacks mCallBacks;
    private List<Sector> mObjectToReturn;
    private String mUrlApi;
    private String mTokenApp;
    private String mTokenUser;
    private SectorDAO mAsyncObjectDAO;
    private String mSb;
    public SectoresDownloadTask(String urlApi, String tokenApp, String tokenUser, SectorDAO objectDAO) {
        super();
        mUrlApi=urlApi;
        mTokenApp=tokenApp;
        mTokenUser=tokenUser;
        mAsyncObjectDAO = objectDAO;
    }

    public SectoresDownloadTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(String... values) {
        mSb =  values[0];
        String url = mUrlApi + "/sectores/"+mSb;
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

                        mObjectToReturn = new ArrayList<Sector>();

                        Sector hintContent = new Sector(-1, Integer.valueOf(mSb),-1,"-- SIN ESPECIFICAR --","0" );
                        //mAsyncObjectDAO.insert(hintContent);
                        mObjectToReturn.add(hintContent);
                        for (Integer i = 0; i < jArray.length(); i++) {
                            this.publishProgress(((i + 1) * 100) / jArray.length(), jArray.length());
                            JSONObject oneObject = jArray.getJSONObject(i);
                            // Pulling items from the array
                            Sector content = new Sector(oneObject.getInt("sector")
                                    ,Integer.valueOf(mSb)
                                    ,oneObject.getInt("sector")
                                    ,oneObject.getString("descripcion")
                                    ,oneObject.getString("registros"));

                            mObjectToReturn.add(content);
                            /*
                            try {
                                mAsyncObjectDAO.insert(content);
                            }catch (Exception e){
                                LogSNE.d("NERUS", "Error at InsertDAO :" + e.getMessage());
                            }
                            */

                            LogSNE.d("NERUS", "Object :" + i.toString() + ".-" + content.toString());
                        }
                        mAsyncObjectDAO.deleteAll();
                        mAsyncObjectDAO.insertAll(mObjectToReturn);
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
