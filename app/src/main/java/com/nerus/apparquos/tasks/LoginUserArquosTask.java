package com.nerus.apparquos.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.nerus.apparquos.daos.ParametroDAO;

import com.nerus.apparquos.daos.UsuarioDAO;
import com.nerus.apparquos.entities.Parametro;
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

public class LoginUserArquosTask extends AsyncTask<String, Integer, Object> {
    public static final String TASK_NAME="LoginUserArquosTask";
    private TasksCallBacks mCallBacks;
    private String mUrlApi;
    private String mTokenApp;
    private String mTokenUser;
    private UsuarioDAO mAsyncObjectDAO;
    private ParametroDAO mParametroDAO;

    private Usuario objectToReturn;
    public LoginUserArquosTask(String urlApi, String tokenApp, String tokenUser, UsuarioDAO objectDAO, ParametroDAO parametroDAO) {
        super();
        mUrlApi=urlApi;
        mTokenApp=tokenApp;
        mTokenUser=tokenUser;
        mAsyncObjectDAO = objectDAO;
        mParametroDAO = parametroDAO;
    }

    public LoginUserArquosTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(String... values) {
        String username = values[0];
        String contrasena = values[1];
        String handheld   = values[2];

        String url = mUrlApi + "/usuarios/";
        HttpURLConnection urlConnection = null;
        try {


            HashMap<String, String> params = new HashMap<>();
            params.put("username",username);
            params.put("contrasena",contrasena);
            params.put("handheld",handheld);

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
            LogSNE.d("NERUS", "LoginUserArquosTask The response  is: " + response);
            try {

                JSONObject jsonResult = new JSONObject(response);
                if (jsonResult.has("data") == true) {
                    try {
                        //Empleado hintContent = new Empleado(-1, "-- SIN ESPECIFICAR --","","" );
                        //mAsyncObjectDAO.insert(hintContent);
                        this.publishProgress(1, 1);
                        JSONArray jArray = jsonResult.getJSONArray("data");
                        JSONObject oneObject = jArray.getJSONObject(0);
                        String idUsuario = oneObject.getString("id_usuario");

                        if (idUsuario!="-1" && idUsuario.length()>2){
                            // Pulling items from the array
                            objectToReturn = new Usuario(0 //oneObject.getInt("id_personal")
                                    ,1
                                    ,oneObject.getString("descripcion")
                                    , oneObject.getString("username")
                                    , oneObject.getString("id_usuario")
                                    ,oneObject.getString("email")
                                    ,"" //contrasena //oneObject.getString("token")
                            );
                            try {
                                mAsyncObjectDAO.updateData(objectToReturn);
                            }catch (Exception e){
                                LogSNE.d("NERUS", "Error at InsertDAO :" + e.getMessage());
                                return e.getMessage();
                            }
                            return "OK";
                        }else{
                            return oneObject.getString("descripcion");
                        }
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
            LogSNE.d("NERUS", "LoginUserArquosTask  IOException is: " + e.toString());
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
