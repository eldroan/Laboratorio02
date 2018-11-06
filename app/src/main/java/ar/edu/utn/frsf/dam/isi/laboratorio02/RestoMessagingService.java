package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class RestoMessagingService extends FirebaseMessagingService {
    @Override
    public void onCreate() {
        super.onCreate();
        BroadcastReceiver br = new EstadoPedidoReceiver();
        IntentFilter filtro = new IntentFilter();

        filtro.addAction("ESTADO_LISTO");
        getApplication().getApplicationContext().registerReceiver(br,filtro);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> data = remoteMessage.getData();
        int idpedido = Integer.parseInt(data.get("ID_PEDIDO"));
        PedidoRepository  rp = new PedidoRepository();
        Pedido p = rp.buscarPorId(idpedido);
        p.setEstado(Pedido.Estado.LISTO);
        rp.guardarPedido(p);

        Intent i = new Intent();
        i.putExtra("idPedido",p.getId());
        i.setAction("ESTADO_LISTO");
        sendBroadcast(i);


    }
}
