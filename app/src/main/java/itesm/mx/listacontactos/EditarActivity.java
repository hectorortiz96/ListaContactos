package itesm.mx.listacontactos;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;

public class EditarActivity extends ListActivity implements AdapterView.OnItemClickListener {


    ArrayList<Contacto> listaContactos;
    EditarAdapter adapter;
    final int REQUEST_EDITAR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        final VariablesGlobales globalListaContactos = ((VariablesGlobales) getApplicationContext());
        listaContactos = (ArrayList<Contacto>)globalListaContactos.getListaContactos();
        adapter = new EditarAdapter(this, listaContactos);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Contacto contacto = (Contacto) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, EditarContactoActivity.class);
        intent.putExtra("Contacto",contacto);
        intent.putExtra("posicion",position);
        startActivity(intent);
    }



}
