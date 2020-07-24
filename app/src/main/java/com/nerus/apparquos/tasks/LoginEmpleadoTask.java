package com.nerus.apparquos.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.nerus.apparquos.daos.EmpleadoDAO;
import com.nerus.apparquos.daos.ParametroDAO;
import com.nerus.apparquos.entities.Empleado;
import com.nerus.apparquos.entities.Parametro;
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
public class LoginEmpleadoTask extends AsyncTask<String, Integer, Object> {
    public static final String TASK_NAME="LoginEmpleadoTask";
    private TasksCallBacks mCallBacks;
    private Empleado mObjectToReturn;

    private String mUrlApi;
    private String mTokenApp;
    private String mTokenUser;
    private EmpleadoDAO mAsyncObjectDAO;
    private ParametroDAO mParametroDAO;

    public LoginEmpleadoTask(String urlApi, String tokenApp, String tokenUser, EmpleadoDAO objectDAO, ParametroDAO parametroDAO) {
        super();
        mUrlApi=urlApi;
        mTokenApp=tokenApp;
        mTokenUser=tokenUser;
        mAsyncObjectDAO = objectDAO;
        mParametroDAO = parametroDAO;
    }

    public LoginEmpleadoTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(String... values) {
        String username = values[0];
        String contrasena = values[1];
        String handheld   = values[2];

        String url = mUrlApi + "/login_empleado/";
        HttpURLConnection urlConnection = null;
        try {


            HashMap<String, String> params = new HashMap<>();
            params.put("username",username);
            params.put("password",contrasena);
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
            //LogSNE.d("NERUS", "The response  is: " + response);
            try {

                JSONObject jsonResult = new JSONObject(response);
                if (jsonResult.has("data") == true) {
                    try {
                        //Empleado hintContent = new Empleado(-1, "-- SIN ESPECIFICAR --","","",0,0 );
                        //mAsyncObjectDAO.insert(hintContent);

                            this.publishProgress(1, 1);
                            JSONObject oneObject = jsonResult.getJSONObject("data");

                            // Pulling items from the array
                            Empleado content = new Empleado(oneObject.getInt("id_personal")
                                    ,oneObject.getString("descripcion")
                                    ,oneObject.getString("email")
                                    ,oneObject.getString("token")
                                    ,oneObject.getInt("abc_lecturas")
                                    ,oneObject.getInt("abc_ordenes")
                            );

                            try {
                                mAsyncObjectDAO.deleteAll();
                                mAsyncObjectDAO.insert(content);
                                mObjectToReturn = content;
                                List<Parametro> list = new ArrayList<Parametro>();
                                //mParametroDAO.insert(parametro);
                                /*

                                Parametro parametro = new Parametro("CURRENT_IDPERSONAL",content.getIdPersonal().toString(),content.getNombre(),"UN ID_PERSONAL DE LA TABLA CAT_PERSONAL, AUTORIZADO", clsFecha.getFechaYMDhms());
                                list.add(parametro);
                                parametro = new Parametro("TOKEN_USR",content.getToken(),content.getNombre(),"TOKEN USR, AUTORIZADO", clsFecha.getFechaYMDhms());
                                list.add(parametro);
                                mParametroDAO.insertAll(list);
                                LogSNE.d("NERUS", "parametro:" + parametro.toString());
                                */
                            }catch (Exception e){
                                LogSNE.d("NERUS", "Error at InsertDAO :" + e.getMessage());
                                return e.getMessage();
                            }

                            //LogSNE.d("NERUS", "Object :" +   content.toString());


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
