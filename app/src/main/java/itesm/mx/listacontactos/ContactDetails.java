package itesm.mx.listacontactos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class ContactDetails extends AppCompatActivity {

    List<Contacto> listaContactos;
    TextView tvNombre;
    TextView tvNumero;
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        tvNombre = (TextView) findViewById(R.id.textContactName);
        tvNumero = (TextView) findViewById(R.id.textPhonenumber);


        VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
        listaContactos = globalListaContactos.getListaContactos();

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            nombre = intent.getStringExtra("Nombre");
            tvNombre.setText(nombre);
        }

        if (listaContactos != null && !nombre.isEmpty()){
            for (int iCont = 0; iCont < listaContactos.size(); iCont++){
                Contacto contactoTemp = listaContactos.get(iCont);
                if (contactoTemp.getNombre().equals(nombre))
                    tvNumero.setText(contactoTemp.getNumero());

            }
        }
    }
}
