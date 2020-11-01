package com.ea2soa.skyphototips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends Activity {

    private EditText inputTextUser;
    private EditText inputTextPass;
    private Button buttonLogin;
    private Button buttonRegister;

    private Boolean internetConection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Ejecuto","Ejecuto onCreate");

        inputTextUser=(EditText)findViewById(R.id.inputTextUser);
        inputTextPass=(EditText)findViewById(R.id.inputTextPass);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(botonesListeners);
        buttonRegister.setOnClickListener(botonesListeners);
    }

    @Override
    protected void onStart() {
        Log.i("Ejecuto","Ejecuto OnStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("Ejecuto","Ejecuto OnResume");
        super.onResume();

        if(!conexionAInternet())
            Toast.makeText(getApplicationContext(),"Se necesita conexion a internet...",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        Log.i("Ejecuto","Ejecuto OnPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("Ejecuto","Ejecuto OnStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("Ejecuto","Ejecuto OnDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.i("Ejecuto","Ejecuto OnRestart");
        super.onRestart();
    }


    private View.OnClickListener botonesListeners = new View.OnClickListener()
    {

        public void onClick(View v)
        {
            Intent intent;
            switch (v.getId())
            {
                case R.id.buttonLogin:

                    if(conexionAInternet()) {
                        intent=new Intent(MainActivity.this,LoginActivity.class);
                        intent.putExtra("user",inputTextUser.getText().toString());
                        intent.putExtra("pass",inputTextPass.getText().toString());

                        startActivity(intent);

                        Toast.makeText(getApplicationContext(),"Bienvenido!",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Se necesita conexion a internet...",Toast.LENGTH_LONG).show();

                    break;

                case R.id.buttonRegister:
                    if(conexionAInternet()) {
                        intent=new Intent(MainActivity.this,RegisterActivity.class);
                        //intent.putExtra("user",inputTextUser.getText().toString());
                        //intent.putExtra("pass",inputTextPass.getText().toString());

                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Se necesita conexion a internet...",Toast.LENGTH_LONG).show();

                    break;

                default:
                    Toast.makeText(getApplicationContext(),"Error en Listener de botones",Toast.LENGTH_LONG).show();
            }


        }
    };

    private boolean conexionAInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else {
            return false;
        }
    }



}
