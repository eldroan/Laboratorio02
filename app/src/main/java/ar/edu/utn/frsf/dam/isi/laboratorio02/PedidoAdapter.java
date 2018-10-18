package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
            View fila = inflater.inflate(R.layout.fila_historial,parent,false);
            holder = new PedidoHolder();

            holder.textViewContacto = (TextView) convertView.findViewById(R.id.textViewContacto);
            holder.textViewFechaEntrega = (TextView) convertView.findViewById(R.id.textViewFechaEntrega);
            holder.textViewItems = (TextView) convertView.findViewById(R.id.textViewItems);
            holder.textViewAPagar = (TextView) convertView.findViewById(R.id.textViewAPagar);
            holder.textViewEstado = (TextView) convertView.findViewById(R.id.textViewEstado);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.buttonCancelar = (Button) convertView.findViewById(R.id.buttonCancelar);
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
        holder.imageView.setImageResource(pedido.getRetirar()? R.mipmap.imageRetira : R.mipmap.imageEnvio);

        return super.getView(position, convertView, parent);
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

