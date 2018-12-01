package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class MainActivity extends AppCompatActivity {

    private Button btnNuevoPedido;
    private Button btnHistorial;
    private Button btnListaProductos;
    private Button btnPrepararPedidos;
    private Button btnConfiguracion;
    private Button btnCategorias;
    private Button btnGestionarProductos;

    private Button btnPruebaDBLocal;
    private CategoriaDao catDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        catDao = MyRepository.getInstance(this).getCategoriaDao();


        btnNuevoPedido = (Button) findViewById(R.id.btnMainNuevoPedido);
        btnNuevoPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AltaProductoActivity.class);
                i.putExtra("ID_PEDIDO",-1);
                startActivity(i);
            }
        });
        
        btnHistorial = (Button) findViewById(R.id.btnHistorialPedidos);
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,HistorialPedidosActivity.class);
                startActivity(i);
            }
        });

        btnListaProductos = (Button) findViewById(R.id.btnListaProductos);
        btnListaProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ProductList.class);
                i.putExtra("NUEVO_PEDIDO",0);
                startActivity(i);
            }
        });

        btnPrepararPedidos = (Button) findViewById(R.id.btnPrepararPedidos);
        btnPrepararPedidos.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {

                                                      Intent nuevoServicio = new Intent(MainActivity.this, PrepararPedidoService.class);
                                                      startService(nuevoServicio);
                                                  }
                                              }
        );

        btnConfiguracion = (Button) findViewById(R.id.btnConfiguracion);
        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent i = new Intent(MainActivity.this, ConfiguracionActivity.class);
                                                    startActivity(i);
                                                }
                                            }
        );

        btnCategorias = (Button) findViewById(R.id.btnCategorias);
        btnCategorias.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent i = new Intent(MainActivity.this, CategoriaActivity.class);
                                                    startActivity(i);
                                                }
                                            }
        );

        btnGestionarProductos = (Button) findViewById(R.id.btnGestionProductos);
        btnGestionarProductos.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent i = new Intent(MainActivity.this, GestionProductoActivity.class);
                                                 startActivity(i);
                                             }
                                         }
        );

        btnPruebaDBLocal = (Button) findViewById(R.id.btnPruebaDBLocal);
        btnPruebaDBLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        //Inserta una categoria
                        Categoria c1 = new Categoria();
                        Random r = new Random();
                        c1.setNombre(" CAT _ " + r.nextInt(1000));
                        catDao.insert(c1);
                        //Muestra la lista en un toast
                        List<Categoria> lista = catDao.getAll();
                        final StringBuilder resultado = new StringBuilder(" === CATEGORIAS ==="+ "\r\n");
                        for (Categoria c : lista) {
                            resultado.append(c.getId() + ": " + c.getNombre() + "\r\n");
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,resultado.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };
                Thread t1 = new Thread(r1);
                t1.start();





            }
        });
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.canal_estado_nombre);
            String description = getString(R.string.canal_estado_descripcion);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CANAL01",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
