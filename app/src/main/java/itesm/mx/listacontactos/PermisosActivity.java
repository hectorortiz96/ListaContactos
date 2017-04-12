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

public class PermisosActivity extends AppCompatActivity implements  View.OnClickListener {

    String contrasena;
    String confContrasena;
    final int REQUEST_ACCEDER = 0;
    Button btnAceptar;
    EditText etContrasena;
    EditText etConfContraseña;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos);

        btnAceptar = (Button)findViewById(R.id.button_aceptar);
        etContrasena = (EditText) findViewById(R.id.password_contrasena);

        //contrasena = etContrasena.getText().toString();
        //confContrasena = etConfContraseña.getText().toString();


        btnAceptar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        String passwordValor = sharedPref.getString("password",null);
        contrasena = etContrasena.getText().toString();
        Log.d("Password vale:",passwordValor);
        Log.d("contraseña vale:", contrasena);
        if (contrasena.equals(passwordValor)) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_ACCEDER);
        }else{
            Toast.makeText(getApplicationContext(), "Contraseña incorrecta!", Toast.LENGTH_SHORT).show();
            etContrasena.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        etContrasena.setText(null);
        if (data != null){
            Boolean agregado = data.getBooleanExtra("agregado",false);
            Boolean modificado = data.getBooleanExtra("modificado",false);
            Boolean eliminado = data.getBooleanExtra("eliminado",false);

            if (agregado || modificado || eliminado) {
                Intent intent = new Intent();
                intent.putExtra("actualizar",true);
            }
        }

    }
}
