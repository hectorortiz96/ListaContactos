package itesm.mx.listacontactos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactDetails extends AppCompatActivity implements View.OnClickListener {

    List<Contacto> listaContactos;
    TextView tvNombre;
    TextView tvNumero;
    String nombre;
    Button marcar;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    Intent callIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        tvNombre = (TextView) findViewById(R.id.textContactName);
        tvNumero = (TextView) findViewById(R.id.textPhonenumber);
        marcar = (Button) findViewById(R.id.buttonCall);


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

        marcar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){

        Log.d("Llamada onClick", "Se llamo a este metodo");

        try {
            //SE PUEDEN HACER LLAMADAS EN ESTE DISPOSITIVO

            callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + tvNumero.getText().toString()));
            int result = ContextCompat.checkSelfPermission(ContactDetails.this, Manifest.permission.CALL_PHONE);


            // Here, thisActivity is the current activity
            if (result == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permiso llamar", "YA SE TIENE PERMISO DE LLAMAR");
                startActivity(callIntent);
            } else {
                // No explanation needed, we can request the permission.
                Log.d("Permiso llamar", "SE SOLICITA PERMISO");
                ActivityCompat.requestPermissions(ContactDetails.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } catch (Exception e){
            //NO SE PUEDEN HACER LLAMADAS EN ESTE DISPOSITIVO
            Log.d("Permiso llamar","NO SE PUEDEN HACER LLAMADAS EN ESTE DISPOSITIVO");
            Toast.makeText(getApplicationContext(), "No se pueden hacer llamadas en este dispositivo!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d("Permiso llamar", "SI NOS DIO PERMISO DE LLAMAR, YA DEBERIA PODER");
                    startActivity(callIntent);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    //Log.d("Permiso llamar", "NO NOS DIO PERMISO DE LLAMAR");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
