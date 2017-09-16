package Mostrar_Productos;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maxi.mispreciosapp.Menu_Navigate;
import com.example.maxi.mispreciosapp.R;

import java.util.ArrayList;

import Clases.ProductoCarrito;

/**
 * Created by Maxi on 30/8/2017.
 */

public class ListViewAdapterCarrito extends ListViewAdapter {

    private ArrayList<ProductoCarrito> productos;

    public ListViewAdapterCarrito(Context context, ArrayList<ProductoCarrito> productos) {
        this.productos = productos;
        this.posicion = -1; //Inicializa por defecto
        this.estadoInicial = true;
        this.context  = context;
        Log.d("CANTIDAD","CANTIDAD DE PRODUCTOS= "+String.valueOf(productos.size()));
    }

    @Override
    public Object getItem(int position) {
        return productos.get(position);
    }

    @Override
    public int getCount() {
        return this.productos.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtTitle;
        TextView txtNombre;
        //View inflate = LayoutInflater.from(this.context).inflate(R.layout.carrito_row,parent,false);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.carrito_row, parent, false);
        }
        int estado = this.productos.get(position).getEstado();
        if((this.posicion == -1)||(this.posicion==position)) {
            setColorPorEstado(convertView, position,estado);
        }

        txtTitle = (TextView) convertView.findViewById(R.id.listcarrito_title);
        txtNombre = (TextView) convertView.findViewById(R.id.listcarrito_nombre);
        txtTitle.setText(productos.get(position).getCodBarra());
        txtNombre.setText(productos.get(position).getNombre());

        return convertView;
    }

    public void setColorPorEstado(View colorPorEstado, int position, int estado) {
        if (estado == 0) {
            this.productos.get(position).setEstado(this.productos.get(position).getEstado() + 1);
            colorPorEstado.setBackgroundResource(Color.TRANSPARENT);
        } else if (estado == 1) {
            this.productos.get(position).setEstado(this.productos.get(position).getEstado() + 1);
            colorPorEstado.setBackgroundResource(R.color.colorProductoCargado);
        } else if (estado == 2) {
            this.productos.get(position).setEstado(0);
            colorPorEstado.setBackgroundResource(R.color.colorProductoNoEsta);
        }
    }
    public ArrayList<ProductoCarrito> getProductos(){
        return this.productos;
    }
}
