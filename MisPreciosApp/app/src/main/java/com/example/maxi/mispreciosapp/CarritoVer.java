package com.example.maxi.mispreciosapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Clases.ProductoCarrito;
import Mostrar_Productos.ListViewAdapter;
import Mostrar_Productos.ListViewAdapterCarrito;

public class CarritoVer extends AppCompatActivity {

    private ListView listCarrito;
    private ListViewAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_ver);
        this.adapter = null;
        listCarrito = (ListView) findViewById(R.id.listaCarritoCompra);
        updateListView();
        listCarrito.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                adapter.setItemId(i);
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void onBackPressed(){
        Log.d("BACK","onBackPressed");
        if(this.adapter != null) {
            ArrayList<ProductoCarrito> productos = adapter.getProductos();
            updateEstadoCompra(productos);
        }
        super.onBackPressed();
    }

    private void updateEstadoCompra(ArrayList<ProductoCarrito> productos) {
        for(ProductoCarrito producto : productos){
                ContentValues regProducto = new ContentValues();
                Log.d("ACTUALIZO",producto.toString());
                if(producto.getEstado()==0) {
                    producto.setEstado(2);
                }else{
                    producto.setEstado(producto.getEstado()-1);
                }
                regProducto.put("estado", producto.getEstado());
                Menu_Navigate.dataBase.setEstados(producto.getCodBarra(),regProducto);
        }
    }

    private void updateListView(){
       //
        Cursor fila = Menu_Navigate.dataBase.getInfoCarritoListView();
        Log.d("UPDATE",String.valueOf(fila.getCount()));
        ArrayList<ProductoCarrito> listaProductos = new ArrayList<ProductoCarrito>();
        if(fila.getCount()>0){
            while(fila.moveToNext()){
                String codBarra = fila.getString(0);
                String nombre   = fila.getString(1);
                int estado      = fila.getInt(2);
                ProductoCarrito productoCarrito = new ProductoCarrito(codBarra,nombre,estado);
                listaProductos.add(productoCarrito);
            }
            adapter = new ListViewAdapterCarrito(this, listaProductos);
            listCarrito.setAdapter(adapter);
        }
    }
}
