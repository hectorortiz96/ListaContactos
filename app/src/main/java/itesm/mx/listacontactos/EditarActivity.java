package itesm.mx.listacontactos;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class EditarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ArrayList<Contacto> listaContactos;
    EditarAdapter adapter;
    final int REQUEST_EDITAR = 0;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        listView = (ListView) findViewById(R.id.listView);
        final VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
        listaContactos = (ArrayList<Contacto>)globalListaContactos.getListaContactos();
        adapter = new EditarAdapter(this, listaContactos);
        //setListAdapter(adapter);
        //getListView().setOnItemClickListener(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        //Menu menu = new MenuBuilder(getApplication());
        //getListView().addHeaderView(getMenuInflater().inflate(R.menu.main_menu,menu));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Contacto contacto = (Contacto) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, EditarContactoActivity.class);
        intent.putExtra("Contacto",contacto);
        intent.putExtra("posicion",position);
        startActivity(intent);
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }*/


}
