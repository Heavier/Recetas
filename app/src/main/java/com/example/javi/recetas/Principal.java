package com.example.javi.recetas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javi.recetas.adaptador.Adaptador;
import com.example.javi.recetas.bbdd.Contrato;
import com.example.javi.recetas.ingrediente.GestorIn;
import com.example.javi.recetas.ingrediente.Ingrediente;
import com.example.javi.recetas.ingrediente.ListaIngredientes;
import com.example.javi.recetas.receta.Gestor;
import com.example.javi.recetas.receta.Receta;
import com.example.javi.recetas.utils.Dialogo;
import com.example.javi.recetas.utils.DialogoDetalles;
import com.example.javi.recetas.utils.OnDialogoListener;

import java.io.File;

public class Principal extends AppCompatActivity {

    public static final int REQUEST_IMAGE_GET = 1;
    private Gestor gestor;
    private GestorIn gestorIn;
    private Adaptador adaptador;
    private Cursor c;
    private final static int ALTA = 1;
    private Ingrediente in;
    private Uri uriImagen;
    private ListView lvRecetas;
    private ImageView imageView;
    private String rutaImagen;

    // ------------------------------------------------------------------------------------- //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.lvRecetas = (ListView) findViewById(R.id.lvRecetas);
        gestor = new Gestor(this);
        gestorIn = new GestorIn(this);
        in = new Ingrediente();
        init();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nuevo_ingrediente:
                addIngrediente();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gestor.open();
        gestorIn.open();
        c = gestor.getCursor();
        adaptador = new Adaptador(this, c, gestor);
        lvRecetas.setAdapter(adaptador);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gestor.close();
        gestorIn.close();
    }

    // ------------------------------------------------------------------------------------- //
    public void init(){

        LayoutInflater i = LayoutInflater.from(getApplicationContext());
        View v = i.inflate(R.layout.activity_alta, null);

        this.imageView = (ImageView) v.findViewById(R.id.ivImagen);

        lvRecetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Receta r = new Receta();
                r.set(c);
                final OnDialogoListener odl = new OnDialogoListener() {

                    @Override
                    public void onPreShow(View v) {
                        TextView titulo = (TextView) v.findViewById(R.id.tvTituloDetalle);
                        ImageView imagen = (ImageView) v.findViewById(R.id.ivImagenDetalle);
                        TextView instrucciones = (TextView) v.findViewById(R.id.tvInstruccionesDetalle);
                        TextView ingredientes = (TextView) v.findViewById(R.id.tvIngredientesDetalle);

                        titulo.setText(r.getNombre());

                        File file = new File(r.getFoto());
                        Uri uri = Uri.fromFile(file);
                        imagen.setImageURI(uri);

                        instrucciones.setText(r.getInstruccion());
                        ingredientes.setText("");
                        for (String linea : gestorIn.getMisIngredientes(r.getId())) {
                            ingredientes.append(linea + "\n");
                        }
                    }

                    @Override
                    public void onOkSelected(View v) {
                    }

                    @Override
                    public void onDeleteSelected(View v) {
                    }
                };
                DialogoDetalles d = new DialogoDetalles(Principal.this, R.layout.detalles, odl);
                d.show();
            }
        });


        lvRecetas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                final Receta r = new Receta();
                r.set(c);
                final OnDialogoListener odl = new OnDialogoListener() {
                    @Override
                    public void onPreShow(View v) {
                        Button b = (Button) v.findViewById(R.id.button);
                        Button c = (Button) v.findViewById(R.id.btBuscar);
                        Button d = (Button) v.findViewById(R.id.btElegir);
                        Button cambiarImagen = (Button) v.findViewById(R.id.btCambiarImagen);
                        b.setVisibility(View.GONE);
                        c.setVisibility(View.GONE);
                        d.setVisibility(View.VISIBLE);
                        cambiarImagen.setVisibility(View.GONE); // Hacer visible para activar la opción de cambiar de imagen

                        EditText etNombre = (EditText) v.findViewById(R.id.etNombre);
                        EditText etInstruccion = (EditText) v.findViewById(R.id.etInstruccion);
                        ImageView imageView = (ImageView) v.findViewById(R.id.ivImagen);
                        etNombre.setText(r.getNombre());
                        etInstruccion.setText(r.getInstruccion());

                        File file = new File(r.getFoto());
                        Uri uri = Uri.fromFile(file);
                        imageView.setImageURI(uri);

                        rutaImagen = r.getFoto();
                    }

                    @Override
                    public void onOkSelected(View v) {
                        tostada("Datos insertados.");
                        EditText etNombre = (EditText) v.findViewById(R.id.etNombre);
                        EditText etInstruccion = (EditText) v.findViewById(R.id.etInstruccion);
                        r.setNombre(etNombre.getText().toString());
                        r.setInstruccion(etInstruccion.getText().toString());
                        r.setFoto(rutaImagen);
                        int n;
                        n = gestor.update(r);
                        c = gestor.getCursor();
                        adaptador.changeCursor(c);
                    }

                    @Override
                    public void onDeleteSelected(View v) {
                        tostada("Eliminado.");
                        c = gestor.getCursor();
                        gestor.delete(id);
                        adaptador.changeCursor(c);
                    }
                };
                Dialogo d = new Dialogo(Principal.this, R.layout.activity_alta, odl);
                d.show();
                return true;
            }
        });
    }
    // ------------------------------------------------------------------------------------- //

    public void insertar(View view) {
        Intent i = new Intent(this, Alta.class);
        startActivityForResult(i, ALTA);
    }

    private void tostada(String i){
        Toast.makeText(this, i, Toast.LENGTH_SHORT).show();
    }


    public void addIngrediente() {
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("Nuevo ingrediente");
        LayoutInflater inflater= LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.nuevo_ingrediente, null);
        alert.setView(vista);
        alert.setPositiveButton(R.string.guardar,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText nombre = (EditText) vista.findViewById(R.id.etNombreIngrediente);
                        String str = nombre.getText().toString();
                        in.setNombre(str);
                        gestorIn.insert(in);
                    }
                });
        alert.setNegativeButton(R.string.cancelar, null);
        alert.show();
    }

    public void elegirIngredientes(View view) {
        Intent i = new Intent(this, ListaIngredientes.class);
        Bundle b = new Bundle();
        b.putLong("id", c.getLong(c.getColumnIndex(Contrato.TablaReceta._ID)));
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==REQUEST_IMAGE_GET){
            Uri uri = data.getData();
            if (uri != null){
                imageView.setImageURI(uri);
            }
            try{
                File imageFile = new File(getRealPathFromURI_API19(this, uri));
                rutaImagen = imageFile.getAbsolutePath();
                uriImagen = uri;
            }catch (NullPointerException e){
            }
        }
    }

    // Método para obtener el nombre de la imagen. Sólo disponible a partir de la api 19
    // Detalles en http://stackoverflow.com/questions/2789276/android-get-real-path-by-uri-getpath
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);
        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };
        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public void cambiarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
        String pathImagen = rutaImagen;
        BitmapFactory.Options opciones = new BitmapFactory.Options();
        opciones.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathImagen, opciones);
        int anchoFoto = opciones.outWidth;
        int altoFoto = opciones.outHeight;
        // int factorEscalado = 2 * MainActivity.factorDeEscalado(anchoFoto, altoFoto, imageView.getWidth(), imageView.getHeight());
        int factorEscalado = Math.max(anchoFoto/imageView.getWidth(), altoFoto/imageView.getHeight()); // ArithmeticException: divide by zero
        opciones.inJustDecodeBounds = false;
        opciones.inSampleSize = factorEscalado;
        // opciones.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(pathImagen, opciones);
        imageView.setImageBitmap(bitmap);
    }
    //----------------------------------------------------
}
