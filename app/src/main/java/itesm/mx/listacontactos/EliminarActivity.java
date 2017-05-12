package itesm.mx.listacontactos;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EliminarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button btnEliminar;
    ArrayList<Contacto> listaContactos;
    EliminarAdapter adapter;
    ArrayList<Integer> contactosEliminarPos;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);

        final VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
        listaContactos = (ArrayList<Contacto>)globalListaContactos.getListaContactos();
        contactosEliminarPos = new ArrayList<>();
        adapter = new EliminarAdapter(this, listaContactos);

        listView = (ListView) findViewById(R.id.listViewHey);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        //setListAdapter(adapter);
        //getListView().setOnItemClickListener(this);


        //getListView().setOnItemClickListener(this);
        btnEliminar = (Button) findViewById(R.id.button_eliminar);

        View.OnClickListener MyListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("BOTON eliminar:", Integer.toString(contactosEliminarPos.size()));
                if (contactosEliminarPos.size() > 0) {

                    ArrayList<Contacto> tempContactos = new ArrayList<>();
                    for (int iCont = 0; iCont < listaContactos.size(); iCont++) {
                        Boolean mustDelete = false;
                        //Busca si el contacto actual debe o no ser eliminado
                        for (int iCont2 = 0; iCont2 < contactosEliminarPos.size(); iCont2++) {
                            if (iCont == contactosEliminarPos.get(iCont2)) {
                                mustDelete = true;
                                iCont2 = contactosEliminarPos.size();
                            }
                        }
                        //Si el contacto no debe ser eliminado, conservalo
                        if (!mustDelete) {
                            tempContactos.add(listaContactos.get(iCont));
                        }

                    }

                    //Actualiza el arreglo de contactos de la clase global con los contactos que no fueron eliminados
                    String eliminados = Integer.toString(listaContactos.size() - tempContactos.size());
                    listaContactos = tempContactos;
                    globalListaContactos.setListaContactos(listaContactos);

                    //Guarda los cambios en memoria
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(listaContactos);
                    editor.putString("Contactos", json);
                    editor.commit();

                    //Muestra feedback de la operacion realizada
                    Toast.makeText(getApplicationContext(), "Se eliminaron " + eliminados + " contactos!", Toast.LENGTH_SHORT).show();
                    finish();
                } else{
                    //Muestra feedback de la operacion realizada
                    Toast.makeText(getApplicationContext(), "No se elimino ningun contacto!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        };

        btnEliminar.setOnClickListener(MyListener);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.);
        //setSupportActionBar(toolbar);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Log.d("Llamada:", "ONITEMCLICK fue llamado");

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        //Si le dio click y ya esta palomeada la checkbox, despalomeala
        if (checkBox.isChecked())
            checkBox.setChecked(false);
        //Si le dio click y no esta palomeada la checkbox, palomeala
        else if (!checkBox.isChecked())
            checkBox.setChecked(true);

        //Guarda la posicion del contacto que debe ser eliminado
        if (checkBox.isChecked())
            contactosEliminarPos.add(position);
        //Borra del arreglo de posiciones de contactos a eliminar aquel contacto que ya no se va a eliminar
        else if (!checkBox.isChecked())
        {
            ArrayList<Integer> temp = new ArrayList<>();

            for (int iCont = 0; iCont < contactosEliminarPos.size(); iCont++){
                if (contactosEliminarPos.get(iCont) != position)
                {
                    Integer Aux = contactosEliminarPos.get(iCont);
                    temp.add(Aux);
                }
            }

            contactosEliminarPos = temp;
        }
        //Contacto contacto = (Contacto) parent.getItemAtPosition(position);
        Log.d("CHECKBOX eliminar:", Integer.toString(contactosEliminarPos.size()));

    }


}
