package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.CategoriaRest;

public class CategoriaActivity extends AppCompatActivity implements Runnable{
    private EditText textoCat;
    private Button btnCrear;
    private Button btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        textoCat = (EditText) findViewById(R.id.txtNombreCategoria);
        btnCrear = (Button) findViewById(R.id.btnCrearCategoria);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CategoriaRest categoriaRest = new CategoriaRest();
                Thread unHilo = new Thread() {
                    @Override
                    public void run(){
                        final Categoria newCat = new Categoria();
                        newCat.setNombre(textoCat.getText().toString());
                        try {
                            categoriaRest.crearCategoria(newCat);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CategoriaActivity.this, "La categoria fué creada", Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (IllegalStateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error: No se pudo ejecutar la operacion", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                };
                unHilo.start();
                textoCat.setText("");
            }
        });
        btnMenu= (Button) findViewById(R.id.btnCategoriaVolver);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoriaActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void run() {


    }
}
