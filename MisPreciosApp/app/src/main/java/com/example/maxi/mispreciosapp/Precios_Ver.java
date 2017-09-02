package com.example.maxi.mispreciosapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

import Clases.Producto;
import Clases.Supermercado;
import Mostrar_Productos.ListViewAdapter;
import Mostrar_Productos.ListViewAdapterPrecios;

public class Precios_Ver extends AppCompatActivity {
    private String codBarra;
    private ListView listView;
    private ListViewAdapter adapter;
    private AppCompatActivity precios_ver;
    ArrayList<String> nombreSupermercados;
    Vector<Supermercado> infoSupermercado;
    private Producto producto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precios_ver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.precios_ver = this;
        this.codBarra = (String)getIntent().getExtras().getSerializable("codBarra");

        //YA LLEGA CON EDITAR
        this.producto = (Producto) getIntent().getExtras().getSerializable("Producto");
        this.nombreSupermercados = new ArrayList<>();
        this.infoSupermercado = new Vector<Supermercado>();

        this.listView = (ListView) findViewById(R.id.listView1);

        Log.d("PRODUCTO","producto");
        Log.d("PRODUCTO",this.producto.toString());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                int pos = adapterView.getPositionForView(view);
                Toast.makeText(getApplicationContext(), "presiono " + pos, Toast.LENGTH_SHORT).show();
                generarSubMenu(view,pos);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "presiono LARGO " + i, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        if(Menu_Navigate.dataBase.supermercadosParaAgregar(producto)) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mainActivity = new Intent(Precios_Ver.this, AgregarProducto.class);
                    producto.setModo("AGREGAR");
                    //NO TIENE EL SUPERMERCADO
                    //Log.d("PRECIOS_VER", "AGREGAR");
                    mainActivity.putExtra("Producto", producto);
                    mainActivity.putExtra("codBarra", producto.getCodbarra());
                    startActivity(mainActivity);
                }
            });
        }
        updateListView(this.listView);
    }



    private void updateListView(ListView lista) {
        Cursor fila = Menu_Navigate.dataBase.getInfoListView(this.producto.getCodbarra());
        ArrayList<String> importes = new ArrayList<>();

        if(fila.getCount()>0){
            while(fila.moveToNext()){
                Supermercado supermecado = new Supermercado(fila.getString(0),fila.getInt(2));
                infoSupermercado.add(supermecado);
                this.nombreSupermercados.add(fila.getString(0));
                importes.add("$"+fila.getString(1));
            }
            adapter = new ListViewAdapterPrecios(this, nombreSupermercados,importes);
            lista.setAdapter(adapter);
        }
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
    }

    public void onBackPressed(){
        this.finish();
    }
    //Genero el submenu para editar o eliminar los datos del super

    private void generarSubMenu(View view, final int pos) {
        PopupMenu popup = new PopupMenu(Precios_Ver.this, view);
        popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_eliminar:
                        Toast.makeText(Precios_Ver.this, "soy eliminar de la fila", Toast.LENGTH_LONG).show();
                    case R.id.menu_editar:
                        Intent editarActividad = new Intent(Precios_Ver.this,AgregarProducto.class);
                        //Obtengo id de supermercado
                        producto.setIdsupermercado(infoSupermercado.elementAt(pos).getIdSupermercado());
                        Log.d("PRECIOS_VER","EDITAR");

                        editarActividad.putExtra("codBarra",producto.getCodbarra());
                        editarActividad.putExtra("Producto",producto);
                        startActivity(editarActividad);
                        finish();
                        //refrescar la activida
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
}
