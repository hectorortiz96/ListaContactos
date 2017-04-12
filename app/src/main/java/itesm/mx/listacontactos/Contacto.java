package itesm.mx.listacontactos;

import java.io.Serializable;

/**
 * Created by Usuario on 20/03/2017.
 */

public class Contacto implements Serializable {

    private int tipo;
    private String nombre;
    private String numero;
    private byte[] imagen;

    public Contacto(int tipo, String nombre, String numero)
    {
        this.tipo = tipo;
        this.nombre = nombre;
        this.numero = numero;
        this.imagen = null;
    }

    public Contacto(int tipo, String nombre, String numero, byte[] imagen)
    {
        this.tipo = tipo;
        this.nombre = nombre;
        this.numero = numero;
        this.imagen = imagen;
    }

    public void setTipo(int tipo){this.tipo = tipo;}
    public int getTipo(){return tipo;}

    public void setNombre(String nombre){this.nombre = nombre;}
    public String getNombre(){return nombre;}

    public void setNumero(String numero){this.numero = numero;}
    public String getNumero(){return numero;}

    public void setImagen(byte[] imagen){this.imagen = imagen;}
    public byte[] getImagen(){
        return this.imagen;
    }

}
