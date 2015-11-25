package com.example.javi.recetas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javi.recetas.ingrediente.GestorIn;
import com.example.javi.recetas.ingrediente.Ingrediente;
import com.example.javi.recetas.ingrediente.ListaIngredientes;
import com.example.javi.recetas.receta.Gestor;
import com.example.javi.recetas.receta.Receta;
import com.example.javi.recetas.receta_ingrediente.GestorReIn;
import com.example.javi.recetas.receta_ingrediente.RecetaIngrediente;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Alta extends AppCompatActivity {

    public static final int REQUEST_IMAGE_GET = 1;
    private Gestor gestor;
    private GestorIn gestorIn;
    private GestorReIn gestorReIn;
    private android.widget.EditText etNombre;
    private android.widget.Button button;
    private android.widget.EditText etId;
    private android.widget.Button btEditar;
    private android.widget.EditText etInstruccion;
    private android.widget.TextView tvTexto;
    private Button button2;
    private android.widget.ImageView imageView;

    private String rutaImagen;
    private Uri uriImagen;
    private Button button3;
    private TextView tvTextoElegidos;
    private android.widget.ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);
        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
        this.imageView = (ImageView) findViewById(R.id.ivImagen);
        this.button2 = (Button) findViewById(R.id.btBuscar);
        this.etInstruccion = (EditText) findViewById(R.id.etInstruccion);
        this.button = (Button) findViewById(R.id.button);
        this.etNombre = (EditText) findViewById(R.id.etNombre);
        gestor = new Gestor(this);
        gestorIn = new GestorIn(this);
        gestorReIn = new GestorReIn(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gestor.open();
        gestorIn.open();
        gestorReIn.open();
        //verRegistros();

        Intent i = getIntent();
        ArrayList<String> ingr =  i.getStringArrayListExtra("ingredientes");
        if (ingr!= null) {
            //Log.v("STRING ARRAY: ", ingr.toString());
            tvTextoElegidos.setText("");
            for (String in : ingr) {
                tvTextoElegidos.append(in + "\n");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gestor.close();
        gestorIn.close();
        gestorReIn.close();
    }

    public void add(View view){
        Receta re = new Receta();
        RecetaIngrediente recetaIngrediente = new RecetaIngrediente();
        re.setNombre(etNombre.getText().toString().trim());
        re.setInstruccion(etInstruccion.getText().toString().trim());


        if (!re.getNombre().isEmpty()){
            try {
                re.setFoto(rutaImagen);

                long r  = gestor.insert(re);
                //long f  = gestorReIn.insert(recetaIngrediente);
                if (r>0) {
                    Intent i = new Intent();
                    Bundle b = new Bundle();
                    b.putLong("id", r);
                    i.putExtras(b);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }else{
                    tostada(String.valueOf(R.string.noInsertar));
                }
            }catch (NullPointerException file){ // No atrapa la excepcion  !!
                tostada(String.valueOf(R.string.noImagen));
            }
        }else {
            re.setNombre("");
            re.setInstruccion("");
            re.setFoto("");
            long r  = gestor.insert(re);
            if (r>0) {
                Intent i = new Intent();
                Bundle b = new Bundle();
                b.putLong("id", r);
                i.putExtras(b);
                setResult(Activity.RESULT_OK, i);
                finish();
            }else{
                tostada(String.valueOf(R.string.noInsertar));
            }
        }
    }

    private void tostada(String i){
        Toast.makeText(this, i, Toast.LENGTH_SHORT).show();
    }

    private void verRegistros(){
        List<Receta> l = gestor.select();
        for (Receta p : l){ Log.v("RECETAS: ", p.toString()); }

        List<Ingrediente> in = gestorIn.select();
        for (Ingrediente p : in) {Log.v("INGREDIENTES: ", p.toString()); }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==REQUEST_IMAGE_GET){
            Uri uri = data.getData();
            if (uri != null){
                imageView.setImageURI(uri);
            }
            File imageFile = new File(getRealPathFromURI_API19(this, uri));
            rutaImagen = imageFile.getAbsolutePath();
            uriImagen = uri;
            //Log.v("FOTO: ", rutaImagen);
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

    public void buscarImagen(View view) {
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
        int factorEscalado = Math.max(anchoFoto/imageView.getWidth(), altoFoto/imageView.getHeight());
        opciones.inJustDecodeBounds = false;
        opciones.inSampleSize = factorEscalado;
        // opciones.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(pathImagen, opciones);
        imageView.setImageBitmap(bitmap);
    }


}
