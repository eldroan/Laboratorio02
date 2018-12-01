package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class PedidoConDetalles {
    @Embedded
    public Pedido pedido;

    @Relation(parentColumn = "id_pedido", entityColumn = "id_pedido", entity = PedidoDetalle.class)
    public List<PedidoDetalle> detalle;

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<PedidoDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<PedidoDetalle> detalle) {
        this.detalle = detalle;
    }
}
