package com.nerus.apparquos.helpers;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.security.auth.callback.CallbackHandler;

public final class clsFecha {
    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date resultdate = new Date(System.currentTimeMillis());
        return sdf.format(resultdate);
    }
    public static String getFechaYMD() {
        String cFecha = "";
        //cFecha = clsUtilities.getCurrentDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date resultdate = new Date(System.currentTimeMillis());
        cFecha = sdf.format(resultdate);
        cFecha = cFecha.replace("-","");
        cFecha = cFecha.substring(0,8);
        return cFecha;
    }
    public static String getFechaYMDhms() {
        String cFecha = "";
        //cFecha = clsUtilities.getCurrentDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date resultdate = new Date(System.currentTimeMillis());
        cFecha = sdf.format(resultdate);
        cFecha = cFecha.replace("-","");
        cFecha = cFecha.substring(0,17);
        return cFecha;
    }
    public static String getTimeHMS() {
        String cFecha = "";
        //cFecha = clsUtilities.getCurrentDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date resultdate = new Date(System.currentTimeMillis());
        cFecha = sdf.format(resultdate);
        cFecha = cFecha.replace("-","");
        cFecha = cFecha.substring(9,17);
        return cFecha;
    }
    public static String fechaToDtos(String cFecha){
        String cReturn ="";
        if (fechaValida(cFecha)){
            String dd = cFecha.substring(0,2);
            String mm = cFecha.substring(3,5);
            String yy = cFecha.substring(6,10);
            cReturn = yy+mm+dd;
        }
        return  cReturn;
    }
    public static boolean fechaValida(String cFecha){

        int[] diasMes= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean lReturn = true;
        if (cFecha.length()<10){
            lReturn=false;
        }else{
            //LogSNE.d("NERUS", "FECHA:"+cFecha);
            String dd = cFecha.substring(0,2);
            String mm = cFecha.substring(3,5);
            String yy = cFecha.substring(6,10);
            //LogSNE.d("NERUS", "DIA:"+dd);
            //LogSNE.d("NERUS", "MES:"+mm);
            //LogSNE.d("NERUS", "AÑO:"+yy);

            if (Integer.parseInt(dd)>0 && Integer.parseInt(dd)<32){
                if (Integer.parseInt(mm)>0 && Integer.parseInt(mm)<13){
                    if (Integer.parseInt(yy)>1900 && Integer.parseInt(yy)<9999){
                        if (Integer.parseInt(mm)==2 && Integer.parseInt(dd)==29) {
                            lReturn = Integer.parseInt(yy) % 400 == 0 ||
                                    (Integer.parseInt(yy) % 4 == 0 && Integer.parseInt(yy) % 100 != 0);
                        }else{
                            lReturn =  Integer.parseInt(dd)>=1 && Integer.parseInt(dd)<=diasMes[Integer.parseInt(mm)-1];
                        }
                    }else lReturn=false;
                }else lReturn=false;
            }else lReturn=false;

        }

        return  lReturn;

    }
    public static String getSHA1(String password){
        String validar ="";
        if (password.length()>0){
            byte[] data1 = new byte[0];
            try {
                data1 = password.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byte[] digest = messageDigest.digest(data1);
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<digest.length; i++){
                int b=digest[i] & 0xFF;
                if (b < 0x10) {
                    hexString.append('0');
                }
                hexString.append(Integer.toHexString(b));

            }

            validar = hexString.toString().toUpperCase();
        }
        return  validar;
    }
    public static String getFootPrint() {
        String fingerPrint = clsFecha.capitalize(android.os.Build.BRAND)
                +"["+android.os.Build.MODEL+"]"
                +"[CPU-"+clsFecha.capitalize(android.os.Build.CPU_ABI)+"]"
                +"[API-"+android.os.Build.VERSION.SDK_INT+"]"
                +"[R-"+android.os.Build.VERSION.RELEASE+"]";

        return  fingerPrint;
    }
    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    public static String getSalute(String subjet){
        String cAhorita = getTimeHMS();
        Integer nHra = Integer.valueOf(cAhorita.substring(0,2));
        String cSaludo = "Hola! "+subjet+", que haces despierto?";
        if (nHra>=5 && nHra<12){
            cSaludo = "Buenos días, " + subjet;
        }else if (nHra>=12 && nHra<19) {
            cSaludo = "Buenas tardes, " + subjet;
        }else if (nHra>=19 && nHra<23) {
            cSaludo = "Buenas noches, " + subjet;
        }
        return cSaludo.toUpperCase();
    }
}
