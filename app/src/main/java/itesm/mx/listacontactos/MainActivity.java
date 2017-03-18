package itesm.mx.listacontactos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener,View.OnClickListener{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ImageButton buttonSettings;

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
        startActivity(intent);
        return false;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Familiares");
        listDataHeader.add("Amigos");
        listDataHeader.add("Servicios de emergencias");

        // Adding child data
        List<String> familiares = new ArrayList<String>();
        familiares.add("The Shawshank Redemption");
        familiares.add("The Godfather");

        List<String> amigos = new ArrayList<String>();
        amigos.add("Pedro Martinex");
        amigos.add("Martin Murillo");

        List<String> emergencias = new ArrayList<String>();
        emergencias.add("Hospital Cruz Roja");
        emergencias.add("Policia");
        emergencias.add("Atencion Adulto Mayor");

        listDataChild.put(listDataHeader.get(0), familiares); // Header, Child data
        listDataChild.put(listDataHeader.get(1), amigos);
        listDataChild.put(listDataHeader.get(2), emergencias);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
