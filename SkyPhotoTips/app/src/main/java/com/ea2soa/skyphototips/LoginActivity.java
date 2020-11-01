package com.ea2soa.skyphototips;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.ea2soa.skyphototips.dto.RequestLogin;
import com.ea2soa.skyphototips.dto.RequestRegisterUser;
import com.ea2soa.skyphototips.dto.ResponseLogin;
import com.ea2soa.skyphototips.dto.ResponseRegisterUser;
import com.ea2soa.skyphototips.services.ServiceSoa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {

    private String token;
    private String tokenRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent loginIntent=getIntent();
        Bundle extras=loginIntent.getExtras();

        //LLAMAR A API PARA LOGUEO
        Log.i("LOG_LOGIN","Starting Login");

        RequestLogin requestLogin = new RequestLogin();
        requestLogin.setEmail(extras.getString("user"));
        requestLogin.setPassword(extras.getString("pass"));

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getString(R.string.retrofit_service))
                .build();

        ServiceSoa serviceSoa = retrofit.create(ServiceSoa.class);

        Call<ResponseLogin> call = serviceSoa.respLogin(requestLogin);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                if(response.isSuccessful()){
                    token = response.body().getToken();
                    tokenRefresh = response.body().getToken_refresh();

                    Log.i("LOG_LOGIN","Token: " + token);
                    Log.i("LOG_LOGIN","Token_Refresh: " + tokenRefresh);

                    Intent continueIntent;
                    continueIntent=new Intent(LoginActivity.this,SensorsCheck.class);
                    continueIntent.putExtra("user",requestLogin.getEmail());
                    continueIntent.putExtra("pass",requestLogin.getPassword());
                    continueIntent.putExtra("tokenRefresh",tokenRefresh);
                    startActivity(continueIntent);
                }
                else {
                    Log.e("LOG_LOGIN",response.errorBody().toString());

                    Toast.makeText(getApplicationContext(),"Datos Invalidos",Toast.LENGTH_LONG).show();

                    /*Intent goBackIntent;
                    goBackIntent=new Intent(LoginActivity.this,MainActivity.class);
                    //.putExtra("user",requestLogin.getEmail());
                    //goBackIntent.putExtra("errorlogin",response.errorBody().toString());
                    startActivity(goBackIntent);*/
                    goBack();
                }

                Log.i("LOG_LOGIN","Fin Mensaje");
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.e("LOG_LOGIN",t.getMessage());
                Toast.makeText(getApplicationContext(),"Error al iniciar sesion",Toast.LENGTH_LONG).show();

                goBack();
            }
        });


    }

    public void goBack() {
        Intent goBackIntent;
        goBackIntent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(goBackIntent);
    }

    @Override
    protected void onPause() {
        Log.i("LOG_LOGIN","Ejecuto onPause");
        super.onPause();

        //goBack();
    }

    @Override
    protected void onStop() {
        Log.i("LOG_LOGIN","Ejecuto onStop");
        super.onStop();

        //goBack();
    }


}
