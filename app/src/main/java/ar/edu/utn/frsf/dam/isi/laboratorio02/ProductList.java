package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class ProductList extends AppCompatActivity {

    private Spinner cmbProductosCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        ProductoRepository repo = new ProductoRepository();

        List<Categoria> categorias = repo.getCategorias();
        List<String> catString = new ArrayList<String>();
        for(Categoria c : categorias){
            catString.add(c.toString());
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(ProductList.this, android.R.layout.simple_spinner_item, catString);
        cmbProductosCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
        cmbProductosCategoria.setAdapter(adaptador);
    }

}
