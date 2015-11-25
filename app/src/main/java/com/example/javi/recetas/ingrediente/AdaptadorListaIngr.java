package com.example.javi.recetas.ingrediente;


import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;

import com.example.javi.recetas.R;

import java.util.ArrayList;

public class AdaptadorListaIngr extends CursorAdapter {

    private GestorIn gestorIn;
    private ArrayList<String> selectedStrings;
    private Ingrediente in;


    public AdaptadorListaIngr(Context context, Cursor c, GestorIn gestor) {
        super(context, c, true);
        this.gestorIn = gestor;
        selectedStrings = new ArrayList<String>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.elemento_ingr, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final CheckBox ch = (CheckBox) view.findViewById(R.id.cbIngr);

        in = gestorIn.getRow(cursor);

        ch.setText(in.getNombre());

        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedStrings.add(ch.getText().toString());
                } else {
                    selectedStrings.remove(ch.getText().toString());
                }
            }
        });
    }

    public ArrayList<String> getSelectedString(){
        Log.v("PRIMER STRING: ", selectedStrings.toString());
        return selectedStrings;
    }
}
