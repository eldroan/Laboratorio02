package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class HistorialPedidosActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView lstHistorialPedido;
    private Button bttnHistorialNuevo;
    private Button bttnHistorialMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);

        lstHistorialPedido = (ListView) findViewById(R.id.lstHistorialPedidos);
        bttnHistorialMenu = (Button) findViewById(R.id.bttnHistorialMenu);
        bttnHistorialNuevo = (Button) findViewById(R.id.bttnHistorialNuevo);
        bttnHistorialNuevo.setOnClickListener(this);
        bttnHistorialMenu.setOnClickListener(this);
        PedidoRepository pr = new PedidoRepository();

        PedidoAdapter pa = new PedidoAdapter(this,pr.getLista());
        lstHistorialPedido.setAdapter(pa);

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
