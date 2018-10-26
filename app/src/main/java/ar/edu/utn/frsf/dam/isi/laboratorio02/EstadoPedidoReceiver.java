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

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if(intent.getAction() == "ESTADO_ACEPTADO" ){
            Pedido p = (new PedidoRepository()).buscarPorId(intent.getIntExtra("idPedido",-1));
            //Toast.makeText(context,"Pedido para " + p.getMailContacto() + " ha cambiado de estado a " + p.getEstado().toString(), Toast.LENGTH_LONG).show();

            SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm");

            StringBuilder strb = new StringBuilder();
            int i = 0;
            for(PedidoDetalle pd : p.getDetalle()){
                if(i==2)
                    break;

                strb.append("Se pidio "+ pd.getCantidad() +" de " + pd.getProducto().getNombre() +"\n");

                i++;
            }

            Intent destino = new Intent(context,AltaProductoActivity.class);
            destino.putExtra("ID_PEDIDO",p.getId());
            destino.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pi = PendingIntent.getActivity(context,0,destino,0);

            NotificationCompat.Builder not = new NotificationCompat.Builder(context,"CANAL01")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Tu pedido fue aceptado")
                    .setContentText("El costo seá de $" + p.total().toString() + "\n Previsto el envio para " + dateformat.format(p.getFecha()))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle( new NotificationCompat.BigTextStyle().bigText("El costo seá de $" + p.total().toString() + "\n Previsto el envio para " + dateformat.format(p.getFecha()) + "\n" + strb.toString()))
                    .setContentIntent(pi)
                    .setAutoCancel(true);

            NotificationManagerCompat nm = NotificationManagerCompat.from(context);
            nm.notify(99,not.build());

            //((HistorialPedidosActivity)context).pa.notifyDataSetChanged();
        }
    }
}
