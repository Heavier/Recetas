package com.example.javi.recetas.receta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.javi.recetas.bbdd.Ayudante;
import com.example.javi.recetas.bbdd.Contrato;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de recetas
 */
public class Gestor {
    private Ayudante abd;
    private SQLiteDatabase bd;

    public Gestor(Context c){
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

    public long insert(Receta r){
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaReceta.NOMBRE, r.getNombre());
        valores.put(Contrato.TablaReceta.FOTO, r.getFoto());
        valores.put(Contrato.TablaReceta.INTRUCCION, r.getInstruccion());
        long id = bd.insert(Contrato.TablaReceta.TABLA, null, valores);
        return id;
    }

    public int delete(Receta r){
        return delete(r.getId());
    }

    public int delete(long id){
        String condicion = Contrato.TablaReceta._ID + "=?";
        String[] argumentos = {id + ""};
        int cuenta = bd.delete(Contrato.TablaReceta.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(Receta r){
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaReceta.NOMBRE, r.getNombre());
        valores.put(Contrato.TablaReceta.FOTO, r.getFoto());
        valores.put(Contrato.TablaReceta.INTRUCCION, r.getInstruccion());
        String condicion = Contrato.TablaReceta._ID + "= ?";
        String[] argumentos = {r.getId() + ""};
        int cuenta = bd.update(Contrato.TablaReceta.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List<Receta> select(String condicion){
        List<Receta> lc = new ArrayList<>();
        Cursor cursor = bd.query(
                Contrato.TablaReceta.TABLA, null, condicion, null, null, null, null);
        cursor.moveToFirst();
        Receta ag;
        while (!cursor.isAfterLast()) {
            ag = getRow(cursor);
            lc.add(ag);
            cursor.moveToNext();
        }
        cursor.close();
        return lc;
    }

    public List<Receta> select(String condicion, String[] paramentros){
        List<Receta> la = new ArrayList<>();
        Cursor cursor = getCursor(condicion, paramentros);
        Receta p;
        while (cursor.moveToNext()){
            p = getRow(cursor);
            la.add(p);
        }
        cursor.close();
        return la;
    }

    public List<Receta> select(){
        return select(null, null);
    }


    public Receta getRow(Cursor c) {
        Receta re = new Receta();
        re.setId(c.getLong(c.getColumnIndex(Contrato.TablaReceta._ID)));
        re.setNombre(c.getString(c.getColumnIndex(Contrato.TablaReceta.NOMBRE)));
        re.setFoto(c.getString(c.getColumnIndex(Contrato.TablaReceta.FOTO)));
        re.setInstruccion(c.getString(c.getColumnIndex(Contrato.TablaReceta.INTRUCCION)));
        return re;
    }

    public Receta getRow(long id) {
        Cursor c = getCursor("_id = ?",new String[]{id+""});
        return getRow(c);
    }

    public Receta getRowPrimero(Cursor c) {
        Receta re = new Receta();
        re.setId(c.getLong(0));
        re.setNombre(c.getString(1));
        re.setFoto(c.getString(2));
        re.setInstruccion(c.getString(3));
        return re;
    }

    public Receta getRow(String nombre) {
        String[] parametros = new String[] { nombre };
        Cursor c = bd.rawQuery(" select * from " + Contrato.TablaReceta.TABLA + " where " + Contrato.TablaReceta.NOMBRE + " = ?", parametros);
        c.moveToFirst();
        Receta ag = getRow(c);
        c.close();
        return ag;
    }



    public Cursor getCursor(){
        return getCursor(null, null);
    }

    public Cursor getCursor(String condicion, String[] parametros) {
        Cursor cursor = bd.query(
                Contrato.TablaReceta.TABLA, null, condicion, parametros, null, null, Contrato.TablaReceta.NOMBRE + ", " + Contrato.TablaReceta.INTRUCCION);
        return cursor;
    }
}
