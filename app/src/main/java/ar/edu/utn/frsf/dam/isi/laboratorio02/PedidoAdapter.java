package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PedidoAdapter extends ArrayAdapter<Pedido> {

    private Context ctx;
    private List<Pedido> listaPedidos;

    public PedidoAdapter(Context context, List<Pedido> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.listaPedidos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PedidoHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(this.ctx);
             convertView = inflater.inflate(R.layout.fila_historial,parent,false);
            holder = new PedidoHolder();

            holder.textViewContacto = (TextView) convertView.findViewById(R.id.textViewContacto);
            holder.textViewFechaEntrega = (TextView) convertView.findViewById(R.id.textViewFechaEntrega);
            holder.textViewItems = (TextView) convertView.findViewById(R.id.textViewItems);
            holder.textViewAPagar = (TextView) convertView.findViewById(R.id.textViewAPagar);
            holder.textViewEstado = (TextView) convertView.findViewById(R.id.textViewEstado);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.buttonCancelar = (Button) convertView.findViewById(R.id.buttonCancelar);
            holder.buttonCancelar.setOnClickListener(null);
            holder.buttonCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int indice = (int) v.getTag();
                    Pedido pedidoSeleccionado = listaPedidos.get(indice);
                    if(pedidoSeleccionado.getEstado().equals(Pedido.Estado.REALIZADO) || pedidoSeleccionado.getEstado().equals(Pedido.Estado.ACEPTADO) || pedidoSeleccionado.getEstado().equals(Pedido.Estado.EN_PREPARACION)){
                        pedidoSeleccionado.setEstado(Pedido.Estado.CANCELADO);
                        PedidoAdapter.this.notifyDataSetChanged();
                        return;
                    }

                }
            });
            holder.buttonDetalle = (Button) convertView.findViewById(R.id.buttonVerDetalle);
            convertView.setTag(holder);
        } else {
            holder = (PedidoHolder) convertView.getTag();
        }

        Pedido pedido = listaPedidos.get(position);

        holder.textViewContacto.setText(pedido.getMailContacto());
        holder.textViewFechaEntrega.setText(pedido.getFecha().toString());
        holder.textViewItems.setText(pedido.getDetalle().size());
        holder.textViewAPagar.setText(pedido.total().toString());
        holder.textViewEstado.setText(pedido.getEstado().toString());
        holder.imageView.setImageResource(pedido.getRetirar()? R.mipmap.retira : R.mipmap.envio);

        switch(pedido.getEstado()){
            case LISTO:
                holder.textViewEstado.setTextColor(Color.DKGRAY);
                break;
            case ENTREGADO:
                holder.textViewEstado.setTextColor(Color.BLUE);
                break;
            case CANCELADO:
            case RECHAZADO:
                holder.textViewEstado.setTextColor(Color.RED);
                break;
            case ACEPTADO:
                holder.textViewEstado.setTextColor(Color.GREEN);
                break;
            case EN_PREPARACION:
                holder.textViewEstado.setTextColor(Color.MAGENTA);
                break;
            case REALIZADO:
                holder.textViewEstado.setTextColor(Color.BLUE);
                break;

        }

        return convertView;
    }

    private static class PedidoHolder{
        public TextView textViewContacto;
        public TextView textViewFechaEntrega;
        public TextView textViewItems;
        public TextView textViewAPagar;
        public TextView textViewEstado;
        public ImageView imageView;
        public Button buttonCancelar;
        public Button buttonDetalle;

    }

}

