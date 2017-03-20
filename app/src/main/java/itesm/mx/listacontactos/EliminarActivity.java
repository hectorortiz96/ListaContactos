package itesm.mx.listacontactos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class EliminarActivity extends AppCompatActivity {

    Button btnEliminar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);

        btnEliminar = (Button) findViewById(R.id.button_eliminar);



    }
}
