package Clases;

import android.util.Log;

/**
 * Created by Maxi on 31/8/2017.
 */

public class ProductoCarrito {
    private String codBarra;
    private String nombre;
    private int estado;

    public ProductoCarrito(String codBarra, String nombre, int estado) {
        this.codBarra = codBarra;
        this.nombre   = nombre;
        this.estado   = estado;
        Log.d("PRODUCTOCREADO",this.toString());
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }



    @Override
    public String toString() {
        return "ProductoCarrito{" +
                "codBarra='" + codBarra + '\'' +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                '}';
    }
}
