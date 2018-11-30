package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.*;
import android.view.View;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.CategoriaRest;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ProductList extends AppCompatActivity {

    private Spinner cmbProductosCategoria;
    private TextView categoria;
    private ListView listaProductos;
    private EditText quantity;
    private Button agregarPedido;
    ArrayAdapter<String> categoriasAdapter;
    ArrayAdapter<String> productosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        final Intent myIntent = getIntent();
        final int nuevopedido = myIntent.getIntExtra("NUEVO_PEDIDO",0);
        quantity = (EditText) findViewById(R.id.editTextCantidad);
        agregarPedido = (Button) findViewById(R.id.buttonAgregarPedido);
        cmbProductosCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
        categoria = (TextView) findViewById(R.id.textView2);
        listaProductos = (ListView) findViewById(R.id.lstProductos);
        listaProductos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Runnable r = new Runnable() {
            @Override
            public void run() {

                ProductoRepository repoProductos = new ProductoRepository();
                CategoriaRest catRest = new CategoriaRest();
                Categoria[] categorias = catRest.listarTodas().toArray(new Categoria[0]);
                final List<String> catString = new ArrayList<String>();
                for(Categoria c : categorias){
                    catString.add(c.toString());
                }
                final List<Producto> prodList = repoProductos.getLista();
                final List<String> prodString = new ArrayList<String>();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        categoriasAdapter = new ArrayAdapter<String>(ProductList.this, android.R.layout.simple_spinner_item, catString);
                        productosAdapter = new ArrayAdapter<String>(ProductList.this, android.R.layout.simple_list_item_single_choice, prodString);

                        final HashMap<Integer,Integer> listPosToProdId = new HashMap<Integer,Integer>();
                        agregarPedido.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    Intent returnIntent = new Intent();
                                    Integer cant = Integer.parseInt(quantity.getText().toString());
                                    returnIntent.putExtra("cantidad",cant);
                                    int selectedItemId = listaProductos.getCheckedItemPosition();
                                    Integer selectedProductId = listPosToProdId.get(selectedItemId);
                                    if(selectedProductId == null)
                                        throw new Exception("Se rompio todo");

                                    returnIntent.putExtra("idProducto",selectedProductId);
                                    setResult(Activity.RESULT_OK,returnIntent);
                                    finish();
                                    Log.println(Log.WARN,"LOCO","MENSAJE " +myIntent.getExtras().toString());
                                    // Toast.makeText(ProductList.this,myIntent.getExtras().toString(),Toast.LENGTH_SHORT).show();

                                }catch(Exception ex){
                                    Log.println(Log.ERROR,"LOCO","MENSAJE " );
                                    Toast.makeText(ProductList.this,"Que toast re loco",Toast.LENGTH_SHORT).show();
                                    setResult(Activity.RESULT_CANCELED);
                                    finish();
                                }


                            }
                        });

                        cmbProductosCategoria.setAdapter(categoriasAdapter);
                        cmbProductosCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                prodString.clear();
                                listaProductos.setAdapter(productosAdapter);
                                int index =0;
                                for(Producto p : prodList){
                                    if(p.getCategoria().toString().equals(adapterView.getItemAtPosition(position).toString())){
                                        prodString.add(p.toString());
                                        listPosToProdId.put(index,p.getId());
                                        index++;
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                        );
                    }
                });
            }
        };
        Thread thread = new Thread(r);
        thread.start();

        if(nuevopedido != 1){
            agregarPedido.setEnabled(false);
            quantity.setEnabled(false);
        }else{
            quantity.setFocusable(true);
            quantity.setEnabled(true);
        }

    }




}
