package itesm.mx.listacontactos;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactDetails extends AppCompatActivity implements View.OnClickListener {

    List<Contacto> listaContactos;
    ImageView ivImage;
    TextView tvNombre;
    TextView tvNumero;
    String nombre;
    Button marcar;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    Intent callIntent;
    byte[] image;
    Button mensaje;
    int[] iconIds;
    RadioButton comida;
    RadioButton hospital;
    RadioButton triste;
    RadioButton policia;
    View myView;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        tvNombre = (TextView) findViewById(R.id.textContactName);
        tvNumero = (TextView) findViewById(R.id.textPhonenumber);
        ivImage = (ImageView) findViewById(R.id.imageContactPhoto);
        marcar = (Button) findViewById(R.id.buttonCall);
        mensaje = (Button) findViewById(R.id.buttonMessage);


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
                if (contactoTemp.getNombre().equals(nombre)){
                    tvNumero.setText(contactoTemp.getNumero());
                    image = contactoTemp.getImagen();
                }

            }
        }

        if (image != null){
            Bitmap bmimage = BitmapFactory.decodeByteArray(image,0,image.length);
            ivImage.setImageBitmap(bmimage);
        }

        iconIds = new int[]{R.drawable.comida, R.drawable.emergencia, R.drawable.hospital, R.drawable.medico, R.drawable.triste};

        marcar.setOnClickListener(this);
        mensaje.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){

        switch (v.getId()) {
            case (R.id.buttonCall):
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
                break;
            case (R.id.buttonMessage):
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Elige un mensaje");

                /*
                ImageView ivIcon = new ImageView(ContactDetails.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ivIcon.setLayoutParams(lp);

                for (int iCont = 0; iCont < iconIds.length; iCont++){

                    Bitmap bmIcon = BitmapFactory.decodeResource(getResources(),iconIds[iCont]);
                    ivIcon.setImageBitmap(bmIcon);
                    builder.setView(ivIcon);
                }*/
                LayoutInflater inflater = getLayoutInflater();
                myView = inflater.inflate(R.layout.message_alert_dialog,null);
                builder.setView(myView);

                builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog


                    }
                });

                // Setting Negative "NO" Button
                builder.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                builder.show();
                break;
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        comida = (RadioButton)myView.findViewById(R.id.radioButton_comida);
        hospital = (RadioButton)myView.findViewById(R.id.radioButton_hospital);
        policia = (RadioButton)myView.findViewById(R.id.radioButton_policia);
        triste = (RadioButton)myView.findViewById(R.id.radioButton_tristeza);

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton_comida:
                if (checked)
                    policia.setChecked(false);
                    hospital.setChecked(false);
                    triste.setChecked(false);
                    message = getResources().getString(R.string.parentezco) + " Necesito comida, por favor!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButton_hospital:
                if (checked)
                    policia.setChecked(false);
                    triste.setChecked(false);
                    comida.setChecked(false);
                    message = getResources().getString(R.string.parentezco) + " Me hacen falta medicinas!";
                     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButton_policia:
                if (checked)
                    hospital.setChecked(false);
                    triste.setChecked(false);
                    comida.setChecked(false);
                    message = getResources().getString(R.string.parentezco) + " Ocupo a la policia! Llamenla!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButton_tristeza:
                if (checked)
                    policia.setChecked(false);
                    hospital.setChecked(false);
                    comida.setChecked(false);
                    message = getResources().getString(R.string.parentezco) + " Me siento triste!";
                     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
