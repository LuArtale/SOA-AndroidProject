package com.ea2soa.skyphototips;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

    private EditText inputTextUserR;
    private EditText inputTextPassR;
    private Button buttonRegisterR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputTextUserR=(EditText)findViewById(R.id.inputTextUserR);
        inputTextPassR=(EditText)findViewById(R.id.inputTextPassR);

        buttonRegisterR = (Button) findViewById(R.id.buttonRegisterR);

        buttonRegisterR.setOnClickListener(botonesListeners);

        Intent loginIntent=getIntent();
        /*Bundle extras=loginIntent.getExtras();
        String textUserPassSent=extras.getString("user");
        textUserPassSent+=extras.getString("pass");
        inputUserPassSent.setText(textUserPassSent);*/
    }


    private View.OnClickListener botonesListeners = new View.OnClickListener()
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
    };
}
