package com.example.javi.recetas.bbdd;

import android.provider.BaseColumns;

/**
 * Columnas de la tabla
 */
public class Contrato {
    public Contrato(){
    }
    public static abstract class TablaReceta implements BaseColumns{
        public static final String TABLA = "receta";
        public static final String NOMBRE = "nombre";
        public static final String FOTO = "foto";
        public static final String INTRUCCION = "instruccion";
    }

    public static abstract class TablaIngrediente implements BaseColumns{
        public static final String TABLA = "ingrediente";
        public static final String NOMBRE = "nombre";
    }

    public static abstract class TablaRecetaIngrediente implements BaseColumns{
        public static final String TABLA = "recetaingrediente";
        public static final String IDRECETA = "idreceta";
        public static final String IDINGREDENTE = "idingrediente";
        public static final String CANTIDAD = "cantidad";
    }
}
