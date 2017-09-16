package com.example.maxi.mispreciosapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Vector;

import Clases.Producto;
import Clases.Supermercado;
import Clases.Utilidades;
import Conexion.AdminSQLiteOpenHelper;
import Conexion.BaseDeDatos;

public class Menu_Navigate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Utilidades utilidades;
    private RewardedVideoAd mAD;
    private TextView txtBienvenida;
    public static BaseDeDatos dataBase;
    private Vector<Supermercado> supermercados;
    private Vector<Producto> productos;
    private String tipoScan;
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        inicializarComponentesAndroid();
        procesoInicio();

        MobileAds.initialize(this, "interstitial_ad_unit_id");
    }

    private void inicializarComponentesAndroid() {
        setContentView(R.layout.activity_menu__navigate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        this.txtBienvenida = (TextView) findViewById(R.id.textBienvenida);
        this.scanButton    = (Button)   findViewById(R.id.btnScan);
        this.scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScanCodigo();
            }
        });


    }

    private void procesoInicio(){
        presentacion();
        utilidades = new Utilidades();
        dataBase = new BaseDeDatos(this);
        dataBase.crearTablas();
        if(!dataBase.existenTablas()){
            this.utilidades.cargarTablas(dataBase);
        }
        //Estan aca para un futuro
        this.supermercados = dataBase.cargarSupermercados();
        this.productos     = dataBase.cargarProductos();

        String pathDatabase = getDatabasePath("administracion.db").getAbsolutePath();
    }

    private void presentacion() {
        this.txtBienvenida.setText("BIENVENIDOS A MIS PRECIOS.\n \n" +
                                   "Aqui podras almacenar tu lista de precios del supermercado que vos elijas" +
                                   " con el fin de poder controlarlos y saber donde te conviene adquirir cada producto. \n \n" +
                                   "Solo basta con tomar la lectura del codigo de barras del producto para poder" +
                                   " efectuar algunas de las opciones que te brindamos con el fin de ayudarte en la elección" +
                                   " del mejor precio.");
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scan) {
            //tipoScan = "BUSCAR";
            ScanCodigo();
        } else if (id == R.id.nav_addSuper) {
            Intent mainActivity = new Intent(this,AltaSuper.class);
            startActivity(mainActivity);
        } else if (id == R.id.nav_modiSuper) {
            this.dataBase.MostrarTabla("VENDIDO");
            this.dataBase.MostrarTabla("SUPERMERCADOS");
            this.dataBase.MostrarTabla("PRODUCTOS");
        } else if (id == R.id.nav_deleSuper) {
            borrarSupermercados();
        }else if (id == R.id.nav_addProduMan) {
            Intent addManual = new Intent(this,AgregarProducto.class);
            Producto producto = new Producto();
            addManual.putExtra("codBarra","0");
            addManual.putExtra("Producto",producto);
            startActivity(addManual);
        }else if (id == R.id.nav_listaArmar) {
            Intent addCarrito = new Intent(this,ScanCode.class);
            startActivity(addCarrito);
        }else if (id == R.id.nav_listaVer3) {
            Intent verCarrito = new Intent(this,TablasCarrito.class);
            startActivity(verCarrito);
        }//else if (id == R.id.nav_listaVer) {
          //  Intent verCarrito = new Intent(this,CarritoVer.class);
          //  startActivity(verCarrito);
        //}




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void borrarSupermercados() {
        this.dataBase.borrarTablas();
        this.dataBase.crearTablas();
        //utilidades.cargarTablas(this.dataBase);
    }

    public void ScanCodigo(){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //Se procede con el proceso de scaneo
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult.getFormatName() != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Contenido: " + scanContent + " Formato: " + scanFormat, Toast.LENGTH_SHORT);
            toast.show();
            // display it on screen
            //if(tipoScan.equals("BUSCAR")){
                Producto producto = new Producto();
                producto.setCodbarra(scanContent);
                if(existeCodigo(producto)){
                    mostrarArticulos(scanContent,producto);
                }else{
                    agregarArticulo(scanContent,producto);
                }


        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se ha recibido datos del scaneo!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void mostrarArticulos(String scanContent, Producto producto) {
        Intent mostrar = new Intent(this,Precios_Ver.class);
        producto.setModo("EDITAR");
        mostrar.putExtra("codBarra",scanContent);
        mostrar.putExtra("Producto",producto);
        startActivity(mostrar);
    }

    private void agregarArticulo(String scanContent, Producto producto){
        Intent agregar = new Intent(this,AgregarProducto.class);
        producto.setModo("NUEVO");
        agregar.putExtra("codBarra",scanContent);
        agregar.putExtra("Producto",producto);
        startActivity(agregar);
    }

    private boolean existeCodigo(Producto producto){
        Cursor fila = Menu_Navigate.dataBase.getProductos(producto);
        if(fila.getCount()>0){
            return true;
        }
        return false;
    }
}
