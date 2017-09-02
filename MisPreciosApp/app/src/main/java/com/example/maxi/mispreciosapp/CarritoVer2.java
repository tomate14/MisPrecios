package com.example.maxi.mispreciosapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;

import Clases.ItemInfoCarrito;
import Clases.ProductoCarrito;
import Mostrar_Productos.ListViewAdapterMenu;

public class CarritoVer2 extends AppCompatActivity {

    private ExpandableListView exv;
    private ListViewAdapterMenu listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_ver2);

        this.exv = (ExpandableListView) findViewById(R.id.listaCarritoExp);
        updateListView();

    }

    private void updateListView(){
        // Log.d("UPDATE","updateListView");
        Cursor fila = Menu_Navigate.dataBase.getInfoCarritoListView();
        ArrayList<ProductoCarrito> listaProductos = new ArrayList<ProductoCarrito>();
        HashMap<Integer,ArrayList<ItemInfoCarrito>> datos = new HashMap<Integer,ArrayList<ItemInfoCarrito>>();
        if(fila.getCount()>0){
            while(fila.moveToNext()){
                String codBarra = fila.getString(0);
                String nombre   = fila.getString(1);
                int estado      = fila.getInt(2);
                ProductoCarrito productoCarrito = new ProductoCarrito(codBarra,nombre,estado);
                listaProductos.add(productoCarrito);
                Log.d("CANTIDAD",String.valueOf(listaProductos.size()));
                Cursor columna = Menu_Navigate.dataBase.getInfoCarritoSubListView(codBarra);
                ArrayList<ItemInfoCarrito> subLista = new ArrayList<>();
                if(columna.getCount()>0) {
                    while (columna.moveToNext()) {
                              nombre = columna.getString(0);
                        float importe = columna.getFloat(1);
                        ItemInfoCarrito item = new ItemInfoCarrito(nombre, importe, listaProductos.size()-1);
                        subLista.add(item);
                    }
                }
                datos.put(listaProductos.size()-1,subLista);
            }
            listView = new ListViewAdapterMenu(this,listaProductos,datos);
            exv.setAdapter(listView);
        }
    }


}
