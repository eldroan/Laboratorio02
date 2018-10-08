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
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ProductList extends AppCompatActivity {

    private Spinner cmbProductosCategoria;
    private TextView categoria;
    private ListView listaProductos;
    private EditText quantity;
    private Button agregarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        final Intent myIntent = getIntent();
        //Bundle myBundle = myIntent.getExtras();
        int nuevopedido = myIntent.getIntExtra("NUEVO_PEDIDO",0);

        quantity = (EditText) findViewById(R.id.editTextCantidad);

        if(nuevopedido != 1){
            agregarPedido.setEnabled(false);
            quantity.setEnabled(false);
        }else{
            quantity.setFocusable(true);
            quantity.setEnabled(true);
        }


        ProductoRepository repo = new ProductoRepository();

        List<Categoria> categorias = repo.getCategorias();
        final List<String> catString = new ArrayList<String>();
        for(Categoria c : categorias){
            catString.add(c.toString());
        }
        final List<Producto> prodList = repo.getLista();
        final List<String> prodString = new ArrayList<String>();


        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(ProductList.this, android.R.layout.simple_spinner_item, catString);

        final ArrayAdapter<String> adaptadorListView = new ArrayAdapter<String>(ProductList.this, android.R.layout.simple_list_item_single_choice, prodString);



        cmbProductosCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
        categoria = (TextView) findViewById(R.id.textView2);
        listaProductos = (ListView) findViewById(R.id.lstProductos);
        listaProductos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        listaProductos.setSelection(position);
        //    }
        //});

        agregarPedido = (Button) findViewById(R.id.buttonAgregarPedido);
        final HashMap<Integer,Integer> listPosToProdId = new HashMap<Integer,Integer>();
        agregarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent returnIntent = new Intent();
                    Integer cant = Integer.parseInt(quantity.getText().toString());
//                    myIntent.putExtra("cantidad",cant);
                    returnIntent.putExtra("cantidad",cant);
                    int selectedItemId = listaProductos.getCheckedItemPosition();
                    Integer selectedProductId = listPosToProdId.get(selectedItemId);
                    if(selectedProductId == null)
                        throw new Exception("Se rompio todo");

//                    myIntent.putExtra("idProducto",selectedProductId);
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



        cmbProductosCategoria.setAdapter(adaptador);
        cmbProductosCategoria.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,int position, long id) {
                        prodString.clear();
                        listaProductos.setAdapter(adaptadorListView);
                        int index =0;
                        for(Producto p : prodList){
                            if(p.getCategoria().toString().equals(adapterView.getItemAtPosition(position).toString())){
                                prodString.add(p.toString());
                                listPosToProdId.put(index,p.getId());
                                index++;
                            }
                        }


                    }
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                }
        );
    }




}
