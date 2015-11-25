package com.example.javi.recetas.ingrediente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.javi.recetas.bbdd.Ayudante;
import com.example.javi.recetas.bbdd.Contrato;
import com.example.javi.recetas.receta.Receta;

import java.util.ArrayList;
import java.util.List;


public class GestorIn {
    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorIn(Context c){
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
    public long insert(Ingrediente in){
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaIngrediente.NOMBRE, in.getNombre());
        long id = bd.insert(Contrato.TablaIngrediente.TABLA, null, valores);
        return id;
    }

    public int delete(Ingrediente in){
        return delete(in.getId());
    }

    public int delete(long id){
        String condicion = Contrato.TablaIngrediente._ID + "=?";
        String[] argumentos = {id + ""};
        int cuenta = bd.delete(Contrato.TablaIngrediente.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(Ingrediente in){
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaIngrediente.NOMBRE, in.getNombre());
        String condicion = Contrato.TablaIngrediente._ID + "= ?";
        String[] argumentos = {in.getId() + ""};
        int cuenta = bd.update(Contrato.TablaIngrediente.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List<Ingrediente> select(String condicion){
        List<Ingrediente> lc = new ArrayList<>();
        Cursor cursor = bd.query(
                Contrato.TablaIngrediente.TABLA, null, condicion, null, null, null, null);
        cursor.moveToFirst();
        Ingrediente in;
        while (!cursor.isAfterLast()) {
            in = getRow(cursor);
            lc.add(in);
            cursor.moveToNext();
        }
        cursor.close();
        return lc;
    }

    public Cursor getIngredientesReceta(long id){
        String sql = "select  i.*, ri.* " +
                "from ingrediente i " +
                "left join (select * from recetaingrediente ri where idreceta= ? ) ri " +
                "on ri.idingrediente = i._id " +
                "order by i.nombre";
        String[] parametros = new String[]{id+""};
        return bd.rawQuery(sql, parametros);
    }

    public ArrayList<String> getMisIngredientes(long id){
        String sql = "select  i.*, ri.* " +
                "from ingrediente i " +
                "left join recetaingrediente ri " +
                "on ri.idingrediente = i._id " +
                "where idreceta= ? " +
                "order by i.nombre";
        String[] parametros = new String[]{id+""};
        Cursor cursor = bd.rawQuery(sql, parametros);
        ArrayList<String> str = new ArrayList<>();
        Ingrediente in;
        while(cursor.moveToNext()){
            in = getRow(cursor);
            str.add(in.getNombre());
        }
        return str;
    }

    public List<Ingrediente> select(String condicion, String[] parametros){
        List<Ingrediente> la = new ArrayList<>();
        Cursor cursor = getCursor(condicion, parametros);
        Ingrediente p;
        while (cursor.moveToNext()){
            p = getRow(cursor);
            la.add(p);
        }
        cursor.close();
        return la;
    }

    public List<Ingrediente> select(){
        return select(null, null);
    }


    public Ingrediente getRow(Cursor c) {
        Ingrediente in = new Ingrediente();
        in.setId(c.getLong(c.getColumnIndex(Contrato.TablaIngrediente._ID)));
        in.setNombre(c.getString(c.getColumnIndex(Contrato.TablaIngrediente.NOMBRE)));
        return in;
    }

    public Ingrediente getRow(long id) {
        Cursor c = getCursor("_id = ?",new String[]{id+""});
        return getRow(c);
    }

    public Ingrediente getRowPrimero(Cursor c) {
        Ingrediente in = new Ingrediente();
        in.setId(c.getLong(0));
        in.setNombre(c.getString(1));
        return in;
    }

    public Ingrediente getRow(String nombre) {
        String[] parametros = new String[] { nombre };
        Cursor c = bd.rawQuery(" select * from " + Contrato.TablaIngrediente.TABLA + " where " + Contrato.TablaIngrediente.NOMBRE + " = ?", parametros);
        c.moveToFirst();
        Ingrediente in = getRow(c);
        c.close();
        return in;
    }



    public Cursor getCursor(){
        return getCursor(null, null);
    }

    public Cursor getCursor(String condicion, String[] parametros) {
        Cursor cursor = bd.query(
                Contrato.TablaIngrediente.TABLA, null, condicion, parametros, null, null, Contrato.TablaIngrediente.NOMBRE);
        return cursor;
    }
}
