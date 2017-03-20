package itesm.mx.listacontactos;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 20/03/2017.
 */

//Clase global a todas las demas clases
public class VariablesGlobales extends Application {
    private List<Contacto> ListaContactosGlobal;
    public void setListaContactos(List<Contacto> ListaContactos){this.ListaContactosGlobal = ListaContactos;}
    public List<Contacto> getListaContactos(){return ListaContactosGlobal;}

}
