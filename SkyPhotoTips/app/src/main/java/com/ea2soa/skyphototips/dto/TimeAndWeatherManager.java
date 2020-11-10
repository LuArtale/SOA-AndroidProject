package com.ea2soa.skyphototips.dto;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.ea2soa.skyphototips.MainActivity;

import java.util.Calendar;

public class TimeAndWeatherManager extends AsyncTask<Void, Void, String> {

    SharedPreferences sharedPref;
    MainActivity activity;
    private Long currentDateEpoch;
    private Long savedTimeWeather;
    private Boolean alive;


    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }



    public TimeAndWeatherManager(SharedPreferences sharedPref, MainActivity activity, Boolean alive) {
        this.sharedPref = sharedPref;
        this.activity = activity;
        this.alive = alive;
    }




    public String readWeather() {
        return sharedPref.getString("saved_last_weather", "Sin Datos");
    }

    @Override
    protected String doInBackground(Void... voids) {

        alive = true;
        String weatherAnalisis = "Error";

        try {
            currentDateEpoch = Calendar.getInstance().getTimeInMillis() / 1000;
            Log.i("LOG_TM_MANAGER:", "currentDate epoch: " + currentDateEpoch);

            savedTimeWeather = sharedPref.getLong("saved_time_weather", 1604188800);

            Log.i("LOG_TM_MANAGER:", "Datos guardados antes: " + savedTimeWeather);

            if((savedTimeWeather + 3600) < currentDateEpoch || readWeather().equals("Error")) {

                activity.analizeWeather();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong("saved_time_weather", currentDateEpoch);
                editor.apply();

                Log.i("LOG_TM_MANAGER:", "Se actualizo el timestamp");
            }
            else {
                Log.i("LOG_TM_MANAGER:", "Timestamp se mantiene");
                publishProgress();
            }

            weatherAnalisis = "Ok";

            Log.i("LOG_TM_MANAGER:", "Datos guardados despues: " + savedTimeWeather);

        } catch (Exception e)
        {
            Log.e("LOG_TM_MANAGER", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        return weatherAnalisis;
    }

    @Override
    protected void onPostExecute(String message) {
        alive = false;
        activity.showCondition(message);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        activity.showLocation("America/Argentina/Buenos_Aires");
    }


}
