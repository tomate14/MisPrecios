package Conexion;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.maxi.mispreciosapp.Menu_Navigate;

import java.util.Vector;

import Clases.Producto;
import Clases.Supermercado;

/**
 * Created by Maxi on 3/8/2017.
 * PASAR LOS METODOS DE BASE DE DATOS A ESTA CLASE
 * QUERYS, TODOOOOOO
 */

public class BaseDeDatos {
    private SQLiteDatabase SQLite;
    private AdminSQLiteOpenHelper admin;

    public BaseDeDatos(Menu_Navigate menu_navigate){
        admin = new AdminSQLiteOpenHelper(menu_navigate,"administracion", null, 1);
        SQLite = admin.getWritableDatabase();
    }
    public void crearTablas(){
        this.SQLite.execSQL("CREATE TABLE IF NOT EXISTS PROVINCIAS (id INT( 2 ) NOT NULL , nombre VARCHAR( 50 ) NOT NULL , PRIMARY KEY (id) )");
        this.SQLite.execSQL("CREATE TABLE IF NOT EXISTS DEPARTAMENTOS (id INT( 3 ) NOT NULL ,provincia_id INT ( 2 ) NOT NULL ,nombre VARCHAR( 100 ) NOT NULL ,PRIMARY KEY (id))");
        this.SQLite.execSQL("CREATE TABLE IF NOT EXISTS CIUDADES (	id INT( 4 ) NOT NULL ,	departamento_id INT ( 3 ) NOT NULL ,	nombre VARCHAR( 100 ) NOT NULL ,	PRIMARY KEY (id))");
        this.SQLite.execSQL("CREATE TABLE IF NOT EXISTS PRODUCTOS(id INTEGER PRIMARY KEY AUTOINCREMENT, codbarra text, nombre text)");
        this.SQLite.execSQL("CREATE TABLE IF NOT EXISTS SUPERMERCADOS(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre text, provincia_id integer, departamento_id integer, ciudad_id integer)");
        this.SQLite.execSQL("CREATE TABLE IF NOT EXISTS VENDIDO (idProducto text, idSupermercado integer, importe float, PRIMARY KEY(idProducto,idSupermercado))");
        this.SQLite.execSQL("CREATE TABLE IF NOT EXISTS CARRITO (codBarra text,estado text, PRIMARY KEY(codBarra))");
    }
    public void borrarTablas(){
        this.SQLite.execSQL("DROP TABLE CARRITO");
        //this.SQLite.execSQL("DROP TABLE PROVINCIAS");
        //this.SQLite.execSQL("DROP TABLE CIUDADES");
        //this.SQLite.execSQL("DROP TABLE DEPARTAMENTOS");
        //this.SQLite.execSQL("DROP TABLE SUPERMERCADOS");
        //this.SQLite.execSQL("DROP TABLE PRODUCTOS");
        //this.SQLite.execSQL("DROP TABLE VENDIDO");
    }
    public boolean existenTablas(){
        Log.d("TABLAS","EXISTEN?");
        String query = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='PROVINCIAS'";
        String query2 = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='CIUDADES'";
        String query3 = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='DEPARTAMENTOS'";
        Cursor result1 = this.SQLite.rawQuery(query,null);
        Cursor result2 = this.SQLite.rawQuery(query,null);
        Cursor result3 = this.SQLite.rawQuery(query,null);
        return (result1.getCount()>0 && result2.getCount()>0 && result3.getCount()>0);
    }

    public void updateProducto(Producto producto, ContentValues regProducto, ContentValues regVendido){

        String where = "     CODBARRA = '"+producto.getCodbarra()+"'";
        this.SQLite.update("productos",regProducto,where,null);
        where = " idProducto = '"+producto.getCodbarra()+"' " +
                " AND idSupermercado = "+producto.getIdsupermercado();
        this.SQLite.update("vendido",regVendido, where, null);
    }

    public Cursor getProductosSupermercado(Producto producto) {
        String query = "SELECT P.id, P.nombre, V.importe, V.idSupermercado " +
                       " FROM  VENDIDO AS V INNER JOIN PRODUCTOS AS P ON (V.idProducto = P.codBarra)" +
                       " WHERE P.CODBARRA = '"+producto.getCodbarra()+"'  " +
                       "   AND V.idSupermercado = "+producto.getIdsupermercado() +
                       " ORDER BY P.CODBARRA ASC";
        Cursor aux = this.SQLite.rawQuery(query, null);
        return (aux);
    }
    public Cursor getProductos(Producto producto) {
        String query = "SELECT P.id, P.nombre, V.importe, V.idSupermercado " +
                " FROM  VENDIDO AS V INNER JOIN PRODUCTOS AS P ON (V.idProducto = P.codBarra)" +
                " WHERE P.CODBARRA = '"+producto.getCodbarra()+"'  " +
                " ORDER BY P.CODBARRA ASC";
        Cursor aux = this.SQLite.rawQuery(query, null);
        return (aux);
    }
    /*public Cursor getSupermercado(Producto producto) {
        String query = " SELECT id,nombre,provincia_id,departamento_id,ciudad_id " +
                       " FROM SUPERMERCADOS " +
                       " WHERE id= "+producto.getIdsupermercado();
        return (this.SQLite.rawQuery(query, null));
    }*/

    public Cursor getNombreSupermercado(String nombre) {
        String query = "SELECT id " +
                       " FROM SUPERMERCADOS " +
                      " WHERE NOMBRE = '"+nombre+"'";
       return(this.SQLite.rawQuery(query,null));
    }
    public Cursor getProvincia(String item) {
        String query = "SELECT P.* " +
                " FROM PROVINCIAS AS P"+
                " WHERE P.nombre = '"+item+"'";
        Cursor cursor = null;
        try {
            cursor = this.SQLite.rawQuery(query, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor;
            }
        }finally {
        }
        return null;
    }
    public Cursor getDepartamento(String item) {
        String query = " SELECT C.id,C.departamento_id " +
                " FROM   DEPARTAMENTOS AS D, CIUDADES AS C " +
                " WHERE  D.ID = C.DEPARTAMENTO_ID " +
                "   AND  C.nombre = '"+item+"'";
        Cursor cursor = null;
        try {
            cursor = this.SQLite.rawQuery(query, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor;
            }
        }finally {
        }
        return null;
    }

    /*
    * Get info para mostrar con el scaneo del articulo si existe en la base de datos
    */
    public Cursor getInfoListView(String codBarra) {
        String query = "  SELECT nombre,IMPORTE,V.idSupermercado " +
                       "  FROM VENDIDO AS V INNER JOIN SUPERMERCADOS AS S ON (S.id = V.idSupermercado) " +
                       "  WHERE  V.idProducto = '"+ codBarra+"'"+
                       "  ORDER BY S.NOMBRE";
        Log.d("getInfoListView",query);
       // Cursor cursor = this.SQLite.rawQuery(query,null);
        return (this.SQLite.rawQuery(query,null));
    }

    public long altaProducto(ContentValues regProducto, ContentValues regVendido, Producto producto) {
        //insert en producto y en VENDIDO (FALTA)
        long exitoProd = 2;
        if(producto.getModo().equals("NUEVO")) {
             exitoProd = this.SQLite.insert("productos", null, regProducto);
            Log.d("exitoProd", String.valueOf(exitoProd));
        }
        long exitoVendido = this.SQLite.insert("vendido",null,regVendido);

        if((exitoProd >2 ) && (exitoVendido>0)){
            return 1;
        }
        return 0;
    }

    public long altaSupermercado(String tabla, ContentValues registro) {
        return this.SQLite.insert(tabla,null,registro);
    }
    public void MostrarTabla(String tabla){
        String query = " SELECT * FROM "+tabla;
        Log.d("TABLA = ",tabla);
        Cursor c = SQLite.query(tabla, new String[]{"*"}, null,null, null, null, null);
        if (c.moveToFirst()) {
            do {

                Log.d("PRIMERO=",c.getString(0));
                Log.d("SEGUNDO=",c.getString(1));
                Log.d("TERCERO=",c.getString(2));
            } while(c.moveToNext());
        }
    }
    public boolean supermercadosParaAgregar(Producto producto){
        String query = " SELECT NOMBRE "+
                " FROM   SUPERMERCADOS AS S " +
                " WHERE  NOT EXISTS (SELECT 1 FROM VENDIDO AS V" +
                "                    WHERE  V.idSupermercado = S.id" +
                "                      AND  V.idProducto = '"+producto.getCodbarra()+"') " +
                " ORDER BY NOMBRE ";
        Cursor cursor = this.SQLite.rawQuery(query,null);
        Log.d("CANTIDAD", String.valueOf(cursor.getCount()));
        return (cursor.getCount()>0);

    }
    public Cursor rawQuery(String query, Object o) {
        return this.SQLite.rawQuery(query,null);
    }

    public void execSQL(String line) {
        this.SQLite.execSQL(line);
    }

    public Vector<Supermercado> cargarSupermercados() {
        Vector<Supermercado> aux = new Vector<Supermercado>();
        Log.d("CANTIDAD", "CARGANDO LOS SUPER");
        String query = " SELECT id,nombre,provincia_id,departamento_id,ciudad_id" +
                       " FROM SUPERMERCADOS ";
        Cursor fila = this.SQLite.rawQuery(query,null);
        if (fila.moveToFirst()) {
            do {
                int idSupermercado = fila.getInt(0);
                String nombre = fila.getString(1);
                int idProvincia = fila.getInt(2);
                int idDepartamento = fila.getInt(3);
                int idCiudad = fila.getInt(4);
                Supermercado sup = new Supermercado(idSupermercado,nombre,idProvincia,idDepartamento,idCiudad);
                Log.d("CANTIDAD", sup.toString());
            } while(fila.moveToNext());
        }
        return aux;
    }

    public Vector<Producto> cargarProductos() {
        Vector<Producto> aux = new Vector<Producto>();
        Log.d("CANTIDAD", "CARGANDO LOS PRODUCTOS");
        String query = " SELECT id,codBarra, nombre" +
                       " FROM PRODUCTOS ";
        Cursor fila = this.SQLite.rawQuery(query,null);
        if (fila.moveToFirst()) {
            do {
                int idProducto = fila.getInt(0);
                String nombre = fila.getString(1);
                String codBarra = fila.getString(2);
                Producto prod = new Producto(idProducto,codBarra,nombre);
                Log.d("CANTIDAD", prod.toString());
            } while(fila.moveToNext());
        }
        return aux;
    }
    public boolean existeProducto(String scanCode){

        String query = " SELECT * " +
                       " FROM   CARRITO " +
                       " WHERE  codBarra = '"+scanCode+"'";
        Log.d("QUERY", query);
        Cursor cursor = this.SQLite.rawQuery(query,null);
        Log.d("CANTIDAD", String.valueOf(cursor.getCount()));
        return (cursor.getCount()>0);


    }

    public long InsertEnBase(String tabla, ContentValues registro) {
        return this.SQLite.insert(tabla,null,registro);
    }

    public Cursor getInfoCarritoListView() {
        String query = " SELECT C.CODBARRA,IFNULL(P.Nombre,'SIN INFORMACION'), IFNULL(C.estado,0) " +
                       " FROM CARRITO AS C LEFT JOIN PRODUCTOS AS P ON (C.codBarra = P.codBarra) ";
        return (this.SQLite.rawQuery(query,null));
    }


    public Cursor getInfoCarritoSubListView(String codBarra){
        //Levanto datos para cargar la sublista por productos
        String query = " SELECT S.nombre, V.importe " +
                " FROM CARRITO AS C LEFT JOIN VENDIDO AS V ON (C.codBarra = V.idProducto)" +
                " LEFT JOIN SUPERMERCADOS AS S ON (V.idSupermercado = S.id)" +
                " WHERE C.codBarra = '"+codBarra+"' " +
                " ORDER BY V.importe ASC ";
        return (this.SQLite.rawQuery(query,null));
    }

    public Cursor getInfoCarritoListViewSupermercado() {
        String query = " SELECT  " +
                " FROM SUPERMERCADOS AS S  ";
        return (this.SQLite.rawQuery(query,null));
    }

    public void setEstados(String codBarra, ContentValues regCarrito) {
        //Set Estado del carrito cuando salgo de la actividad
        String where = "     CODBARRA = '"+codBarra+"'";
        this.SQLite.update("carrito",regCarrito,where,null);
    }
}
