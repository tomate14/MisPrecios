package com.example.maxi.mispreciosapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Vector;

import Clases.Producto;
import Mostrar_Productos.ListViewAdapter;
import Mostrar_Productos.ListViewAdapterCarrito;


public class ScanCode extends AppCompatActivity {

    private Vector<Producto> productos;
    private ListView listView;
    private IntentIntegrator integrator;
    private ListViewAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ACTIVIDAD","ScanCode");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        this.productos = new Vector<Producto>();
        this.integrator = new IntentIntegrator(this);
        integrator.initiateScan();


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("ACTIVITIRESULT","");
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult.getFormatName() != null) {
            String scanContent = scanningResult.getContents();
            Producto producto = new Producto(scanContent);
            if(! Menu_Navigate.dataBase.existeProducto(producto.getCodbarra())){
                this.productos.add(producto);
                ContentValues registro = new ContentValues();
                registro.put("codBarra", producto.getCodbarra());
                registro.put("estado", 0);

                long cantidad = Menu_Navigate.dataBase.InsertEnBase("CARRITO",registro);
                if(cantidad>0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Agregado correctamente", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Error al agregar. Reintente", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ya esta en el carrito", Toast.LENGTH_SHORT);
                toast.show();
            }
            //integrator.initiateScan();
            this.recreate();
        }else{
            finish();
        }
    }

}
