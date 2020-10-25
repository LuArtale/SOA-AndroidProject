package com.ea2soa.skyphototips;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText inputTextUser;
    private EditText inputTextPass;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Ejecuto","Ejecuto onCreate");

        inputTextUser=(EditText)findViewById(R.id.inputTextUser);
        inputTextPass=(EditText)findViewById(R.id.inputTextPass);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(botonesListeners);
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




    //Metodo que actua como Listener de los eventos que ocurren en los componentes graficos de la activty
    private View.OnClickListener botonesListeners = new View.OnClickListener()
    {

        public void onClick(View v)
        {
            Intent intent;

            //Se determina que componente genero un evento
            switch (v.getId())
            {
                //Si se ocurrio un evento en el boton OK
                case R.id.buttonLogin:
                    //se genera un Intent para poder lanzar la activity principal
                    /*intent=new Intent(MainActivity.this,DialogActivity.class);

                    //Se le agrega al intent los parametros que se le quieren pasar a la activyt principal
                    //cuando se lanzado
                    intent.putExtra("textoOrigen",inputTextUser.getText().toString());

                    //se inicia la activity principal
                    startActivity(intent);*/

                    Toast.makeText(getApplicationContext(),"Te has logueado! Ponele...",Toast.LENGTH_LONG).show();

                    break;
                default:
                    Toast.makeText(getApplicationContext(),"Error en Listener de botones",Toast.LENGTH_LONG).show();
            }


        }
    };


}
