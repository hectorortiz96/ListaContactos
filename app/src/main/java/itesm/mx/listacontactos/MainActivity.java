package itesm.mx.listacontactos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener,View.OnClickListener{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ImageButton buttonSettings;
    List<Contacto> listaContactos;
    final int REQUEST_PERMISOS = 0;

//push carlos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.explistLista);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(this);

        buttonSettings = (ImageButton)findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(this);


    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent;
        intent = new Intent(MainActivity.this, ContactDetails.class);
        String nombre = (String)parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
        intent.putExtra("Nombre",nombre);
        startActivity(intent);
        return false;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();
        List<String> familiares =  new ArrayList<String>();
        List<String> amigos =  new ArrayList<String>();
        List<String> emergencias = new ArrayList<String>() ;
        listaContactos = new ArrayList<Contacto>();


        //Crea (si aun no ha sido creado) y accede al archivo donde esta guardada la informacion de los contactos
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);


        //Guarda una contraseña
        SharedPreferences.Editor editor = sharedPref.edit();
        //editor.clear();
        //editor.commit();
        String passwordValor = "usuario";
        editor.putString("password", passwordValor);
        editor.commit();

        //Trae la informacion de los contactos (se guardo en forma de un arreglo, la adquiero en forma de un arreglo)
        Gson gson = new Gson();
        String json = sharedPref.getString("Contactos", null);

        Type listType = new TypeToken<ArrayList<Contacto>>(){}.getType();
        listaContactos = gson.fromJson(json, listType);

        if (listaContactos != null){
            VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
            globalListaContactos.setListaContactos(listaContactos);

            //Adding child data
            for (int iCont = 0; iCont < listaContactos.size(); iCont++){
                Contacto contactoTemp = listaContactos.get(iCont);
                Log.d("Nombre:",listaContactos.get(iCont).getNombre());
                if (contactoTemp.getTipo() == 0)
                    familiares.add(contactoTemp.getNombre());
                else if (contactoTemp.getTipo() == 1)
                    amigos.add(contactoTemp.getNombre());
                else if (contactoTemp.getTipo() == 2)
                    emergencias.add(contactoTemp.getNombre());
            }
        }
        else
        {
            List<Contacto> NuevaLista = new ArrayList<Contacto>();
            VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
            globalListaContactos.setListaContactos(NuevaLista);
        }


        // Adding child data
        listDataHeader.add("Familiares");
        listDataHeader.add("Amigos");
        listDataHeader.add("Servicios de emergencias");

        /*
        // Adding child data
        familiares.add("Alejandro Salgado");
        familiares.add("Oscar Uriel");

        amigos.add("Carlos Sanchez");
        amigos.add("Martin Murillo");

        emergencias.add("Hospital Cruz Roja");
        emergencias.add("Policia");
        emergencias.add("Atencion Adulto Mayor");*/

        listDataChild.put(listDataHeader.get(0), familiares); // Header, Child data
        listDataChild.put(listDataHeader.get(1), amigos);
        listDataChild.put(listDataHeader.get(2), emergencias);

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        intent = new Intent(MainActivity.this, PermisosActivity.class);
        startActivityForResult(intent,REQUEST_PERMISOS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null){
            Boolean actualizar = data.getBooleanExtra("actualizar",false);
            if (actualizar) {

                VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
                listaContactos = globalListaContactos.getListaContactos();

                List<String> familiares =  new ArrayList<String>();
                List<String> amigos =  new ArrayList<String>();
                List<String> emergencias = new ArrayList<String>() ;

                for (int iCont = 0; iCont < listaContactos.size(); iCont++){
                    Contacto contactoTemp = listaContactos.get(iCont);
                    if (contactoTemp.getTipo() == 0)
                        familiares.add(contactoTemp.getNombre());
                    else if (contactoTemp.getTipo() == 1)
                        amigos.add(contactoTemp.getNombre());
                    else if (contactoTemp.getTipo() == 2)
                        emergencias.add(contactoTemp.getNombre());
                }
                listDataChild.clear();

                /*
                familiares.add("Alejandro Salgado");
                familiares.add("Oscar Uriel");

                amigos.add("Carlos Sanchez");
                amigos.add("Martin Murillo");

                emergencias.add("Hospital Cruz Roja");
                emergencias.add("Policia");
                emergencias.add("Atencion Adulto Mayor");*/

                listDataChild.put(listDataHeader.get(0), familiares); // Header, Child data
                listDataChild.put(listDataHeader.get(1), amigos);
                listDataChild.put(listDataHeader.get(2), emergencias);

                listAdapter.notifyDataSetChanged();
            }
        }

    }
}
