package com.ea2soa.skyphototips;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class LoginActivity extends Activity {

    private EditText inputUserPassSent;

    Boolean loginSuccesful;

    private static final String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUserPassSent=(EditText)findViewById(R.id.inputUserPassSent);

        //se crea un objeto Bundle para poder recibir los parametros enviados por la activity Inicio
        //al momeento de ejecutar startActivity
        Intent loginIntent=getIntent();
        Bundle extras=loginIntent.getExtras();
        String textUserPassSent=extras.getString("user");
        textUserPassSent+=extras.getString("pass");
        inputUserPassSent.setText(textUserPassSent);

        //LLAMAR A API PARA LOGUEO



        this.loginSuccesful = true;
    }

    @Override
    protected void onPause() {
        Log.i("Ejecuto","Ejecuto onPause");
        super.onPause();

        if(this.loginSuccesful){
            Intent goBackIntent;
            goBackIntent=new Intent(LoginActivity.this,MainActivity.class);
            //intent.putExtra("user",inputTextUser.getText().toString());
            //intent.putExtra("pass",inputTextPass.getText().toString());
            startActivity(goBackIntent);
        }
    }
}
