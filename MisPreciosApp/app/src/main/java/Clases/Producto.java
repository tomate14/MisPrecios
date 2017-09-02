package Clases;

import java.io.Serializable;

/**
 * Created by Maxi on 3/8/2017.
 * modo = AGREGAR (MISMO PRODUCTO A OTRO SUPERMERCADO),
 *        EDITAR  (PRODUCTO Y SUPERMERCADO),
 *        NUEVO (UN PRODUCTO QUE NO ESTA EN LA BASE)
 */

public class Producto implements Serializable {
    
    private int id;
    private String codbarra;
    private String nombre;
    private float importe;
    private int idsupermercado;
    private String modo;

    public Producto(int id, String codbarra, String nombre, float importe, int idsupermercado) {
        this.id = id;
        this.codbarra = codbarra;
        this.nombre = nombre;
        this.importe = importe;
        this.idsupermercado = idsupermercado;
    }
    public Producto(String scanCode){
        this.codbarra = scanCode;
    }
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codbarra='" + codbarra + '\'' +
                ", nombre='" + nombre + '\'' +
                ", importe=" + importe +
                ", idsupermercado=" + idsupermercado +
                ", modo='" + modo + '\'' +
                '}';
    }

    public Producto(int id, String codbarra, String nombre) {
        this.id = id;
        this.codbarra = codbarra;
        this.nombre = nombre;
    }

    public Producto(String codbarra, String nombre, float importe, int idsupermercado) {
        this.codbarra = codbarra;
        this.nombre = nombre;
        this.importe = importe;
        this.idsupermercado = idsupermercado;
    }

    public Producto() {
        this.codbarra = "0";
        this.nombre = "";
        this.modo = "NUEVO";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodbarra() {
        return codbarra;
    }

    public void setCodbarra(String codbarra) {
        this.codbarra = codbarra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public int getIdsupermercado() {
        return idsupermercado;
    }

    public void setIdsupermercado(int idsupermercado) {
        this.idsupermercado = idsupermercado;
    }

    public boolean esVacio() {
        return (this.codbarra.equals(""));
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }
}
