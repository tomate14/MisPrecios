package Mostrar_Productos;

/**
 * Created by Maxi on 30/7/2017.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maxi.mispreciosapp.R;

import java.util.ArrayList;

import Clases.ProductoCarrito;

public abstract class ListViewAdapter extends BaseAdapter {

    //IMPORTANTE, USAR LOS LAYOUT CON RELATIVE PARA LA LISTA, LA CONCHA DE TU MADRE
    //NO SEAS PELOTUDO, QUE ANDA MAL
    protected Context context;
    protected ArrayList<String> titulos;
    protected ArrayList<String> importe;
    protected ArrayList<Integer> estados;
    protected int posicion; //Posicion de seleccion del item de la lista
    protected boolean estadoInicial;
    protected LayoutInflater inflater;

    @Override
    //public Object getItem(int position) {
    //    return null;
    //}
    public abstract Object getItem(int position);
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItemId(int position) {
       posicion = position;
    }
    public ArrayList<String> getTitulos() {
        return titulos;
    }

    public void setTitulos(ArrayList<String> titulos) {
        this.titulos = titulos;
    }

    public ArrayList<Integer> getEstados() {
        return estados;
    }

    public void setEstados(ArrayList<Integer> estados) {
        this.estados = estados;
    }

    public boolean isEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(boolean estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public abstract int getCount();

    public abstract View getView(int position, View convertView, ViewGroup parent);

    public abstract  ArrayList<ProductoCarrito> getProductos();


}