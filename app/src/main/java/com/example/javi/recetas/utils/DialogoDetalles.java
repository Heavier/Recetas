package com.example.javi.recetas.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.example.javi.recetas.R;

public class DialogoDetalles {
    private Context context;
    private int layout;
    private AlertDialog.Builder dialogo;
    private LayoutInflater inflater;
    private View vista;
    private OnDialogoListener odl;

    public DialogoDetalles(Context context, int layout, OnDialogoListener odl){
        this.context = context;
        this.layout = layout;
        this.odl = odl;
        dialogo = new AlertDialog.Builder(context);
        inflater = LayoutInflater.from(context);
        vista = inflater.inflate(layout, null);
        dialogo.setView(vista);
        dialogo.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogoDetalles.this.odl.onOkSelected(vista);
            }
        });
    }

    public void show(){
        odl.onPreShow(vista);
        dialogo.show();
    }
}
