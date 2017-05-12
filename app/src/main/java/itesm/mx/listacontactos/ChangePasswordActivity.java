package itesm.mx.listacontactos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity implements  View.OnClickListener {

    String contrasena;
    String confContrasena;
    final int REQUEST_ACCEDER = 0;
    Button btnAceptar;
    EditText etContrasena;
    EditText etConfContrasena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnAceptar = (Button)findViewById(R.id.button_aceptar);
        etContrasena = (EditText) findViewById(R.id.password_contrasena);
        etConfContrasena = (EditText) findViewById(R.id.password_confirmar) ;
        //contrasena = etContrasena.getText().toString();
        //confContrasena = etConfContraseña.getText().toString();


        btnAceptar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v){

        contrasena = etContrasena.getText().toString();
        confContrasena = etConfContrasena.getText().toString();

        if (confContrasena.equals(contrasena) && !contrasena.isEmpty()) {
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("password",contrasena);
            editor.commit();
            Toast.makeText(getApplicationContext(), "Contraseña modificada exitosamente!", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Los campos no coinciden!", Toast.LENGTH_SHORT).show();
            etContrasena.setText("");
            etConfContrasena.setText("");
        }
    }

}

