package itesm.mx.listacontactos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Usuario on 08/04/2017.
 */

public class EditarAdapter extends ArrayAdapter<Contacto> {

    public EditarAdapter(Context context, ArrayList<Contacto> contactoArray) {

        super(context, 0, contactoArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Contacto contacto = getItem(position);

        //Familiar -> Naranja
        if (contacto.getTipo() == 0) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_editar1, parent, false);
            }

            //Amigo -> Verde
        } else if (contacto.getTipo() == 1) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_editar2, parent, false);
            }

            //Servicios -> Rosa
        } else if (contacto.getTipo() == 2){

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_editar3, parent, false);
            }
        }
        else if (contacto.getTipo() == 3){

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_editar4, parent, false);
            }
        }

        TextView tvNombre = (TextView) convertView.findViewById(R.id.textView_nombreContacto);
        tvNombre.setText(contacto.getNombre());

        return convertView;

    }
}
