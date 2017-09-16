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
import android.widget.Toast;


public class AltaSuper extends AppCompatActivity {
    private Button btnAgregar;
    private EditText txtNombre;
    private Spinner  spinnerProv;
    private Spinner  spinnerCiudad;
    private AppCompatActivity actividad;
    private boolean selected;
    private String textSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_super);
        setTitle("AGREGAR SUPERMERCADO");
        this.actividad = this;
        this.selected = false;
        this.btnAgregar = (Button) findViewById(R.id.btnAgregar);
        this.txtNombre  = (EditText)findViewById(R.id.txtNombre);
        this.spinnerProv = (Spinner) findViewById(R.id.spinnerProvi);
        this.spinnerCiudad = (Spinner) findViewById(R.id.spinnerCiudad);

        String query =
        " SELECT NOMBRE "+
                " FROM PROVINCIAS AS P"+
                " ORDER BY NOMBRE ";
        Menu_Navigate.utilidades.updateSpinner(this, this.spinnerProv,query);
        this.spinnerProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {
                selected = false;
                String text = (String) spinnerProv.getSelectedItem();
                String query = " SELECT C.NOMBRE "+
                               " FROM PROVINCIAS AS P, DEPARTAMENTOS AS D, CIUDADES AS C"+
                               " WHERE P.ID = D.provincia_id" +
                               "   AND D.ID = C.departamento_id"+
                               "   AND P.nombre = '"+text+"'" +
                               " ORDER BY C.NOMBRE ";
                Menu_Navigate.utilidades.updateSpinner(actividad,spinnerCiudad,query);
                selected = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textSpinner = "";
            };
        });
        this.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtNombre.getText().toString();
                if(!existeSuper(nombre)){
                    agregarSuper(nombre);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "El supermercado ya existe", Toast.LENGTH_SHORT);
                    toast.show();
                }
                finish();
            }

        });
    }


    private void agregarSuper(String nombre) {
            /*String query = "SELECT P.* " +
                           " FROM PROVINCIAS AS P"+
                           " WHERE P.nombre = '"+(String) spinnerProv.getSelectedItem()+"'";
            Cursor cursor_provincia = Menu_Navigate.utilidades.getValor(query);*/
            Cursor cursor_provincia = Menu_Navigate.dataBase.getProvincia((String) spinnerProv.getSelectedItem());
            int id_provincia    = cursor_provincia.getInt(cursor_provincia.getColumnIndex("id"));

            /*query = " SELECT C.id,C.departamento_id " +
                        " FROM   DEPARTAMENTOS AS D, CIUDADES AS C " +
                        " WHERE  D.ID = C.DEPARTAMENTO_ID " +
                        "   AND  C.nombre = '"+(String) spinnerCiudad.getSelectedItem()+"'";
            Cursor cursor_depto_ciudad = Menu_Navigate.utilidades.getValor(query);*/
            Cursor cursor_depto_ciudad = Menu_Navigate.dataBase.getDepartamento((String) spinnerCiudad.getSelectedItem());
            int id_departamento = cursor_depto_ciudad.getInt(cursor_depto_ciudad.getColumnIndex("departamento_id"));
            int id_ciudad       = cursor_depto_ciudad.getInt(cursor_depto_ciudad.getColumnIndex("id"));

            ContentValues registro = new ContentValues();
            registro.put("nombre", nombre);
            registro.put("provincia_id",id_provincia);
            registro.put("departamento_id",id_departamento);
            registro.put("ciudad_id",id_ciudad);
            // los inserto en la base de dato
            long count = Menu_Navigate.dataBase.altaSupermercado("SUPERMERCADOS",registro);
            if(count>0){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Agregado Correctament", Toast.LENGTH_SHORT);
                toast.show();
            }
    }

    private boolean existeSuper(String nombre) {
        Cursor fila = Menu_Navigate.dataBase.getNombreSupermercado(nombre);
        if(fila.getCount()>0){
            return true;
        }
        return false;
    }
}
