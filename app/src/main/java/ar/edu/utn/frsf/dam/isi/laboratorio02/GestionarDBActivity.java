package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDetalleDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;


public class GestionarDBActivity extends AppCompatActivity {

    private Button btnNuevaCategoria;
    private Button btnNuevoProducto;
    private Button btnNuevoPedido;
    private Button btnBorrarTodo;

    private CategoriaDao catDao;
    private ProductoDao prodDao;
    private PedidoDao pedDao;
    private PedidoDetalleDao pedDetDao;
    private TextView labelDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_db);

        labelDB = (TextView) findViewById(R.id.labelDB);

        catDao = MyRepository.getInstance(this).getCategoriaDao();
        prodDao = MyRepository.getInstance(this).getProductoDao();
        pedDao = MyRepository.getInstance(this).getPedidoDao();
        pedDetDao = MyRepository.getInstance(this).getPedidoDetalleDao();
        ActualizarLabel();

        btnNuevaCategoria = (Button) findViewById(R.id.btnNuevaCategoria);
        btnNuevaCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        //Crea la variables
                        Categoria c1 = new Categoria();
                        Random r;
                        List<Producto> listaProd = prodDao.getAll();
                        r = new Random();
                        c1.setNombre(" CATEOGORIA _ " + r.nextInt(1000));
                        catDao.insert(c1);
                        ActualizarLabel();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GestionarDBActivity.this, "Se ha creado la categoria", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };
                Thread t1 = new Thread(r1);
                t1.start();
            }
        });

        btnNuevoProducto = (Button) findViewById(R.id.btnNuevoProducto);
        btnNuevoProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        //Crea la variables
                        Producto p1 = new Producto();
                        Random r;
                        List<Producto> listaProd = prodDao.getAll();
                        List<Categoria> listaCat = catDao.getAll();

                        if(!listaCat.isEmpty()){
                            r = new Random();
                            p1.setNombre(" PRODUCTO _ " + r.nextInt(1000));
                            p1.setDescripcion(" DESCRIPCION DE P_ " + r.nextInt(1000));
                            p1.setPrecio(r.nextDouble());
                            int indexAux = r.nextInt(listaCat.size());
                            Categoria catAux = listaCat.get(indexAux);
                            p1.setCategoria(catAux);
                            prodDao.insert(p1);
                            ActualizarLabel();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionarDBActivity.this, "El producto fué creado", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionarDBActivity.this, "Debe existir al menos una categoría", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                };
                Thread t1 = new Thread(r1);
                t1.start();


            }
        });

        btnBorrarTodo = (Button) findViewById(R.id.btnBorrarTodo);
        btnBorrarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        BorrarTodo();
                    }
                };
                Thread t1 = new Thread(r1);
                t1.start();
            }
        });
    }

    private void BorrarTodo() {
        MyRepository.getInstance(this).clearAll();
    }

    private void ActualizarLabel() {
        final StringBuilder resultado = new StringBuilder();

        Runnable r1 = new Runnable() {
            @Override
            public void run() {

                List<Categoria> listaCat = catDao.getAll();
                List<Producto> listaProd = prodDao.getAll();
                List<Pedido> listaPed = pedDao.getAll();
                List<PedidoDetalle> listaPedDet = pedDetDao.getAll();

                //Agrega al StrBuilder
                resultado.append(" === CATEGORIAS ==="+ "\r\n");
                for (Categoria c : listaCat) {
                    resultado.append(c.getId() + ": " + c.getNombre() + "\r\n");
                }
                resultado.append(" === PRODUCTOS ==="+ "\r\n");
                for (Producto p : listaProd) {
                    resultado.append(p.getId() + ": " + p.getNombre() + ".DE CATEGORIA: "+ p.getCategoria().getNombre() + ".A $: "+ p.getPrecio() +  "\r\n");
                }
                resultado.append(" === PEDIDOS ==="+ "\r\n");
                for (Pedido pp : listaPed) {
                    resultado.append("Pedido:" + pp.getId() + " en estado " + pp.getEstado().toString() + "\r\n");
                }
                resultado.append(" === PEDDETALLES ==="+ "\r\n");
                for (PedidoDetalle ppdd : listaPedDet) {
                    resultado.append(ppdd.getId() + ": " + ppdd.getPedido().getId() + "compró" +  ppdd.getCantidad() + " de " + ppdd.getProducto().getNombre() + "\r\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        labelDB.setText(resultado.toString());
                    }
                });
            }
        };
        Thread t1 = new Thread(r1);
        t1.start();


    }
}
