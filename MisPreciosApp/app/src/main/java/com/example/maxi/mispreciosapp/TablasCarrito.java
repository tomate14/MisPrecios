package com.example.maxi.mispreciosapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import Clases.ItemInfoCarrito;
import Clases.ProductoCarrito;
import Clases.Supermercado;
import Mostrar_Productos.ListViewAdapterMenu;

public class TablasCarrito extends AppCompatActivity {

    //private TextView mTextMessage;
    private ExpandableListView exv;
    private ListViewAdapterMenu listView;
    private Context context;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    updateListViewProducto();
                    return true;
                case R.id.navigation_dashboard:
                    ArrayList<ProductoCarrito> listaProductos = new ArrayList<ProductoCarrito>();
                    HashMap<Integer,ArrayList<ItemInfoCarrito>> datos = new HashMap<Integer,ArrayList<ItemInfoCarrito>>();
                    listView = new ListViewAdapterMenu(context,listaProductos,datos);
                    exv.setAdapter(listView);
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablas_carrito);
        setTitle("LISTA DE COMPRAS");
        this.context = this;

        this.exv = (ExpandableListView) findViewById(R.id.listCarritoTab);
        updateListViewProducto();


        this.exv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            @Override
            public void onGroupExpand(int groupPosition) {
                for(int i=0;i<listView.getGroupCount();i++){
                    if(i!=groupPosition){
                        exv.collapseGroup(i);
                    }
                }
                int index = exv.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));
                Log.d("CLICKEABLE","ITEM CLICKEADO ["+String.valueOf(index)+"]");
                listView.setPosicion(index);
                exv.setItemChecked(index, true);
                listView.notifyDataSetChanged();
            }
        });
       // mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
    }

    private void updateListViewProducto(){
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
