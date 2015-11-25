package com.example.javi.recetas.bbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Ayudante de la tabla
 */
public class Ayudante extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "recetario.sqlite";

    public static final int DATABASE_VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql = " create table " + Contrato.TablaReceta.TABLA
                + " (" + Contrato.TablaReceta._ID
                + " integer primary key autoincrement , "
                + Contrato.TablaReceta.NOMBRE + " text , "
                + Contrato.TablaReceta.FOTO + " text , "
                + Contrato.TablaReceta.INTRUCCION + " text )";
        Log.v("SQLAAD", sql);
        db.execSQL(sql);

        sql = " create table " + Contrato.TablaIngrediente.TABLA
                + " (" + Contrato.TablaIngrediente._ID
                + " integer primary key autoincrement , "
                + Contrato.TablaIngrediente.NOMBRE + " text )";
        Log.v("SQLAAD", sql);
        db.execSQL(sql);

        sql = " create table " + Contrato.TablaRecetaIngrediente.TABLA
                + " (" + Contrato.TablaRecetaIngrediente._ID
                + " integer primary key autoincrement , "
                + Contrato.TablaRecetaIngrediente.IDRECETA + " text , "
                + Contrato.TablaRecetaIngrediente.IDINGREDENTE + " text , "
                + Contrato.TablaRecetaIngrediente.CANTIDAD + " text )";
        Log.v("SQLAAD", sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql =" drop table if exists " + Contrato.TablaReceta.TABLA;
        db.execSQL(sql);
        sql =" drop table if exists " + Contrato.TablaIngrediente.TABLA;
        db.execSQL(sql);
        sql =" drop table if exists " + Contrato.TablaRecetaIngrediente.TABLA;
        db.execSQL(sql);
        onCreate(db);
    }
}
