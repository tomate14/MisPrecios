package Clases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maxi.mispreciosapp.Menu_Navigate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import Conexion.BaseDeDatos;

/**
 * Created by Maxi on 29/7/2017.
 */

public class Utilidades {
    public static String limpiarAcentos(String cadena) {
        String limpio =null;
        if (cadena !=null) {
            String valor = cadena;
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");

            return limpio;
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
        }
        return limpio;
    }
    //public boolean tablasExisten(SQLiteDatabase db){
   //     String query = "SELECT name FROM sqlite_master WHERE TYPE='table' AND name in" +
   //             " ('CIUDADES','PROVINCIAS','PRODUCTOS','SUPERMERCADO')";
   //     return true
   // }
    public boolean updateSpinner(Context context, Spinner spinner,String query) {
        Cursor fila = Menu_Navigate.dataBase.rawQuery(query,null);
        List list = new ArrayList<>();
        if(fila.getCount()>0){
            while(fila.moveToNext()){
                list.add(fila.getString(0));
            }
            ArrayAdapter spinner_adapter;
            spinner_adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, list);
            spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinner_adapter);

            return true;
        }
        return false;

    }
    /*
     * Levantar un CSV a SQL Lite
     * Primer parametro es la tabla que se inserta
     * Segundo parametro el nombre de la carpeta donde esta el archivo
     */

    public boolean csvTodatabase(String route) {
        //get all files from extarnal drive data directory
        ArrayList<File> files = new ArrayList<File>();
        File directory = new File(route);
        if (!directory.exists()) {
            return false;
        }
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            }
        }
        for (File csvfile : files) {
            readFromFile(csvfile);
        }
        return true;
    }

    private void readFromFile(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                Log.d("LINE= ",line);
                Menu_Navigate.dataBase.execSQL(line);
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
    }

    /*
    Sacar contenido de la base de datos a la direccion que se le da
     */
    private void exportSQLite(String current, String Backup) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "/data/data/com.example.maxi.mispreciosapp/databases/administracion";
                String backupDBPath = "/storage/sdcard1/Download/administracion.db";
                File currentDB = new File("", currentDBPath);
                File backupDB = new File("", backupDBPath);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception e) {
        }
    }



    public void cargarTablas(BaseDeDatos SQLite) {
        //ArrayList<String> lista = new ArrayList<String>();
        //SQLite = admin.getWritableDatabase();
        /*String query = "SELECT * FROM SUPERMERCADOS ORDER BY ID ASC";
        Cursor fila = SQLite.rawQuery(query,null);// where dni=" + dni, null);
        while (fila.moveToNext()){
            lista.add(fila.getString(0));
        }
        query = "SELECT * FROM PRODUCTOS";
        fila = SQLite.rawQuery(query,null);// where dni=" + dni, null);
        while (fila.moveToNext()){
            lista.add(fila.getString(0));
        }*/
        csvTodatabase("/storage/sdcard1/Download/Nueva");
    }
}
