package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if(intent.getAction() == "ESTADO_ACEPTADO" ){
            Pedido p = (new PedidoRepository()).buscarPorId(intent.getIntExtra("idPedido",-1));
            Toast.makeText(context,"Pedido para " + p.getMailContacto() + " ha cambiado de estado a " + p.getEstado().toString(), Toast.LENGTH_LONG).show();
            //((HistorialPedidosActivity)context).pa.notifyDataSetChanged();
        }
    }
}
