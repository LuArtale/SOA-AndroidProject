package com.ea2soa.skyphototips;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity {

    private EditText inputTextName;
    private EditText inputTextLastname;
    private EditText inputTextDni;
    private EditText inputTextEmailR;
    private EditText inputTextPassR;
    private EditText inputTextCommission;

    private Button buttonRegisterR;

    private static final String URI_REGISTER_USER = "http://so-unlam.net.ar/api/api/register";

    //Solo para test://
    private EditText textResult;
    //---------------//

    public IntentFilter filtro;
    private ReceptorOperacion receiver = new ReceptorOperacion();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.i("LOG_REGISTER","OnCreate");

        inputTextName = (EditText)findViewById(R.id.inputTextName);
        inputTextLastname = (EditText)findViewById(R.id.inputTextLastname);
        inputTextDni = (EditText)findViewById(R.id.inputTextDni);
        inputTextEmailR = (EditText)findViewById(R.id.inputTextEmailR);
        inputTextPassR = (EditText)findViewById(R.id.inputTextPassR);
        inputTextCommission = (EditText)findViewById(R.id.inputTextCommission);

        buttonRegisterR = (Button) findViewById(R.id.buttonRegisterR);

        textResult = (EditText)findViewById(R.id.textResult);

        //buttonRegisterR.setOnClickListener(botonesListeners);
        buttonRegisterR.setOnClickListener(HandlerCmdRegistrar);

        //Intent loginIntent=getIntent();
        /*Bundle extras=loginIntent.getExtras();
        String textUserPassSent=extras.getString("user");
        textUserPassSent+=extras.getString("pass");
        inputUserPassSent.setText(textUserPassSent);*/

        configurarBroadcastReceiver();

        Log.i("LOG_REGISTER","Finished setup");
    }


    /*private View.OnClickListener botonesListeners = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent;
            switch (v.getId())
            {
                case R.id.buttonRegisterR:
                    intent=new Intent(RegisterActivity.this,MainActivity.class);
                    //intent.putExtra("user",inputTextUser.getText().toString());
                    //intent.putExtra("pass",inputTextPass.getText().toString());
                    Toast.makeText(getApplicationContext(),"Registrado Exitosamente!",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    break;

                default:
                    Toast.makeText(getApplicationContext(),"Error en Listener de botones",Toast.LENGTH_LONG).show();
            }
        }
    };*/

    private void configurarBroadcastReceiver() {
        Log.i("LOG_REGISTER","Configurando Broadcast Receiver");
        filtro = new IntentFilter("com.ea2soa.skyphototips.intent.action.RESPUESTA_OPERACION");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver,filtro);
    }


    private View.OnClickListener HandlerCmdRegistrar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject obj = new JSONObject();
            try {

                Log.i("LOG_REGISTER","Inicio Handler");

                obj.put("env","TEST");
                obj.put("name",inputTextName.getText().toString());
                obj.put("lastname",inputTextLastname.getText().toString());
                obj.put("dni",Integer.parseInt(inputTextDni.getText().toString()));
                obj.put("email",inputTextEmailR.getText().toString());
                obj.put("password",inputTextPassR.getText().toString());
                obj.put("commission",Integer.parseInt(inputTextCommission.getText().toString()));

                Intent intentRegister=new Intent(RegisterActivity.this,ServicesHTTP_POST.class);

                intentRegister.putExtra("uri",URI_REGISTER_USER);
                intentRegister.putExtra("datosJSON",obj.toString());

                //Toast.makeText(getApplicationContext(),"Inicio registracion!",Toast.LENGTH_LONG).show();
                Log.i("LOG_REGISTER","Inicio registracion!");

                startService(intentRegister);

            } catch (Exception e){
                Log.i("Error",e.toString());
            }
        }
    };


    public class ReceptorOperacion extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            try {

                String datosJsonString = intent.getStringExtra("datosJSON");
                JSONObject datosJSON = new JSONObject(datosJsonString);

                Log.i("LOG_REGISTER","Datos JSON Main Thread: \n" + datosJsonString);

                textResult.setText(datosJsonString);
                Toast.makeText(getApplicationContext(),"Se recibio respuesta del server",Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
