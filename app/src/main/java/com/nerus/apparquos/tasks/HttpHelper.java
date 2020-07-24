package com.nerus.apparquos.tasks;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;
/**
 * Created by AGarciaM on 08/06/17.
 */

public  final  class HttpHelper {
    public static String buildPostParameters(Object content) {
        String output = null;
        if ((content instanceof String) ||
                (content instanceof JSONObject) ||
                (content instanceof JSONArray)) {
            output = content.toString();
        } else if (content instanceof Map) {
            Uri.Builder builder = new Uri.Builder();
            HashMap hashMap = (HashMap) content;
            if (hashMap != null) {
                Iterator entries = hashMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                    entries.remove(); // avoids a ConcurrentModificationException
                }
                output = builder.build().getEncodedQuery();
            }
        }

        return output;
    }
    public static URLConnection makeRequest(String method, String apiAddress, String appToken, String accessToken, String mimeType, String requestBody) throws IOException {
        //LogSNE.d("NERUS", "The requestBody  is: " + requestBody);
        LogSNE.d("NERUS", "The apiAddress  is: " + apiAddress);

        URL url = new URL(apiAddress);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(!method.equals("GET"));
        urlConnection.setRequestMethod(method);
        urlConnection.setReadTimeout(15000);
        urlConnection.setConnectTimeout(15000);

        urlConnection.setRequestProperty("Authorization", "Yenethiwekhi: " + accessToken);
        urlConnection.setRequestProperty("AppToken", "Zokusebenza: " + appToken);

        urlConnection.setRequestProperty("Content-Type", mimeType);
        if (!method.equals("GET")){
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();
        }

        urlConnection.connect();

        return urlConnection;
    }

}
