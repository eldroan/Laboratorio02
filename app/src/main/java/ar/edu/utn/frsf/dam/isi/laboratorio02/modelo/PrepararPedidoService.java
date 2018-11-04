package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.EstadoPedidoReceiver;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;

public class PrepararPedidoService extends IntentService {
    public PrepararPedidoService() {
        super("PrepararPedidoService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*Esto es para agregar al broadcast la intención de caputar la acción
        ESTADO_ACEPTADO */
        BroadcastReceiver br = new EstadoPedidoReceiver();
        IntentFilter filtro = new IntentFilter();
        filtro.addAction("ESTADO_EN_PREPARACION");
        getApplication().getApplicationContext().registerReceiver(br,filtro);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(20000);
            PedidoRepository rp = new PedidoRepository();
            List<Pedido> listaPedido = rp.getLista();
            for (Pedido p:listaPedido) {
                if(p.getEstado().equals(Pedido.Estado.ACEPTADO)){
                    p.setEstado(Pedido.Estado.EN_PREPARACION);
                    Intent i = new Intent();
                    i.putExtra("idPedido",p.getId());
                    i.setAction("ESTADO_ACEPTADO");
                    sendBroadcast(i);
                }
            }

        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

}
