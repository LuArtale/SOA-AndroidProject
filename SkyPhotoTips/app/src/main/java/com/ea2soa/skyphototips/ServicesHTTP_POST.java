package com.ea2soa.skyphototips;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.view.ViewDebug;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServicesHTTP_POST extends IntentService {

    private Exception mException = null;

    private HttpURLConnection httpCon;

    private URL mUrl;


    public ServicesHTTP_POST() {
        super("ServicesHttp_GET");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            String uri = intent.getExtras().getString("uri");
            JSONObject datosJSON = new JSONObject(intent.getExtras().getString("datosJSON"));

            executePost(uri,datosJSON);

        } catch (Exception e){
            Log.e("LOG_SERVICE","Error: " + e.toString());
        }
    }



    protected void executePost(String uri, JSONObject datosJSON) {

        String result = POST(uri, datosJSON);
        if(result == null) {
            Log.e("LOG_SERVICE","Error en GET: \n" + mException.toString());
            return;
        }
        if(result == "NO_OK") {
            Log.e("LOG_SERVICE","Error en GET: NO_OK");
            return;
        }

        Intent intentRegister = new Intent("com.ea2soa.intentservice.intent.action.RESPUESTA_OPERACION");
        intentRegister.putExtra("datosJSON", result);
        sendBroadcast(intentRegister);
    }



    private String POST (String uri, JSONObject datosJSON){
        HttpURLConnection urlConnection = null;
        String result = "";

        try {

            URL mUrl = new URL(uri);

            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(datosJSON.toString().getBytes("UTF-8"));
            Log.i("LOG_SERVICE", "Se envia al servidor: "+datosJSON.toString());
            wr.flush();

            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if((responseCode == HttpURLConnection.HTTP_OK) || (responseCode == HttpURLConnection.HTTP_CREATED)) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                //result = convertInputStreamToString(inputStream).toString();
                result = inputStream.toString();
            } else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getErrorStream());
                result = inputStream.toString();
            } else {
                result = "NO_OK";
            }

            mException = null;
            wr.close();
            urlConnection.disconnect();
            return result;

        } catch (Exception e) {
            mException = e;
            return null;
        }
    }
}
