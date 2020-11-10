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


    public TimeAndWeatherManager(SharedPreferences sharedPref, MainActivity activity) {
        this.sharedPref = sharedPref;
        this.activity = activity;
    }



    /*public void saveWeather(String weather) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("saved_last_weather", weather);
        editor.apply();
    }*/

    public String readWeather() {
        return sharedPref.getString("saved_last_weather", "Sin Datos");
    }

    @Override
    protected String doInBackground(Void... voids) {

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
        activity.showCondition(message);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        activity.showLocation("America/Argentina/Buenos_Aires");
    }


    /*public void analizeWeather() {

        Log.i("LOG_MAIN","Analizando clima");

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getString(R.string.openweather_service))
                .build();

        ServiceOpenWeather openWeatherService = retrofit.create(ServiceOpenWeather.class);

        Call<ResponseGetWeather> call = openWeatherService.getWeatherForecast(
                getString(R.string.openweather_lat),
                getString(R.string.openweather_lon),
                getString(R.string.openweather_exclude),
                getString(R.string.openweather_units),
                getString(R.string.openweather_appid));
        call.enqueue(new Callback<ResponseGetWeather>() {
            @Override
            public void onResponse(Call<ResponseGetWeather> call, Response<ResponseGetWeather> response) {
                if(response.isSuccessful()){

                    editTextTimezone.setText(response.body().getTimezone());
                    DailyWeather dailyWeatherToday = response.body().getDaily()[0];

                    Log.i("LOG_MAIN","Weather - dailyWeatherToday: " + dailyWeatherToday.toString());

                    if(dailyWeatherToday.getWeather()[0].getId() == 800) {
                        saveWeather("Despejado");
                    }
                    else {
                        if(dailyWeatherToday.getWeather()[0].getId() == 801 || dailyWeatherToday.getClouds() < 20) {
                            saveWeather("Pocas Nubes");
                        }
                        else {
                            saveWeather("Muchas nubes y posible lluvia");
                        }
                    }
                }
                else {
                    saveWeather("Error");
                    Log.e("LOG_MAIN",response.errorBody().toString());
                    Log.e("LOG_MAIN","Error de datos en analizeWeather()");
                }

                Log.i("LOG_MAIN","Fin analisis clima");
            }

            @Override
            public void onFailure(Call<ResponseGetWeather> call, Throwable t) {
                Log.e("LOG_LOGIN",t.getMessage());
                Toast.makeText(getApplicationContext(),"Error al analizar el clima",Toast.LENGTH_LONG).show();
                Log.e("LOG_LOGIN","Error al analizar el clima");
            }
        });
    }*/
}
