package com.example.javi.recetas.adaptador;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javi.recetas.R;
import com.example.javi.recetas.receta.Gestor;
import com.example.javi.recetas.receta.Receta;

import java.io.File;
import java.security.Principal;


public class Adaptador extends CursorAdapter {

    private Gestor gestor;

    public Adaptador(Context context, Cursor c, Gestor gestor) {
        super(context, c, true);
        this.gestor = gestor;//Esto cambiará
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.item, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1 = (TextView) view.findViewById(R.id.textView);
        TextView tv2 = (TextView) view.findViewById(R.id.textView2);
        ImageView imv = (ImageView) view.findViewById(R.id.ivImagen);

        Receta re = gestor.getRow(cursor);

        tv1.setText(re.getNombre());
        tv2.setText(re.getInstruccion());

        File file = new File(re.getFoto()); // try catch para foto vacía
        Uri uri = Uri.fromFile(file);
        imv.setImageURI(uri);
    }
}
