package itesm.mx.listacontactos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditarContactoActivity extends AppCompatActivity implements View.OnClickListener  {

    Contacto contactoElegido;
    int posicion;
    EditText etNombre;
    EditText etNumero;
    Spinner spCategoria;
    Button btnGuardar;
    ImageView ibFoto;

    String nombre;
    String numero;
    String[] stringCategorias;
    int categoria;
    byte[] foto;
    final int REQUEST_TAKE_PHOTO = 0;
    final int REQUEST_PICK_PHOTO = 1;
    List<Contacto> listaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto);

        contactoElegido = (Contacto) getIntent().getSerializableExtra("Contacto");
        posicion = getIntent().getIntExtra("posicion",-1);

        etNombre = (EditText) findViewById(R.id.editText_nombre_contacto);
        etNumero= (EditText) findViewById(R.id.editText_numero_contacto);
        spCategoria = (Spinner) findViewById(R.id.spinner_categoria);
        stringCategorias = getResources().getStringArray(R.array.array_categorias);
        btnGuardar = (Button) findViewById(R.id.button_agregar_contacto);
        ibFoto = (ImageView) findViewById(R.id.imageButton);

        etNombre.setText(contactoElegido.getNombre());
        etNumero.setText(contactoElegido.getNumero());

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringCategorias); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(spinnerArrayAdapter);

        AdapterView.OnItemSelectedListener MyListenerSpinner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)

                String seleccion = (String)parent.getItemAtPosition(pos);
                if (seleccion.equals("Familiares")){
                    categoria = 0;
                }
                else if (seleccion.equals("Amigos")){
                    categoria = 1;
                }
                else if (seleccion.equals("Servicios de Salud")) {
                    categoria = 2;
                }
                else if (seleccion.equals("Otros")) {
                    categoria = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        };

        nombre = contactoElegido.getNombre();
        numero = contactoElegido.getNumero();
        categoria = contactoElegido.getTipo();
        foto = contactoElegido.getImagen();
        if (foto != null){
            Bitmap bmimage = BitmapFactory.decodeByteArray(foto,0,foto.length);
            ibFoto.setBackground(null);
            ibFoto.setImageBitmap(bmimage);
        }

        spCategoria.setSelection(categoria);
        spCategoria.setOnItemSelectedListener(MyListenerSpinner);
        btnGuardar.setOnClickListener(this);
        ibFoto.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case (R.id.imageButton):
                selectImage();
                break;
            case (R.id.button_agregar_contacto):
                nombre = etNombre.getText().toString();
                numero = etNumero.getText().toString();


                if (!nombre.isEmpty() && !numero.isEmpty()){

                    listaContactos = new ArrayList<>();
                    VariablesGlobales globalListaContactos = (VariablesGlobales) getApplicationContext();
                    listaContactos = globalListaContactos.getListaContactos();

                    if (posicion != -1){
                        listaContactos.get(posicion).setTipo(categoria);
                        listaContactos.get(posicion).setNombre(nombre);
                        listaContactos.get(posicion).setNumero(numero);
                        listaContactos.get(posicion).setImagen(foto);
                    }

                    globalListaContactos.setListaContactos(listaContactos);

                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(listaContactos);
                    editor.putString("Contactos", json);
                    editor.commit();

                    Intent intent = new Intent();
                    //intent.putExtra("Agregado",true);
                    setResult(RESULT_OK, intent);
                    intent.putExtra("SeEdito",true);

                    Toast.makeText(getApplicationContext(), "Modificaste al contacto!", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "Falto agregar el nombre o el numero! No se pudo modificar al contacto!", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            foto = stream.toByteArray();

            if (foto != null){
                Bitmap bmimage = BitmapFactory.decodeByteArray(foto,0,foto.length);
                ibFoto.setBackground(null);
                ibFoto.setImageBitmap(bmimage);
            }
        }
        else if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK && data != null){
            /*
            Log.d("Chose to pick image: ", "True");
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
            c.moveToFirst();


            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            Bitmap image = BitmapFactory.decodeFile(picturePath);

            Log.d("path of image: ", picturePath);
            ibFoto.setBackground(null);
            ibFoto.setImageBitmap(bitmap);
            c.close();*/
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ibFoto.setImageBitmap(selectedImage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                foto = stream.toByteArray();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (data == null){
            Log.d("data is null", " True");
            Toast.makeText(getApplicationContext(), "No se pudo cargar la imagen correctamente!", Toast.LENGTH_SHORT).show();

        }
    }

    private void selectImage() {

        final String[] opciones = { "Tomar foto", "Elegir de galeria","Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir foto");
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Tomar foto"))
                {
                    Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                    //Validar que el dispositivo tenga camara
                    if (intent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(intent,REQUEST_TAKE_PHOTO);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "El dispositivo no tiene una camara!", Toast.LENGTH_SHORT).show();

                }
                else if (opciones[which].equals("Elegir de galeria"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_PICK_PHOTO);

                }
                else if (opciones[which].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        };

        builder.setItems(opciones, dialogListener);
        builder.show();
    }
}
