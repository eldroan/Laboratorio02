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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
        int idPedidoIntent = getIntent().getIntExtra("ID_PEDIDO",-1);
        if(idPedidoIntent == -1){
            unPedido = new Pedido();
            totalDePedido = 0f;
        }else{
            unPedido = repositorioPedido.buscarPorId(idPedidoIntent);
            totalDePedido = unPedido.total().floatValue();
            editMail.setText(unPedido.getMailContacto());
            editDireccion.setText(unPedido.getDireccionEnvio());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            editHora.setText(sdf.format(unPedido.getFecha()));
            rBttnEnvioDomicilio.setChecked(!unPedido.getRetirar());
            rBttnRetiraLocal.setChecked(unPedido.getRetirar());

        }


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
        int index =0;
        for(PedidoDetalle pd: unPedido.getDetalle()){

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
        bttnHacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hora =0;
                int minutos =0;
                boolean error = false;
                StringBuilder errorSB = new StringBuilder();

                if(editMail.getText().length() == 0){
                    error = true;
                    errorSB.append("El campo de email no puede ser vacio \n");
                }

                if(rBttnEnvioDomicilio.isChecked() && editDireccion.getText().length() == 0){
                    error = true;
                    errorSB.append("La dirección no puede ser vacia para un envio a domicilio\n");
                }

                if(editHora.getText().length() ==0){
                    error = true;
                    errorSB.append("El campo de hora no puede ser vacio \n");
                }else{
                    try{
                        String[] splittedHora = editHora.getText().toString().split(":");
                        hora = Integer.parseInt(splittedHora[0]);
                        minutos = Integer.parseInt(splittedHora[1]);
                        if(hora >23 || hora <0){
                            error = true;
                            errorSB.append("La hora no puede ser mayor a 23 o menor que 0");
                        }
                        if(minutos > 59 || minutos < 0){
                            error = true;
                            errorSB.append("Los minutos no pueden ser mayor a 59 o menores que 0");
                        }
                    }catch(Exception ex){
                        error = true;
                        errorSB.append("La hora debe escribirse en el formato hh:mm");
                    }
                }

                if(listPosToProdId.size() == 0){
                    error = true;
                    errorSB.append("Debe haber almenos un producto agregado para hacer un pedido");
                }

                if(error){
                    Toast.makeText(AltaProductoActivity.this,errorSB.toString(),Toast.LENGTH_LONG).show();
                }else{
                    unPedido.setMailContacto(editMail.getText().toString());
                    if(rBttnEnvioDomicilio.isChecked()){
                        unPedido.setDireccionEnvio(editDireccion.getText().toString());
                        unPedido.setRetirar(false);
                    }else{
                        unPedido.setDireccionEnvio("");
                        unPedido.setRetirar(true);
                    }
                    GregorianCalendar h = new GregorianCalendar();
                    h.set(Calendar.HOUR_OF_DAY,hora);
                    h.set(Calendar.MINUTE,minutos);
                    h.set(Calendar.SECOND,0);
                    unPedido.setFecha(h.getTime());
                    unPedido.setEstado(Pedido.Estado.REALIZADO);
                    repositorioPedido.guardarPedido(unPedido);

                    //ACA PASAR A LA ACTIVAD "HISTORIAL DE PEDIDO" (PASO SIGUIENTE)
                    Intent i = new Intent(AltaProductoActivity.this,HistorialPedidosActivity.class);
                    startActivity(i);

                }
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
