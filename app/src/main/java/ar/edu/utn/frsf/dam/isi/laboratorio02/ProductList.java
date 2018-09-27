package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.*;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ProductList extends AppCompatActivity {

    private Spinner cmbProductosCategoria;
    private TextView categoria;
    private ListView listaProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        ProductoRepository repo = new ProductoRepository();

        List<Categoria> categorias = repo.getCategorias();
        final List<String> catString = new ArrayList<String>();
        for(Categoria c : categorias){
            catString.add(c.toString());
        }
        final List<Producto> prodList = repo.getLista();
        final List<String> prodString = new ArrayList<String>();


        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(ProductList.this, android.R.layout.simple_spinner_item, catString);
        final ArrayAdapter<String> adaptadorListView = new ArrayAdapter<String>(ProductList.this, android.R.layout.simple_list_item_1, prodString);



        cmbProductosCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
        categoria = (TextView) findViewById(R.id.textView2);
        listaProductos = (ListView) findViewById(R.id.lstProductos);



        cmbProductosCategoria.setAdapter(adaptador);
        cmbProductosCategoria.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,int position, long id) {
                        prodString.clear();
                        listaProductos.setAdapter(adaptadorListView);
                        for(Producto p : prodList){
                            if(p.getCategoria().toString().equals(adapterView.getItemAtPosition(position).toString()))
                                prodString.add(p.toString());
                        }


                    }
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                }
        );
    }




}
