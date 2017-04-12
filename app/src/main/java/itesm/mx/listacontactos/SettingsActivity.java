package itesm.mx.listacontactos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    final int REQUEST_AGREGAR = 0;
    final int REQUEST_MODIFICAR = 1;
    final int REQUEST_BORRAR = 2;
    final int REQUEST_CONTRASENA = 3;
    Button btnAgregarContacto;
    Button btnModificarContacto;
    Button btnBorrarContacto;
    Button btnPassword;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        intent = new Intent();

        btnAgregarContacto = (Button) findViewById(R.id.buttonAdd);
        btnModificarContacto =  (Button) findViewById(R.id.buttonEdit);
        btnBorrarContacto =  (Button) findViewById(R.id.buttonDelete);
        btnPassword = (Button) findViewById(R.id.buttonPassword);

        btnAgregarContacto.setOnClickListener(this);
        btnBorrarContacto.setOnClickListener(this);
        btnModificarContacto.setOnClickListener(this);
        btnPassword.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_AGREGAR && resultCode == RESULT_OK) {
            intent.putExtra("agregado", true);
        }
        else if (requestCode == REQUEST_MODIFICAR && resultCode == RESULT_OK){
            intent.putExtra("modificado",true);
        }
        else if (requestCode == REQUEST_BORRAR && resultCode == RESULT_OK){
            intent.putExtra("eliminado",true);
        }
        else if (requestCode == REQUEST_CONTRASENA && resultCode == RESULT_OK){
            intent.putExtra("cambioContrasena", true);
        }

    }

    @Override
    public void onClick(View v){

        if (v.getId() == R.id.buttonAdd) {
            Intent intent = new Intent(this, AgregarActivity.class);
            startActivityForResult(intent, REQUEST_AGREGAR);
        }
        else if(v.getId() == R.id.buttonDelete){
            Intent intent = new Intent(this, EliminarActivity.class);
            startActivityForResult(intent, REQUEST_BORRAR);
        }
        else if (v.getId() == R.id.buttonEdit){
            Intent intent = new Intent(this, EditarActivity.class);
            startActivityForResult(intent,REQUEST_MODIFICAR);
        }
        else if (v.getId() == R.id.buttonPassword){
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivityForResult(intent, REQUEST_CONTRASENA);
        }

    }
}
