package Conexion;

/**
 * Created by Maxi on 16/7/2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{

    public AdminSQLiteOpenHelper(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, nombre, factory, version);
        Log.d("PATHHHHHHHHH",context.getPackageResourcePath());
        Log.d("PATHHHHHHHHH",context.getPackageCodePath());

    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        //aquí creamos la tabla de usuario (dni, nombre, ciudad, numero)
        Log.d("AdminSQLiteOpenHelper",db.getPath());
        db.execSQL("create table supermercado(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre text, ciudad text)");
        db.execSQL("create table productos(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " codbarra text, nombre text, importe float, idsupermercado integer)");
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int version1, int version2) {
        Log.d("onUpgrade",db.getPath());
        db.execSQL("drop table if exists usuario");
        db.execSQL("create table supermercado(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre text, ciudad text)");
        db.execSQL("create table productos(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " codbarra text, nombre text, importe float, idsupermercado integer)");

    }

}