package itesm.mx.listacontactos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    List<Contacto> listaContactos;
    final int REQUEST_PERMISOS = 0;
    EditText input;
    String passwordValor;

    View myView;
    Boolean contactosDefault;

//push carlos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.explistLista);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(this);


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
        List<String> otros =  new ArrayList<String>();
        List<String> emergencias = new ArrayList<String>() ;
        listaContactos = new ArrayList<Contacto>();


        //Crea (si aun no ha sido creado) y accede al archivo donde esta guardada la informacion de los contactos
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        passwordValor = sharedPref.getString("password", null);

        if (passwordValor == null){
            //Guarda una contraseña
            SharedPreferences.Editor editor = sharedPref.edit();
            //editor.clear();
            //editor.commit();
            passwordValor = getResources().getString(R.string.default_password);
            editor.putString("password", passwordValor);
            editor.commit();
        }


        //Trae la informacion de los contactos (se guardo en forma de un arreglo, la adquiero en forma de un arreglo)
        Gson gson = new Gson();
        String json = sharedPref.getString("Contactos", null);

        Type listType = new TypeToken<ArrayList<Contacto>>(){}.getType();
        listaContactos = gson.fromJson(json, listType);


        //Añade al arreglo de contactos los siguientes contactos default (haya habido o no haya habido un arreglo
        //de contactos guardado previamente en memoria)
        contactosDefault = sharedPref.getBoolean("contactosDefault",false);
        if (!contactosDefault)
        {

            Contacto contactoDefault1 = new Contacto(3, "Cruz Roja Monterrey", "83751212", null);
            //Contacto contactoDefault2 = new Contacto(3, "Cruz Verde", "13523786", null);
            Contacto contactoDefault3 = new Contacto(3, "Cuerpo de bomberos", "83420053", null);
            //Contacto contactoDefault4 = new Contacto(3, "Seguridad Publica del Estado", "066", null);
            //Contacto contactoDefault5 = new Contacto(3, "Policia Ministerial", "20204444", null);
            //Contacto contactoDefault6 = new Contacto(3, "Policia Federal Preventiva", "83850144", null);
            Contacto contactoDefault7 = new Contacto(3, "Policia y Transito Monterrey", "83050900", null);
            Contacto contactoDefault8 = new Contacto(3, "Proteccion Civil del Estado", "83428555", null);
            //Contacto contactoDefault9 = new Contacto(3, "Seguridad para el Turista", "018009039200", null);
            Contacto contactoDefault10 = new Contacto(3, "Ejercito Mexicano", "8116472003", null);
            //Contacto contactoDefault11 = new Contacto(3, "Multimedios Television", "83699973", null);

            listaContactos.add(contactoDefault1);
            //listaContactos.add(contactoDefault2);
            listaContactos.add(contactoDefault3);
            //listaContactos.add(contactoDefault4);
            //listaContactos.add(contactoDefault5);
            //listaContactos.add(contactoDefault6);
            listaContactos.add(contactoDefault7);
            listaContactos.add(contactoDefault8);
            //listaContactos.add(contactoDefault9);
            listaContactos.add(contactoDefault10);
            //listaContactos.add(contactoDefault11);

            contactosDefault = true;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("contactosDefault",contactosDefault);
            editor.commit();

            Gson gsonSave = new Gson();
            String jsonSave = gsonSave.toJson(listaContactos);
            editor.putString("Contactos", jsonSave);
            editor.commit();
        }

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
                else if (contactoTemp.getTipo() == 3)
                    otros.add(contactoTemp.getNombre());
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
        listDataHeader.add("Servicios de Salud");
        listDataHeader.add("Otros");

        //Relaciona los headers con los contactos que les corresponden (para eso usa un hashmap)
        listDataChild.put(listDataHeader.get(0), familiares); // Header, Child data
        listDataChild.put(listDataHeader.get(1), amigos);
        listDataChild.put(listDataHeader.get(2), emergencias);
        listDataChild.put(listDataHeader.get(3), otros);


    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){

            Boolean agregado = data.getBooleanExtra("agregado",false);
            Boolean modificado = data.getBooleanExtra("modificado",false);
            Boolean eliminado = data.getBooleanExtra("eliminado",false);
            Boolean cambioContrasena = data.getBooleanExtra("cambioContrasena", false);

            //Despliega los cambios realizados (contactos añadidos, editados o eliminados)
            if (agregado || modificado || eliminado) {

                //Accede al arreglo de contactos de la clase global, la cual contiene la informacion actualizada de los contactos
                VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
                listaContactos = globalListaContactos.getListaContactos();

                List<String> familiares =  new ArrayList<String>();
                List<String> amigos =  new ArrayList<String>();
                List<String> emergencias = new ArrayList<String>() ;
                List<String> otros = new ArrayList<String>() ;


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
                listDataChild.put(listDataHeader.get(3), otros);

                //Informa al adaptador que la informacion cruda (el hashmap) fue actualizada, y que por tanto tiene que actualizar
                //los views
                listAdapter.notifyDataSetChanged();
            }

            if (cambioContrasena){
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
                String tempString = sharedPref.getString("password", null);

                if (!tempString.isEmpty())
                    passwordValor = tempString;

            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here.  The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("onOptionsItemSelected: ", "Entro a este metodo");
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.ajustes){
            seleccionaAjustes();
            return true;
        }else if(item.getItemId() == R.id.cancelar) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void seleccionaAjustes() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduce la contraseña");

        LayoutInflater inflater = getLayoutInflater();
        myView = inflater.inflate(R.layout.password_alert_dialog,null);
        input = (EditText) myView.findViewById(R.id.et_password);

        //input = new EditText(MainActivity.this);

        /*
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        lp.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        input.setLayoutParams(lp);
        */

        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //builder.setView(input);

        builder.setView(myView);

        // Setting Positive "Yes" Button
        builder.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                        String temp = input.getText().toString();
                        if (temp.equals(passwordValor)){
                            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivityForResult(intent, 0);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Contraseña incorrecta!", Toast.LENGTH_SHORT).show();

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


        /*
        //ESTE CODIGO ESTA MUY FIXEADO, BORRAR DESPUES PARA ENCONTRAR LA SOLUCION CORRECTA PARA EL PADDING DE LA CONTRASEÑA
        AlertDialog alertDialog;
        alertDialog = builder.create();
        alertDialog.getWindow().setLayout(400, 200); //Controlling width and height.
        alertDialog.show();
        */

        builder.show();
    }
}
