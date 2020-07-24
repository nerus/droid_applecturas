package com.nerus.apparquos.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.nerus.apparquos.entities.MaterialCapturado;
import com.nerus.apparquos.entities.OrdenCerrada;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MaterialFileCsvTask extends AsyncTask<MaterialCapturado, Integer, Object> {
    public static final String TASK_NAME="MaterialFileCsvTask";
    private final File mFileCsv;
    private final List<MaterialCapturado> mMaterialList;
    private final Uri mFileUri;
    private TasksCallBacks mCallBacks;

    private StringBuilder objectToReturn;


    public MaterialFileCsvTask(File csvFile, Uri fileUri, List<MaterialCapturado> list) {
        super();
        mFileCsv = csvFile;
        mMaterialList = list;
        mFileUri = fileUri;
    }

    public MaterialFileCsvTask setCallBacks(TasksCallBacks callBacks) {
        mCallBacks = callBacks;
        return this;
    }

    @Override
    protected Object doInBackground(MaterialCapturado... values) {
        objectToReturn = new StringBuilder();
        try {
            mFileCsv.createNewFile();
            FileOutputStream fos = new FileOutputStream(mFileCsv.toString(), true);

            FileWriter fw = new FileWriter(fos.getFD());
//LogSNE.d("NERUS","mFileCsv: "+mFileCsv.toString());
            fw.append('"');
            fw.append("ID_ORDEN");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("ID_MATERIAL");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("DESCRIPCION");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("CANTIDAD");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("UDM");
            fw.append('"');
            fw.append(',');

/*
            fw.append('"');
            fw.append("EJECUTADA");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("ID_CAUSA");
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
            fw.append("MOBILE");
            fw.append('"');
            fw.append(',');

            fw.append('"');
            fw.append("OBSERVACION");
            fw.append('"');
            fw.append(',');
*/


            fw.append('\n');
            objectToReturn.append("<MATERIALES>");
            this.publishProgress(1, mMaterialList.size());
            for (Integer i = 0; i < mMaterialList.size(); i++) {
                this.publishProgress(i+1, mMaterialList.size());
                MaterialCapturado  item = mMaterialList.get(i);
                fw.append('"');
                fw.append(item.getIdOrden());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getIdMaterial());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getDescripcion());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getCantidad());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getUdm());
                fw.append('"');
                fw.append(',');

/*
                fw.append('"');
                fw.append(item.getEjecutada() ? "1" : "0");
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getIdNoEjecucion().toString());
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
                fw.append(clsFecha.getFootPrint());
                fw.append('"');
                fw.append(',');

                fw.append('"');
                fw.append(item.getObservaB());
                fw.append('"');
                fw.append(',');
*/

                fw.append('\n');

                objectToReturn.append("<MATERIAL>");

                objectToReturn.append("<id_orden>");
                objectToReturn.append(item.getIdOrden());
                objectToReturn.append("</id_orden>");

                //objectToReturn.append("<id_trabajo>");
                //objectToReturn.append(item.getIdTrabajo().toString());
                //objectToReturn.append("</id_trabajo>");

                //objectToReturn.append("<id_empleado>");
                //objectToReturn.append(item.getIdEmpleado().toString());
                //objectToReturn.append("</id_empleado>");

                objectToReturn.append("<id_material>");
                objectToReturn.append(item.getIdMaterial().trim());
                objectToReturn.append("</id_material>");

                objectToReturn.append("<descripcion>");
                objectToReturn.append(item.getDescripcion());
                objectToReturn.append("</descripcion>");

                objectToReturn.append("<cantidad>");
                objectToReturn.append(item.getCantidad());
                objectToReturn.append("</cantidad>");

                objectToReturn.append("<udm>");
                objectToReturn.append(item.getUdm());
                objectToReturn.append("</udm>");

/*              objectToReturn.append("<cantidad>");
                objectToReturn.append(item.getEjecutada() ? "1" : "0");
                objectToReturn.append("</ejecutada>");

                objectToReturn.append("<latitud>");
                objectToReturn.append( item.getLatitud() );
                objectToReturn.append("</latitud>");

                objectToReturn.append("<longitud>");
                objectToReturn.append( item.getLongitud() );
                objectToReturn.append("</longitud>");

                //objectToReturn.append("<mobile>");
                //objectToReturn.append( clsFecha.getFootPrint() );
                //objectToReturn.append("</mobile>");

                objectToReturn.append("<observa_b>");
                objectToReturn.append( item.getObservaB() );
                objectToReturn.append("</observa_b>");
*/

                objectToReturn.append("</MATERIAL>");


            }
            objectToReturn.append("</MATERIALES>");
            fw.flush();
            fw.close();
            fos.close();


            return "OK";
        } catch (IOException e) {
            LogSNE.d("NERUS", "MaterialFileCsvTask IOException:"+ e.toString());
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
                    mCallBacks.onRequestSuccess(TASK_NAME, objectToReturn.toString());
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
