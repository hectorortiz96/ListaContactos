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
    //Se manda a llamar al hacerle click a cualquier renglon de un header
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        //Para obtener la informacion de un contacto, lo unico que necesitas es pasar a la otra actividad el nombre del
        //contacto sobre el cual se le hizo click (en la otra actividad se buscara dicho nombre en el arreglo de contactos
        //de la clase global y se obtendra la informacion restante)
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

        //Si habia un arreglo de contactos guardado en memoria
        if (listaContactos != null){
            //Guarda en la clase global el arreglo de contactos que extrajiste de memoria para que otras actividades puedan tener acceso a esa informacion
            VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
            globalListaContactos.setListaContactos(listaContactos);

            //Del arreglo de contactos extraido, añadelos al arreglo de strings que corresponda (el de familiares, amigos o servicios)
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
         //Si no habia un arreglo de contactos guardado en memoria, entonces: 1)Deja los arreglos de strings vacios (familiares, amigos, etc),
            // 2)Inicializa al arreglo de contactos de la clase global como un arreglo vacio
            List<Contacto> NuevaLista = new ArrayList<Contacto>();
            VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
            globalListaContactos.setListaContactos(NuevaLista);
        }


        // Asigna los valores estaticos de los headers
        listDataHeader.add("Familiares");
        listDataHeader.add("Amigos");
        listDataHeader.add("Servicios de emergencias");

        //Relaciona los headers con los contactos que les corresponden (para eso usa un hashmap)
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
            //Despliega los cambios realizados (contactos añadidos, editados o eliminados)
            if (actualizar) {

                //Accede al arreglo de contactos de la clase global, la cual contiene la informacion actualizada de los contactos
                VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
                listaContactos = globalListaContactos.getListaContactos();

                List<String> familiares =  new ArrayList<String>();
                List<String> amigos =  new ArrayList<String>();
                List<String> emergencias = new ArrayList<String>() ;

                //Utiliza el arreglo de contactos de la clase global para guardar a los contactos en el arreglo de strings
                //que le corresponde (familiares, amigos, servicios)
                for (int iCont = 0; iCont < listaContactos.size(); iCont++){
                    Contacto contactoTemp = listaContactos.get(iCont);
                    if (contactoTemp.getTipo() == 0)
                        familiares.add(contactoTemp.getNombre());
                    else if (contactoTemp.getTipo() == 1)
                        amigos.add(contactoTemp.getNombre());
                    else if (contactoTemp.getTipo() == 2)
                        emergencias.add(contactoTemp.getNombre());
                }

                //Limpia el hashmap
                listDataChild.clear();

                //Agrega al hashmap los valores actualizados (los headers no cambian, pero sus items o renglones
                //dentro de ellos si lo hacen
                listDataChild.put(listDataHeader.get(0), familiares); // Header, Child data
                listDataChild.put(listDataHeader.get(1), amigos);
                listDataChild.put(listDataHeader.get(2), emergencias);

                //Informa al adaptador que la informacion cruda (el hashmap) fue actualizada, y que por tanto tiene que actualizar
                //los views
                listAdapter.notifyDataSetChanged();
            }
        }

    }
}
