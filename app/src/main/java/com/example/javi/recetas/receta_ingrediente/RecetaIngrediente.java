package com.example.javi.recetas.receta_ingrediente;

import android.database.Cursor;

import com.example.javi.recetas.bbdd.Contrato;

/**
 * Created by javi on 23/11/2015.
 */
public class RecetaIngrediente {
    private long id, idReceta, idIngrediente;
    private int cantidad;

    public RecetaIngrediente() {
    }

    public RecetaIngrediente(long id, long idReceta, long idIngrediente, int cantidad) {
        this.id = id;
        this.idReceta = idReceta;
        this.idIngrediente = idIngrediente;
        this.cantidad = cantidad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(long idReceta) {
        this.idReceta = idReceta;
    }

    public long getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(long idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void set(Cursor c){
        setId(c.getLong(c.getColumnIndex(Contrato.TablaRecetaIngrediente._ID)));
        setIdReceta(c.getLong(c.getColumnIndex(Contrato.TablaRecetaIngrediente.IDRECETA)));
        setIdIngrediente(c.getLong(c.getColumnIndex(Contrato.TablaRecetaIngrediente.IDINGREDENTE)));
        setCantidad(c.getInt(c.getColumnIndex(Contrato.TablaRecetaIngrediente.CANTIDAD)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecetaIngrediente that = (RecetaIngrediente) o;

        if (id != that.id) return false;
        if (idReceta != that.idReceta) return false;
        if (idIngrediente != that.idIngrediente) return false;
        return cantidad == that.cantidad;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (idReceta ^ (idReceta >>> 32));
        result = 31 * result + (int) (idIngrediente ^ (idIngrediente >>> 32));
        result = 31 * result + cantidad;
        return result;
    }

    @Override
    public String toString() {
        return "RecetaIngrediente{" +
                "id=" + id +
                ", idReceta=" + idReceta +
                ", idIngrediente=" + idIngrediente +
                ", cantidad=" + cantidad +
                '}';
    }
}
