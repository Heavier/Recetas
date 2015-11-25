package com.example.javi.recetas.receta_ingrediente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.javi.recetas.bbdd.Ayudante;
import com.example.javi.recetas.bbdd.Contrato;
import com.example.javi.recetas.ingrediente.Ingrediente;

import java.util.ArrayList;
import java.util.List;


public class GestorReIn {
    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorReIn(Context c){
        abd = new Ayudante(c);
    }

    public void open(){
        bd = abd.getWritableDatabase();
    }

    public void openRead(){
        bd = abd.getReadableDatabase();
    }

    public void close(){
        abd.close();
    }

    public long insert(RecetaIngrediente rei){
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaRecetaIngrediente.IDRECETA, rei.getIdReceta());
        valores.put(Contrato.TablaRecetaIngrediente.IDINGREDENTE, rei.getIdIngrediente());
        valores.put(Contrato.TablaRecetaIngrediente.CANTIDAD, rei.getCantidad());
        long id = bd.insert(Contrato.TablaRecetaIngrediente.TABLA, null, valores);
        return id;
    }

    public int delete(RecetaIngrediente rei){
        return delete(rei.getId());
    }

    public int delete(long id){
        String condicion = Contrato.TablaRecetaIngrediente._ID + "=?";
        String[] argumentos = {id + ""};
        int cuenta = bd.delete(Contrato.TablaRecetaIngrediente.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(RecetaIngrediente rei){
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaRecetaIngrediente.IDRECETA, rei.getIdReceta());
        valores.put(Contrato.TablaRecetaIngrediente.IDINGREDENTE, rei.getIdIngrediente());
        valores.put(Contrato.TablaRecetaIngrediente.CANTIDAD, rei.getCantidad());
        String condicion = Contrato.TablaRecetaIngrediente._ID + "= ?";
        String[] argumentos = {rei.getId() + ""};
        int cuenta = bd.update(Contrato.TablaRecetaIngrediente.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List<RecetaIngrediente> select(String condicion){
        List<RecetaIngrediente> lc = new ArrayList<>();
        Cursor cursor = bd.query(
                Contrato.TablaRecetaIngrediente.TABLA, null, condicion, null, null, null, null);
        cursor.moveToFirst();
        RecetaIngrediente in;
        while (!cursor.isAfterLast()) {
            in = getRow(cursor);
            lc.add(in);
            cursor.moveToNext();
        }
        cursor.close();
        return lc;
    }

    public List<RecetaIngrediente> select(String condicion, String[] paramentros){
        List<RecetaIngrediente> la = new ArrayList<>();
        Cursor cursor = getCursor(condicion, paramentros);
        RecetaIngrediente p;
        while (cursor.moveToNext()){
            p = getRow(cursor);
            la.add(p);
        }
        cursor.close();
        return la;
    }

    public List<RecetaIngrediente> select(){
        return select(null, null);
    }


    public RecetaIngrediente getRow(Cursor c) {
        RecetaIngrediente rei = new RecetaIngrediente();
        rei.setId(c.getLong(c.getColumnIndex(Contrato.TablaRecetaIngrediente._ID)));
        rei.setIdReceta(c.getLong(c.getColumnIndex(Contrato.TablaRecetaIngrediente.IDRECETA)));
        rei.setIdIngrediente(c.getLong(c.getColumnIndex(Contrato.TablaRecetaIngrediente.IDINGREDENTE)));
        rei.setCantidad(c.getInt(c.getColumnIndex(Contrato.TablaRecetaIngrediente.CANTIDAD)));
        return rei;
    }

    public RecetaIngrediente getRow(long id) {
        Cursor c = getCursor("_id = ?",new String[]{id+""});
        return getRow(c);
    }

    public RecetaIngrediente getRowPrimero(Cursor c) {
        RecetaIngrediente rei = new RecetaIngrediente();
        rei.setId(c.getLong(0));
        rei.setIdReceta(c.getLong(1));
        rei.setIdIngrediente(c.getLong(2));
        rei.setCantidad(c.getInt(3));
        return rei;
    }

    /*public RecetaIngrediente getRow(long id) {
        long[] parametros = new long[] { id };
        Cursor c = bd.rawQuery(" select * from " + Contrato.TablaRecetaIngrediente.TABLA + " where " + Contrato.TablaRecetaIngrediente._ID + " = ?", parametros);
        c.moveToFirst();
        RecetaIngrediente in = getRow(c);
        c.close();
        return in;
    }*/



    public Cursor getCursor(){
        return getCursor(null, null);
    }

    public Cursor getCursor(String condicion, String[] parametros) {
        Cursor cursor = bd.query(
                Contrato.TablaRecetaIngrediente.TABLA, null, condicion, parametros, null, null, Contrato.TablaRecetaIngrediente._ID);
        return cursor;
    }
}
