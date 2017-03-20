package itesm.mx.listacontactos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AgregarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int categoria;
    private String nombre;
    private String numero;
    List<Contacto> listaContactos;

    EditText etNombre;
    EditText etNumero;
    Spinner spCategoria;
    String[] stringCategorias;
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        etNombre = (EditText) findViewById(R.id.editText_nombre_contacto);
        etNumero = (EditText) findViewById(R.id.editText_numero_contacto);
        spCategoria = (Spinner) findViewById(R.id.spinner_categoria);
        stringCategorias = getResources().getStringArray(R.array.array_categorias);
        btnAgregar = (Button) findViewById(R.id.button_agregar_contacto);


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringCategorias); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(spinnerArrayAdapter);
        spCategoria.setOnItemSelectedListener(AgregarActivity.this);

        View.OnClickListener MyListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nombre = etNombre.getText().toString();
                numero = etNumero.getText().toString();
                Log.d("Nombre:",nombre);
                Log.d("Numero:",numero);
                if (!nombre.isEmpty() && !numero.isEmpty()){
                    listaContactos = new ArrayList<>();
                    Contacto contacto = new Contacto(categoria,nombre,numero);
                    String temp = Integer.toString(contacto.getTipo());
                    Log.d("Nombre contacto",contacto.getNombre());
                    Log.d("Numero contacto", contacto.getNumero());
                    Log.d("Categoria contacto:",temp);
                    VariablesGlobales globalListaContactos = (VariablesGlobales) getApplicationContext();
                    listaContactos = globalListaContactos.getListaContactos();
                    listaContactos.add(contacto);
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
                    Toast.makeText(getApplicationContext(), "Agregaste un contacto!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), "Falto agregar el nombre o el numero! No se agrego contacto!", Toast.LENGTH_SHORT).show();
                }

                finish();

            }
        };

        btnAgregar.setOnClickListener(MyListener);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String seleccion = (String)parent.getItemAtPosition(pos);
        Log.d("Categoria:",seleccion);
        if (seleccion.equals("Familiares")){
            categoria = 0;
        }
        else if (seleccion.equals("Amigos")){
            categoria = 1;
        }
        else if (seleccion.equals("Servicios de emergencias")) {
            categoria = 2;
        }
        String temp = Integer.toString(categoria);
        Log.d("Categoria Valor:",temp);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
