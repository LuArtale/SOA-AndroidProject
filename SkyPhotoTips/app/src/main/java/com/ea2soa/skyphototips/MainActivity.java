package com.ea2soa.skyphototips;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ea2soa.skyphototips.dto.DailyWeather;
import com.ea2soa.skyphototips.dto.Event;
import com.ea2soa.skyphototips.dto.ResponseGetWeather;
import com.ea2soa.skyphototips.dto.ResponseLogin;
import com.ea2soa.skyphototips.services.ServiceOpenWeather;
import com.ea2soa.skyphototips.services.ServiceSoa;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("SourceLockedOrientationActivity")
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;

    private ImageView imgFrontNavArrow;

    private float[] mGravity;
    private float[] mGeomagnetic;
    private float azimut;


    private Long currentDateEpoch;
    Long savedTimeWeather;

    private static final Double limitAcelerometerLowXY = -3.00;
    private static final Double limitAcelerometerHighXY = 3.00;
    private static final Double limitAcelerometerLowZ = 8.00;
    private static final Double limitAcelerometerHighZ = 10.00;

    private Boolean correctPosition;
    private ImageView imgBadPosition;
    private ImageView imgOkPosition;

    private TextView editText;
    private TextView editTextTimezone;
    private TextView editTextCondicion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("LOG_MAIN:", "Ejecuto onCreate");

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        imgFrontNavArrow = (ImageView) findViewById(R.id.imgFrontNavArrow);
        imgFrontNavArrow.setVisibility(View.VISIBLE);

        correctPosition = false;
        imgBadPosition = (ImageView) findViewById(R.id.imgBadPosition);
        imgOkPosition = (ImageView) findViewById(R.id.imgOkPosition);
        imgOkPosition.setVisibility(View.INVISIBLE);

        editText = (TextView) findViewById(R.id.editText);
        editTextTimezone = (TextView) findViewById((R.id.editTextTimezone));
        editTextCondicion = (TextView) findViewById((R.id.editTextCondicion));


        checkTimeAndWeather();

    }

    public void saveWeather(String weather) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_last_weather), weather);
        editor.apply();
    }

    public String readWeather() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.saved_last_weather), "Sin Datos");
    }

    public void checkTimeAndWeather() {
        //String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (epoch*1000));
        currentDateEpoch = Calendar.getInstance().getTimeInMillis() / 1000; //Calculo la fecha actual en segundos
        Log.i("LOG_MAIN:", "currentDate epoch: " + currentDateEpoch);

        String weatherAnalisis;

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        savedTimeWeather = sharedPref.getLong(getString(R.string.saved_time_weather), 1604188800);

        Log.i("LOG_MAIN:", "Datos guardados antes: " + savedTimeWeather);

        if((savedTimeWeather + 3600) < currentDateEpoch || readWeather().equals("Error")) {

            analizeWeather();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(getString(R.string.saved_time_weather), currentDateEpoch);
            editor.apply();

            Log.i("LOG_MAIN:", "Se actualizo el timestamp");
        }
        else {
            Log.i("LOG_MAIN:", "Timestamp se mantiene");
            editTextTimezone.setText("America/Argentina/Buenos_Aires");
        }

        weatherAnalisis = readWeather();

        editTextCondicion.setText(weatherAnalisis);

        if(!weatherAnalisis.equals("Despejado") && !weatherAnalisis.equals("Pocas Nubes")) {
            informBadWeather();
        }

        Log.i("LOG_MAIN:", "Datos guardados despues: " + savedTimeWeather);
    }



    public void informBadWeather() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta de Clima");
        builder.setMessage("Debido a las condiciones climaticas no te recomendamos sacar fotos hoy :(");
        //builder.setMessage("Datos guardados: " + savedTimeWeather + "\n Datos actuales: " + currentDateEpoch);
        builder.setPositiveButton("Aceptar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void analizeWeather() {

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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        synchronized (this)
        {
            switch(event.sensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER :

                    mGravity = event.values;

                    if((event.values[0] > limitAcelerometerLowXY) && (event.values[0] < limitAcelerometerHighXY) &&
                            (event.values[1] > limitAcelerometerLowXY) && (event.values[1] < limitAcelerometerHighXY) &&
                            (event.values[2] > limitAcelerometerLowZ) && (event.values[2] < limitAcelerometerHighZ)) {

                        correctPosition = true;
                        imgBadPosition.setVisibility(View.INVISIBLE);
                        imgOkPosition.setVisibility(View.VISIBLE);
                    }
                    else {
                        correctPosition = false;
                        imgBadPosition.setVisibility(View.VISIBLE);
                        imgOkPosition.setVisibility(View.INVISIBLE);
                    }

                    if (mGravity != null && mGeomagnetic != null && correctPosition == true) {
                        float R[] = new float[9];
                        float I[] = new float[9];

                        if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {

                            // orientation contains azimut, pitch and roll
                            float orientation[] = new float[3];
                            SensorManager.getOrientation(R, orientation);

                            azimut = orientation[0];
                            editText.setText(String.valueOf(azimut));

                            imgFrontNavArrow.setRotation((float) (-azimut*270/3.14159));
                        }
                    }
                    break;


                case Sensor.TYPE_MAGNETIC_FIELD :

                    mGeomagnetic = event.values;

                    if (mGravity != null && mGeomagnetic != null && correctPosition == true) {
                        float R[] = new float[9];
                        float I[] = new float[9];

                        if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {

                            // orientation contains azimut, pitch and roll
                            float orientation[] = new float[3];
                            SensorManager.getOrientation(R, orientation);

                            azimut = orientation[0];
                            editText.setText(String.valueOf(azimut));

                            imgFrontNavArrow.setRotation((float) (-azimut*270/3.14159));

                        }
                    }
                    break;
            }
        }

    }

    protected void Ini_Sensores()
    {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void Parar_Sensores()
    {
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
    }


    @Override
    protected void onStart() {
        Log.i("LOG_MAIN:", "Ejecuto OnStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("LOG_MAIN:", "Ejecuto OnResume");
        super.onResume();
        Ini_Sensores();



    }

    @Override
    protected void onPause() {
        Log.i("LOG_MAIN:", "Ejecuto OnPause");
        Parar_Sensores();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("LOG_MAIN:", "Ejecuto OnStop");
        Parar_Sensores();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("LOG_MAIN:", "Ejecuto OnDestroy");
        Parar_Sensores();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.i("LOG_MAIN:", "Ejecuto OnRestart");
        Ini_Sensores();
        super.onRestart();
    }


}
