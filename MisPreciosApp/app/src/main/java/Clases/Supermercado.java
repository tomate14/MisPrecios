package Clases;

import java.io.Serializable;

/**
 * Created by Maxi on 5/8/2017.
 */

public class Supermercado implements Serializable{
    private String nombre;
    private int idSupermercado;
    private int idDepartamento;
    private int idProvincia;
    private int idCiudad;

    public Supermercado(String nombre, int idSupermercado) {
        this.nombre = nombre;
        this.idSupermercado = idSupermercado;
    }

    public Supermercado() {
        this.idSupermercado = 0;
        this.nombre = "";
    }

    public Supermercado(int idSupermercado, String nombre, int idDepartamento, int idProvincia, int idCiudad) {
        this.nombre = nombre;
        this.idSupermercado = idSupermercado;
        this.idDepartamento = idDepartamento;
        this.idProvincia = idProvincia;
        this.idCiudad = idCiudad;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdSupermercado() {
        return idSupermercado;
    }

    public void setIdSupermercado(int idSupermercado) {
        this.idSupermercado = idSupermercado;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public int getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

    @Override
    public String toString() {
        return "Supermercado{" +
                "nombre='" + nombre + '\'' +
                ", idSupermercado=" + idSupermercado +
                ", idDepartamento=" + idDepartamento +
                ", idProvincia=" + idProvincia +
                ", idCiudad=" + idCiudad +
                '}';
    }
}
