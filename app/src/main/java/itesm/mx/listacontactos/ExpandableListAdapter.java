package itesm.mx.listacontactos;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Hector on 3/15/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private VariablesGlobales globalListaContactos ;
    private List<Contacto> listaContactos;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        //Aqui se tuvo que acceder a la clase global desde context porque el metodo getApplicationContext() solo se
        //puede utilizar en una activity, y esta clase no es una activity. Para poder utilizar el getApplicationContext() aqui,
        //se utiliza una referencia, en este caso el contexto de la actividad (cualquiera sirve) en que se creo un objeto de
        // esta clase; en este caso especifico, el contexto es el de la actividad MainActivity, en la cual se creo un objeto
        // de tipo ExpandableListAdapter
        this.globalListaContactos = ((VariablesGlobales)context.getApplicationContext());
        this.listaContactos = globalListaContactos.getListaContactos();
    }
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        String color = "nada";


        //Familiares -> Naranja
        if (groupPosition == 0){

            {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
                color = "naranja";
            }
        //Amigos -> Verde
        } else if (groupPosition == 1){

             {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group2, null);
                color = "verde";
            }
        //Servicios  -> Azul
        } else if (groupPosition == 2){

            {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group3, null);
                color = "azul";
            }
            //Otros -> Morado
        }  else if (groupPosition == 3) {

            {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group4, null);
                color = "morado";
            }
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        color = "now";
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        String headerTitle = (String)getGroup(groupPosition);
        if (groupPosition == 0){

            {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);

            }

        } else if (groupPosition == 1){

            {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item2, null);
            }

        } else if (groupPosition == 2){

             {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item3, null);
            }

        }  else if (groupPosition == 3){

            {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item4, null);
            }
        }


        TextView tvListChild = (TextView) convertView.findViewById(R.id.textListItem);
        tvListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
