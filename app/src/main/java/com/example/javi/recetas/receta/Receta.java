package com.example.javi.recetas.receta;


import android.database.Cursor;

import com.example.javi.recetas.bbdd.Contrato;

public class Receta {
    private long id;
    private String nombre;
    private String foto;
    private String instruccion;

    public Receta() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(String instruccion) {
        this.instruccion = instruccion;
    }

    public Receta(long id, String nombre, String foto, String instruccion) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.instruccion = instruccion;
    }

    public void set(Cursor c) {
        setId(c.getLong(c.getColumnIndex(Contrato.TablaReceta._ID)));
        setNombre(c.getString(c.getColumnIndex(Contrato.TablaReceta.NOMBRE)));
        setFoto(c.getString(c.getColumnIndex(Contrato.TablaReceta.FOTO)));
        setInstruccion(c.getString(c.getColumnIndex(Contrato.TablaReceta.INTRUCCION)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receta receta = (Receta) o;

        return id == receta.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", foto='" + foto + '\'' +
                ", instruccion='" + instruccion + '\'' +
                '}';
    }
}
