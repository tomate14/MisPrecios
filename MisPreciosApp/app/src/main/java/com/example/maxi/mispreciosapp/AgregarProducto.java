package com.example.maxi.mispreciosapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import Clases.Producto;


public class AgregarProducto extends AppCompatActivity {

    private EditText txtCodBarra;
    private EditText txtNombre;
    private EditText txtImporte;
    private Spinner spinnerSuper;
    private TextView txtSupermercado;
    private Button btnAceptar;
    private String itemSpinner;
    private String codBarra;
    private Producto producto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);
        this.txtCodBarra = (EditText) findViewById(R.id.txtCodBarra);
        this.txtNombre = (EditText) findViewById(R.id.txtNombre);
        this.txtImporte = (EditText) findViewById(R.id.IMPORTE);
        this.txtSupermercado = (TextView)  findViewById(R.id.textView3);
        this.spinnerSuper = (Spinner) findViewById(R.id.spinnerSuper);
        this.codBarra = (String)getIntent().getExtras().getSerializable("codBarra");
        this.producto = (Producto) getIntent().getExtras().getSerializable("Producto");

        Log.d("PRODUCTO","producto");
        Log.d("PRODUCTO",this.producto.getCodbarra());

        String query = " SELECT NOMBRE "+
                       " FROM   SUPERMERCADOS AS S " +
                       " WHERE  NOT EXISTS (SELECT 1 FROM VENDIDO AS V" +
                       "                    WHERE  V.idSupermercado = S.id" +
                       "                      AND  V.idProducto = '"+producto.getCodbarra()+"') " +
                       " ORDER BY NOMBRE ";

        this.txtCodBarra.setText(this.producto.getCodbarra());
        Menu_Navigate.utilidades.updateSpinner(this,spinnerSuper,query);

        getDatosProducto();

        this.spinnerSuper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {
                itemSpinner = spinnerSuper.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemSpinner = "";
            };
        });

        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codbarra = txtCodBarra.getText().toString();
                String nombre   = txtNombre.getText().toString();
                float  importe  = Float.parseFloat(txtImporte.getText().toString());
                producto.setImporte(importe);
                if(producto.getModo().equals("NUEVO")){
                    producto = new Producto(0,codbarra,nombre,importe,getItemSupermercado());
                    producto.setModo("NUEVO");
                }else {
                    producto.setImporte(importe);
                    if(producto.getModo().equals("AGREGAR")){
                        Cursor fila = Menu_Navigate.dataBase.getNombreSupermercado(itemSpinner);
                        fila.moveToFirst();
                        Log.d("idSupermercado", itemSpinner);
                        int idSupermercado = fila.getInt(0);//id
                        Log.d("idSupermercado", String.valueOf(idSupermercado));
                        producto.setIdsupermercado(idSupermercado);
                        producto.setNombre(nombre);
                    }
                }
                alta(producto);
            }

        });


    }

    private void alta(Producto producto) {
        ContentValues regProducto = new ContentValues();
        ContentValues regVendido = new ContentValues();
        if(producto.getId()!= 0){
            regProducto.put("id", producto.getId());
        }

        regProducto.put("codBarra", producto.getCodbarra());
        regProducto.put("nombre", producto.getNombre());
        regVendido.put("idProducto",producto.getCodbarra());
        if(producto.getIdsupermercado() == 0){
            producto.setIdsupermercado(1);
        }
        regVendido.put("idSupermercado",producto.getIdsupermercado());
        regVendido.put("importe",producto.getImporte());
        Log.d("regVendido - idProducto",regVendido.getAsString("idProducto"));
        Log.d("regVendido - idSupermercado", regVendido.getAsString("idSupermercado"));
        Log.d("regVendido - importe", String.valueOf(producto.getImporte()));

        if(!producto.getModo().equals("EDITAR")){
            long exito  = Menu_Navigate.dataBase.altaProducto(regProducto, regVendido,producto);
            if(exito == 1){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Agregado Correctament", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {

            Menu_Navigate.dataBase.updateProducto(producto,regProducto, regVendido);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Editado Correctament", Toast.LENGTH_SHORT);
            toast.show();

        }
        // ponemos los campos a vacío para insertar el siguiente producto
        txtCodBarra.setText(""); txtNombre.setText("");
        finish();
    }
    //verifica la existencia de un producto en la DB
    private boolean existeProducto() {
        Cursor cursor = Menu_Navigate.dataBase.getProductos(this.producto);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return true;
            }
            cursor.close();
        }
        return false;
    }

    private void getDatosProducto(){
        if(producto.getModo().equals("EDITAR")){
            Cursor cursor = Menu_Navigate.dataBase.getProductosSupermercado(this.producto);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                this.producto.setId(cursor.getInt(0));
                this.producto.setNombre(cursor.getString(1));
                this.producto.setImporte(cursor.getFloat(2));
                this.txtCodBarra.setEnabled(false);
                this.txtNombre.setText(this.producto.getNombre());
                this.txtImporte.setText(String.valueOf(this.producto.getImporte()));

                this.spinnerSuper.setVisibility(View.GONE);
                this.txtSupermercado.setVisibility(View.GONE);
                this.itemSpinner = cursor.getString(0);
            }
            cursor.close();
        }
        if(producto.getModo().equals("AGREGAR")) {
            Cursor cursor = Menu_Navigate.dataBase.getProductos(this.producto);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                this.producto.setId(cursor.getInt(0));
                this.producto.setNombre(cursor.getString(1));
                this.txtCodBarra.setEnabled(false);
                this.txtNombre.setText(this.producto.getNombre());
                this.txtNombre.setEnabled(false);
                this.txtImporte.setText("0");
                this.itemSpinner = cursor.getString(0);
            }
        }

    }


    private int getItemSupermercado() {
        Cursor fila = Menu_Navigate.dataBase.getNombreSupermercado(this.itemSpinner);
        if(fila.moveToFirst()){
            return fila.getInt(0);
        }
        return 0;
    }


}
