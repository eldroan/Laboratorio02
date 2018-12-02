package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;
import android.arch.persistence.room.Query;


import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

@Dao
public interface PedidoDao {

    @Query("SELECT * FROM Pedido")
    List<Pedido> getAll();

    @Query("SELECT * FROM Pedido WHERE id_pedido = :id")
    Pedido getForId(Integer id);

    @Insert
    long insert(Pedido pedido);

    @Update
    void update(Pedido pedido);

    @Delete
    void delete(Pedido pedido);
}
