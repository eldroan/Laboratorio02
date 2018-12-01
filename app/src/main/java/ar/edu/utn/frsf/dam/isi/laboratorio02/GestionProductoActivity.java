package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.CategoriaRest;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionProductoActivity extends AppCompatActivity {
    private Button btnMenu;
    private Button btnGuardar;
    private Spinner comboCategorias;
    private EditText nombreProducto;
    private EditText descProducto;
    private EditText precioProducto;
    private ToggleButton opcionNuevoBusqueda;
    private EditText idProductoBuscar;
    private Button btnBuscar;
    private Button btnBorrar;
    private Boolean flagActualizacion;
    //TODO private ArrayAdapter<String> comboAdapter;
    private ArrayAdapter<Categoria> comboAdapter;
    private Categoria categoriaSeleccionada;
    private Integer idBuscado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);
        flagActualizacion = false;
        opcionNuevoBusqueda = (ToggleButton) findViewById(R.id.abmProductoAltaNuevo);
        idProductoBuscar = (EditText) findViewById(R.id.abmProductoIdBuscar);
        nombreProducto = (EditText) findViewById(R.id.abmProductoNombre);
        descProducto = (EditText) findViewById(R.id.abmProductoDescripcion);
        precioProducto = (EditText) findViewById(R.id.abmProductoPrecio);
        comboCategorias = (Spinner) findViewById(R.id.abmProductoCategoria);
        btnMenu = (Button) findViewById(R.id.btnAbmProductoVolver);
        btnGuardar = (Button) findViewById(R.id.btnAbmProductoCrear);
        btnBuscar = (Button) findViewById(R.id.btnAbmProductoBuscar);
        btnBorrar= (Button) findViewById(R.id.btnAbmProductoBorrar);
        opcionNuevoBusqueda.setChecked(false);
        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
        idProductoBuscar.setEnabled(false);
        opcionNuevoBusqueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flagActualizacion =isChecked;
                btnBuscar.setEnabled(isChecked);
                btnBorrar.setEnabled(isChecked);
                idProductoBuscar.setEnabled(isChecked);
           }
       });

        //Esto es para cargar el cbox de las categorías
        Runnable r = new Runnable() {
            @Override
            public void run() {
                CategoriaRest catRest = new CategoriaRest();
                final List<Categoria> listCategorias = catRest.listarTodas();
                //final Categoria[] categorias = listCategorias.toArray(new Categoria[0]);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        comboAdapter = new ArrayAdapter<Categoria>(GestionProductoActivity.this, android.R.layout.simple_spinner_item, listCategorias);
                        comboCategorias.setAdapter(comboAdapter);
                        comboCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                categoriaSeleccionada = (Categoria) comboCategorias.getSelectedItem();
                              }
                              @Override
                              public void onNothingSelected(AdapterView<?> parent) {
                                //Hacer algo si no se selecciona
                              }
                          }
                        );
                    }
                });
            }
        };
        Thread thread = new Thread(r);
        thread.start();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto p = new Producto(
                        nombreProducto.getText().toString(),
                        descProducto.getText().toString(),
                        Double.parseDouble(precioProducto.getText().toString()),
                        categoriaSeleccionada
                        );
                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                if(opcionNuevoBusqueda.isChecked()){
                    //Actualiza
                    p.setId(idBuscado);
                    Call<Producto> updateProd= clienteRest.actualizarProducto(idBuscado,p);
                    updateProd.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this, "Producto actualizado con éxito",
                                    Toast.LENGTH_LONG).show();
                            nombreProducto.setText("");
                            descProducto.setText("");
                            precioProducto.setText("");
                            comboCategorias.setSelection(0);
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this, "Error al actualizar el producto",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else {
                    //Crea nuevo
                    Call<Producto> altaCall= clienteRest.crearProducto(p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this, "Producto creado con éxito",
                                    Toast.LENGTH_LONG).show();
                            nombreProducto.setText("");
                            descProducto.setText("");
                            precioProducto.setText("");
                            comboCategorias.setSelection(0);
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this, "Error al crear el producto",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idBuscado = Integer.parseInt(idProductoBuscar.getText().toString());
                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> getCall = clienteRest.buscarProductoPorId(idBuscado);
                getCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> resp) {
                        if(resp.code()==200 || resp.code()==201) {
                            Producto producto = resp.body();
                            nombreProducto.setText(producto.getNombre());
                            descProducto.setText(producto.getDescripcion());
                            precioProducto.setText(producto.getPrecio().toString());
                            comboCategorias.setSelection(producto.getCategoria().getId()-1);
                        }
                        else
                        {
                            Toast.makeText(GestionProductoActivity.this, "Error al buscar un producto", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Toast.makeText(GestionProductoActivity.this, "Error al buscar el producto",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> altaCall= clienteRest.borrar(idBuscado);
                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call,
                                           Response<Producto> resp) {
                        Toast.makeText(GestionProductoActivity.this, "Producto borrado con éxito",
                                Toast.LENGTH_LONG).show();
                        nombreProducto.setText("");
                        descProducto.setText("");
                        precioProducto.setText("");
                        idProductoBuscar.setText("");
                        comboCategorias.setSelection(0);
                    }
                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Toast.makeText(GestionProductoActivity.this, "Error al borrar el producto",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
