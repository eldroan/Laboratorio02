package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class HistorialPedidosActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView lstHistorialPedido;
    private Button bttnHistorialNuevo;
    private Button bttnHistorialMenu;
    public PedidoAdapter pa;
    private PedidoDao pedidoDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);

        lstHistorialPedido = (ListView) findViewById(R.id.lstHistorialPedidos);
        bttnHistorialMenu = (Button) findViewById(R.id.bttnHistorialMenu);
        bttnHistorialNuevo = (Button) findViewById(R.id.bttnHistorialNuevo);
        bttnHistorialNuevo.setOnClickListener(this);
        bttnHistorialMenu.setOnClickListener(this);
        pedidoDao = MyRepository.getInstance(this).getPedidoDao();

        //PedidoRepository pr = new PedidoRepository();
        final Context ctx = this;

        Runnable r = new Runnable(){
            @Override
            public void run() {
                final List<Pedido> listaPedidos = pedidoDao.getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pa = new PedidoAdapter(ctx,listaPedidos);
                        lstHistorialPedido.setAdapter(pa);
                    }
                });

            }
        };
        Thread t1 = new Thread(r);
        t1.start();

        lstHistorialPedido.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Pedido p = (Pedido) parent.getItemAtPosition(position);

                int idpedido = p.getId();

                Intent i = new Intent(HistorialPedidosActivity.this, AltaProductoActivity.class);
                i.putExtra("ID_PEDIDO",idpedido);
                startActivity(i);
                return true;
            }
        });

        BroadcastReceiver br = new EstadoPedidoReceiver();
        IntentFilter filtro = new IntentFilter();

        filtro.addAction("ESTADO_ACEPTADO");
        getApplication().getApplicationContext().registerReceiver(br,filtro);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.bttnHistorialMenu:
                i = new Intent(HistorialPedidosActivity.this, AltaProductoActivity.class);
                startActivity(i);
                break;
            case R.id.bttnHistorialNuevo:
                i = new Intent(HistorialPedidosActivity.this, MainActivity.class);
                i.putExtra("ID_PEDIDO",-1);
                startActivity(i);
                break;
        }
    }
}
