package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    private PedidoDao pedidoDao;

    @Override
    public void onReceive( Context context, Intent intent) {
        //Pedido p = (new PedidoRepository()).buscarPorId(intent.getIntExtra("idPedido", -1));

        final int idPedido = intent.getIntExtra("idPedido", -1);
        final Context finalCtx = context;
        final String actionIntent = intent.getAction();
        Runnable r = new Runnable(){
            @Override
            public void run() {
                pedidoDao = MyRepository.getInstance(finalCtx).getPedidoDao();
                Pedido p = pedidoDao.getForId(idPedido);

                SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm");
                StringBuilder strb = new StringBuilder();
                int i = 0;
                for (PedidoDetalle pd : p.getDetalle()) {
                    if (i == 2)
                        break;

                    strb.append("Se pidio " + pd.getCantidad() + " de " + pd.getProducto().getNombre() + "\n");

                    i++;
                }

                switch (actionIntent) {
                    case "ESTADO_ACEPTADO":
                        //Toast.makeText(context,"Pedido para " + p.getMailContacto() + " ha cambiado de estado a " + p.getEstado().toString(), Toast.LENGTH_LONG).show();
                        Intent toAltaProductoIntent = new Intent(finalCtx, AltaProductoActivity.class);
                        toAltaProductoIntent.putExtra("ID_PEDIDO", p.getId());
                        toAltaProductoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pi = PendingIntent.getActivity(finalCtx, 0, toAltaProductoIntent, 0);

                        NotificationCompat.Builder aceptadoNotification = new NotificationCompat.Builder(finalCtx, "CANAL01")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Tu pedido fue aceptado")
                                .setContentText("El costo será de $" + p.total().toString() + "\n Previsto el envio para " + dateformat.format(p.getFecha()))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText("El costo seá de $" + p.total().toString() + "\n Previsto el envio para " + dateformat.format(p.getFecha()) + "\n" + strb.toString()))
                                .setContentIntent(pi)
                                .setAutoCancel(true);

                        NotificationManagerCompat aceptadoNotificationManager = NotificationManagerCompat.from(finalCtx);
                        aceptadoNotificationManager.notify(99, aceptadoNotification.build());

                        //((HistorialPedidosActivity)context).pa.notifyDataSetChanged();
                        break;
                    case "ESTADO_EN_PREPARACION":
                        Intent toListaPedidosIntent = new Intent(finalCtx, HistorialPedidosActivity.class);
                        toListaPedidosIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent toListaPedidosIntentPendInt = PendingIntent.getActivity(finalCtx, 0, toListaPedidosIntent, 0);

                        NotificationCompat.Builder enPreparacionNotificacion = new NotificationCompat.Builder(finalCtx, "CANAL01")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Tu pedido se encuentra en preparación")
                                .setContentText("El costo seá de $" + p.total().toString() + "\n Previsto el envio para " + dateformat.format(p.getFecha()))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText("El costo seá de $" + p.total().toString() + "\n Previsto el envio para " + dateformat.format(p.getFecha()) + "\n" + strb.toString()))
                                .setContentIntent(toListaPedidosIntentPendInt)
                                .setAutoCancel(true);

                        NotificationManagerCompat enPreparacionNotificationManager = NotificationManagerCompat.from(finalCtx);
                        enPreparacionNotificationManager.notify(99, enPreparacionNotificacion.build());
                        break;
                    case "ESTADO_LISTO":

                        NotificationCompat.Builder listoNotificacion = new NotificationCompat.Builder(finalCtx, "CANAL01")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Tu pedido se encuentra listo")
                                .setContentText("El costo seá de $" + p.total().toString() + "\n Previsto el envio para " + dateformat.format(p.getFecha()))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText("El costo seá de $" + p.total().toString() + "\n Previsto el envio para " + dateformat.format(p.getFecha()) + "\n" + strb.toString()))
                                .setAutoCancel(true);

                        NotificationManagerCompat listoNotificationManager = NotificationManagerCompat.from(finalCtx);
                        listoNotificationManager.notify(99, listoNotificacion.build());
                        break;


                }

            }
        };
        Thread t2 = new Thread(r);
        t2.start();






    }
}
