package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;
import android.arch.persistence.room.Query;


import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

@Dao
public interface PedidoDetalleDao {

    @Query("SELECT * FROM PedidoDetalle")
    List<PedidoDetalle> getAll();

    @Query("SELECT * FROM PedidoDetalle WHERE id_pedidoDetalle = :pedidoDetalleId")
    PedidoDetalle buscarPorID(Integer pedidoDetalleId);

    @Query("SELECT * FROM PedidoDetalle WHERE id_pedido = :pedidoId")
    List<PedidoDetalle> buscarPorIDPedido(Integer pedidoId);

    @Insert
    long insert(PedidoDetalle pedidoDetalle);

    @Update
    void update(PedidoDetalle pedidoDetalle);

    @Delete
    void delete(PedidoDetalle pedidoDetalle);

}
