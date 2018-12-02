package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {

    private PedidoDao pedidoDao;

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
        pedidoDao = MyRepository.getInstance(this).getPedidoDao();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(10000);
            //PedidoRepository rp = new PedidoRepository();
            Runnable r = new Runnable(){

                @Override
                public void run() {
                    List<Pedido> listaPedido = pedidoDao.getAll();
                    for (Pedido p:listaPedido) {
                        if(p.getEstado().equals(Pedido.Estado.ACEPTADO)){
                            p.setEstado(Pedido.Estado.EN_PREPARACION);
                            pedidoDao.update(p);
                            Intent i = new Intent();
                            i.putExtra("idPedido",p.getId());
                            i.setAction("ESTADO_EN_PREPARACION");
                            sendBroadcast(i);
                        }
                    }
                }
            };

            Thread t2 = new Thread(r);
            t2.start();


        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

}
