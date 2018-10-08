package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class AltaProductoActivity extends AppCompatActivity {

    private EditText editMail;
    private EditText editDireccion;
    private RadioButton rBttnEnvioDomicilio;
    private RadioButton rBttnRetiraLocal;
    private EditText editHora;
    private ListView lViewPedido;
    private Button bttnAgregarProducto;
    private Button bttnQuitarProducto;
    private Button bttnHacerPedido;
    private Button bttnVolver;
    private TextView lblTotalPedido;

    private Pedido unPedido;
    private PedidoRepository repositorioPedido;
    private ProductoRepository repositorioProducto;

    private int selectedItemInList = -1;
    private ArrayList<String> pedidosEnDetalle;
    private ArrayAdapter<String> adaptadorLViewPedido;
    private HashMap<Integer,Integer> listPosToProdId;
    private Float totalDePedido;
    private String lblTotalPedidoOriginal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_producto);

        editMail = (EditText) findViewById(R.id.editEmail);
        editDireccion = (EditText) findViewById(R.id.editDireccion);
        editHora = (EditText) findViewById(R.id.editHora);
        rBttnEnvioDomicilio = (RadioButton) findViewById(R.id.rBttnEnviaDomicilio);
        rBttnRetiraLocal = (RadioButton) findViewById(R.id.rBttnRetiraLocal);
        lViewPedido = (ListView) findViewById(R.id.lViewPedido);
        bttnAgregarProducto = (Button) findViewById(R.id.bttnAgregarProducto);
        bttnQuitarProducto = (Button) findViewById(R.id.bttnQuitarProducto);
        bttnHacerPedido = (Button) findViewById(R.id.bttnHacerPedido);
        bttnVolver = (Button) findViewById(R.id.bttnVolver);
        lblTotalPedido = (TextView) findViewById(R.id.lblTotalPedido);
        lblTotalPedidoOriginal = lblTotalPedido.getText().toString();

        //Inicializamos las variables del modelo
        repositorioProducto = new ProductoRepository();
        repositorioPedido = new PedidoRepository();

        unPedido = new Pedido();
        totalDePedido = 0f;

        lblTotalPedido.setText(lblTotalPedidoOriginal +" "+ totalDePedido.toString());


        //Desabilitamos/Habilitamos el edittext de direccion segun el estado del rbtton de envio a domicilio
        editDireccion.setEnabled(false);

        rBttnRetiraLocal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editDireccion.setText("");
                    editDireccion.setEnabled(false);
                }


            }
        });
        rBttnEnvioDomicilio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    editDireccion.setEnabled(true);

                }

            }
        });

            //Crear el adaptador para el listView de productos.
        pedidosEnDetalle = new ArrayList<String>();
        listPosToProdId = new HashMap<Integer,Integer>();
        for(PedidoDetalle pd: unPedido.getDetalle()){
            int index =0;
            pedidosEnDetalle.add(pd.getProducto().getNombre() + " ($" + pd.getProducto().getPrecio() + ") "+ pd.getCantidad());
            listPosToProdId.put(index,pd.getProducto().getId());
            index++;
        }
        Log.d("TAMANO","El tamaño fue " + pedidosEnDetalle.size());
//        final ArrayList<String> asd = new ArrayList<String>();
//        asd.add("uno");
//        asd.add("dos");
//        asd.add("tres");
        adaptadorLViewPedido = new ArrayAdapter<String>(AltaProductoActivity.this, android.R.layout.simple_list_item_single_choice, pedidosEnDetalle);

        lViewPedido.setAdapter(adaptadorLViewPedido);
        lViewPedido.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        bttnQuitarProducto.setEnabled(false);

        lViewPedido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bttnQuitarProducto.setEnabled(true);
                selectedItemInList = position;

            }
        });
        bttnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AltaProductoActivity.this,ProductList.class);
                i.putExtra("NUEVO_PEDIDO",1);
                startActivityForResult(i,0);
            }
        });
        bttnQuitarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(AltaProductoActivity.this,"Apretado",Toast.LENGTH_LONG).show();
                if(selectedItemInList != -1 ) {

                    try{
                        List<PedidoDetalle> pdl = unPedido.getDetalle();
                        ListIterator<PedidoDetalle> lit = pdl.listIterator();
                        PedidoDetalle pd = null;
                        while(lit.hasNext() ){
                            PedidoDetalle actual = lit.next();
                            if(actual.getProducto().getId() == listPosToProdId.get(selectedItemInList)){
                                pd = actual;
                                break;
                            }
                        }

                        if(pd != null){
                            totalDePedido -= pd.getCantidad() * pd.getProducto().getPrecio().floatValue();
                            lblTotalPedido.setText(lblTotalPedidoOriginal +" "+ totalDePedido.toString());
                            pdl.remove(pd);
                            unPedido.setDetalle(pdl);
                        }else{
                            Toast.makeText(AltaProductoActivity.this    ,"No se encontro el pedido a eliminar",Toast.LENGTH_SHORT);
                        }

                        adaptadorLViewPedido.remove(pedidosEnDetalle.get(selectedItemInList));

                        for (int i = selectedItemInList; i < listPosToProdId.size() - 1; i++) {
                            listPosToProdId.replace(i, listPosToProdId.get(i + 1));
                        }

                        listPosToProdId.remove(listPosToProdId.size() - 1);


                    }catch  (IndexOutOfBoundsException ex){

                    }


                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i < listPosToProdId.size(); i++){
                        sb.append("Key:" + i + " Value" + listPosToProdId.get(i) + " Nombre: " +repositorioProducto.buscarPorId(listPosToProdId.get(i)).getNombre() +", ");
                    }
                    Toast.makeText(AltaProductoActivity.this,"Lista: " + sb.toString() ,Toast.LENGTH_LONG).show();

                    //Para evitar que nos queden errores de redondeo 
                    if(listPosToProdId.size() == 0){
                        totalDePedido = 0f;
                        lblTotalPedido.setText(lblTotalPedidoOriginal +" "+ totalDePedido.toString());
                    }
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 0){
                if(data != null){
                    int cantidad = data.getIntExtra("cantidad",-1);
                    int idProducto = data.getIntExtra("idProducto",-1);

                    if(cantidad != -1 && idProducto != -1){
                        Producto nuevoProducto = repositorioProducto.buscarPorId(idProducto);
                        PedidoDetalle nuevoDetalle = new PedidoDetalle(cantidad,nuevoProducto);

                        unPedido.agregarDetalle(nuevoDetalle);
                        pedidosEnDetalle.add(nuevoProducto.getNombre() + " ($" + nuevoProducto.getPrecio() + ") "+ cantidad);
                        adaptadorLViewPedido.notifyDataSetChanged();
                        listPosToProdId.put(listPosToProdId.size(),idProducto);
                        double precio = nuevoProducto.getPrecio();
                        totalDePedido += cantidad * ((float) precio);
                        lblTotalPedido.setText(lblTotalPedidoOriginal +" "+ totalDePedido.toString());

                    }

                }
            }else{
                //Llego otro request, re loco
            }
        }
    }
}
