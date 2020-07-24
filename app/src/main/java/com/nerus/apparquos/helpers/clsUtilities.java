package com.nerus.apparquos.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.nerus.apparquos.R;
import com.nerus.apparquos.entities.Archivo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public final class clsUtilities {
    public static File getOutputMediaFile(String cFolder, String cSubFolder, String cPrefijo) {
        File mediaStorageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                    , cFolder + File.separator + cSubFolder);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = clsFecha.getFechaYMDhms().replace(":", "").replace(" ", "").replace("-", "");

        String cFileName = cPrefijo + timeStamp+".jpg";

        //LogSNE.d("NERUS", "timeStamp->" +timeStamp.toString() );
        return new File(mediaStorageDir.getPath() + File.separator + cFileName);
    }
    public static File getOutputCSVFile(String cFolder, String cSubFolder, String cPrefijo) {
        File mediaStorageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , cFolder + File.separator + cSubFolder);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = clsFecha.getFechaYMDhms().replace(":", "").replace(" ", "").replace("-", "");

        String cFileName = cPrefijo + timeStamp+".CSV";

        //LogSNE.d("NERUS", "timeStamp->" +timeStamp.toString() );
        return new File(mediaStorageDir.getPath() + File.separator + cFileName);
    }

    public static Uri getUriFromFile(Context theCtx, File theSrcPath) {
        Uri requiredUri;

        // Above Compile SDKversion: 25 -- Uri.fromFile Not working
        // So we have to use Provider

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requiredUri = FileProvider.getUriForFile(theCtx,
                    theCtx.getApplicationContext().getPackageName() + ".provider",
                    theSrcPath);
        } else {
            requiredUri = Uri.fromFile(theSrcPath);
        }

        return requiredUri;
    }

    public static final String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
    public static final String[] CAMERA_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    public static final String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public static final String[] INTERNET_PERMISSIONS = new String[]{Manifest.permission.INTERNET};
    public static int checkSelfPermission(final Context context, String permitRequired) {
        int result = ContextCompat.checkSelfPermission(context ,permitRequired);
        return  result;
    }

    public static List<Archivo> getFileList(String cFolder, String cSubFolder){
        List<Archivo> archivoList = new ArrayList<Archivo>();
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                , cFolder + File.separator + cSubFolder);
//, "PADRON" + File.separator + mItem.id_orden);
        File dir = new File(mediaStorageDir.getPath());
        File[] directoryListing = dir.listFiles();
        Integer nCnt = 1;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                //LogSNE.d("NERUS", "imagen: " + child.toString());
                String cFileName = child.getName();
                String[] split = cFileName.split("_");
                String cIdFile ="";
                if (split.length>1) {cIdFile=split[1];}
                Archivo item = new Archivo(nCnt, cIdFile ,cFileName, child.getPath());
                archivoList.add(item);
                nCnt += 1;
                //for (int i=0; i<split.length; i++){
                //    LogSNE.d("NERUS", "split "+String.valueOf(i)+": " + split[i]);
                //}

            }
            //filesContent.ITEM_MAP.toString();
        } else {
            LogSNE.d("NERUS", "Handle the case where dir is not really a directory.");
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
        return archivoList;
    }

    public static void setHomeBack(final FragmentActivity activity, String caption){
        Toolbar toolbar =  activity.findViewById(R.id.toolbar);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) activity).getSupportActionBar().setTitle(caption);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                //LogSNE.d("NERUS", "onClick home");
                if (activity.getSupportFragmentManager().getBackStackEntryCount() > 0){
                    boolean done = activity.getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
    }

    public static void hideKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static class LogSNE{
        public static void d(String tag, Object msg){
            Log.d("NERUS",msg.toString());
        }
    }
}
