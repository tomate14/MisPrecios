package Mostrar_Productos;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.maxi.mispreciosapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import Clases.ItemInfoCarrito;
import Clases.ProductoCarrito;

/**
 * Created by Maxi on 1/9/2017.
 */

public class ListViewAdapterMenu extends BaseExpandableListAdapter{
    private Context contexto;
    private ArrayList<ProductoCarrito> productos;
    private int posicion;
    private HashMap<Integer,ArrayList<ItemInfoCarrito>> hijos;

    //Se usa un arreglo padre y un arreglo hijo para cargar cosas adentro
    //Arreglo padre viene con codigo de barra y nombre
    //Arreglo hijo viene con los supermercados y el valor

    public ListViewAdapterMenu(Context contexto, ArrayList<ProductoCarrito> listaProductos, HashMap<Integer,ArrayList<ItemInfoCarrito>> datos) {
        this.contexto = contexto;
        this.productos = listaProductos;
        this.hijos  = datos;
        this.posicion = -1;
    }

    @Override
    public int getGroupCount() {
        //cantidad de padres
        return this.productos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //cantidad de hijos del padre[groupPosition]
        return hijos.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //groupPosition posicion del grupo
        //childPosition posicion del hijo (sublista)
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //Informacion del padre, como se va a ver
        TextView txtTitle;
        TextView txtNombre;

        View inflate = View.inflate(contexto,R.layout.carritomulti_row,null);
        int estado = this.productos.get(groupPosition).getEstado();
        int incremento = 0;
        if(isExpanded){
            incremento = 1;
            inflate = setColorPorEstado(inflate, groupPosition, estado, incremento);
        }else{
            inflate = setColorPorEstado(inflate, groupPosition, estado, incremento);
        }

        txtTitle = (TextView) inflate.findViewById(R.id.listcarritomulti_title);
        txtNombre = (TextView) inflate.findViewById(R.id.listcarritomulti_nombre);
        txtTitle.setText(productos.get(groupPosition).getCodBarra());
        txtNombre.setText(productos.get(groupPosition).getNombre());
        return inflate;
    }

    public View setColorPorEstado(View colorPorEstado, int position, int estado, int incremento) {
        if (estado == 0) {
            this.productos.get(position).setEstado(this.productos.get(position).getEstado() + incremento);
            colorPorEstado.setBackgroundResource(Color.TRANSPARENT);
        } else if (estado == 1) {
            this.productos.get(position).setEstado(this.productos.get(position).getEstado() + incremento);
            colorPorEstado.setBackgroundResource(R.color.colorProductoCargado);
        } else if (estado == 2) {
            if(incremento!= 0){
                this.productos.get(position).setEstado(0);
            }
            colorPorEstado.setBackgroundResource(R.color.colorProductoNoEsta);
        }
        return colorPorEstado;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //informacion del hijo, como se va a ver

        View inflate = View.inflate(this.contexto, R.layout.carritosuper_row,null);

        TextView nombreSuper = (TextView) inflate.findViewById(R.id.txtCarritoSuper);
        String nombre  = this.hijos.get(groupPosition).get(childPosition).getNombreSuper();
        String importe = String.valueOf(this.hijos.get(groupPosition).get(childPosition).getImporte());
        if(nombre!=null && importe != null) {
            nombreSuper.setText(nombre + " - $" + importe);
        }
        return inflate;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //Se puede seleccionar a los hijos
        return false;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
}
