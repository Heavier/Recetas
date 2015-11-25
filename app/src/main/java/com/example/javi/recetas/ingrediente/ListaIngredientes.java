package com.example.javi.recetas.ingrediente;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.javi.recetas.Principal;
import com.example.javi.recetas.R;
import com.example.javi.recetas.receta.Gestor;
import com.example.javi.recetas.receta_ingrediente.GestorReIn;
import com.example.javi.recetas.receta_ingrediente.RecetaIngrediente;

import java.util.ArrayList;

public class ListaIngredientes extends AppCompatActivity {

    private android.widget.ListView lvListaIngr;
    private Cursor cursor;
    private AdaptadorListaIngr adaptador;
    private GestorIn gestorIn;
    private Cursor c;
    private long idReceta;
    private Ingrediente ingrediente;
    private RecetaIngrediente recetaIngrediente;
    private GestorReIn gestorReIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ingredientes);
        this.lvListaIngr = (ListView) findViewById(R.id.lvListaIngr);
        gestorIn = new GestorIn(this);
        gestorReIn = new GestorReIn(this);
        Bundle b = getIntent().getExtras();
        if(b !=null ){
            b = getIntent().getExtras();
            idReceta = b.getLong("id");
            Log.v("ID", String.valueOf(idReceta));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gestorIn.open();
        gestorReIn.open();
        c = gestorIn.getCursor();
        cursor = gestorIn.getIngredientesReceta(idReceta);
        adaptador = new AdaptadorListaIngr(this, cursor, gestorIn);
        final ListView lv = (ListView)findViewById(R.id.lvListaIngr);
        lv.setAdapter(adaptador);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gestorIn.close();
        gestorReIn.close();
    }

    public void guardarRecetaIngrediente(View view) {
        // Aquí se van a guardar los inedientes el la tabla n a n con la receta usada.
        ArrayList<String> str = adaptador.getSelectedString();

        recetaIngrediente = new RecetaIngrediente();
        recetaIngrediente.setIdReceta(idReceta);
        recetaIngrediente.setCantidad(1);
        for (String nombre : str) {
            ingrediente = gestorIn.getRow(nombre);
            recetaIngrediente.setIdIngrediente(ingrediente.getId());

            long r  = gestorReIn.insert(recetaIngrediente);// update // No se sobreescriben los ingredientes.
            if (r>0) {
                Intent i = new Intent();
                Bundle b = new Bundle();
                b.putLong("id", r);
                i.putExtras(b);
                setResult(Activity.RESULT_OK, i);
                finish();
            }else{
                Log.v("ERROR ", "");
            }
            //Log.v("Ingrediente: ", ingrediente.toString());
        }

        //Log.v("INGREDIENTES ELEGIDOS: ", str.toString());
    }
}
