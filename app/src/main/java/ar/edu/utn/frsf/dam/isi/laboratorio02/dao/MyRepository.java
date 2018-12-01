package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

public class MyRepository {
    // variable de clase privada que almacena una instancia unica de esta entidad
    private static MyRepository _INSTANCIA_UNICA=null;

    // metodo static publico que retorna la unica instancia de esta clase
    // si no existe, cosa que ocurre la primera vez que se invoca
    // la crea, y si existe retorna la instancia existente.
    public static MyRepository getInstance(Context ctx){
        if(_INSTANCIA_UNICA==null) _INSTANCIA_UNICA = new MyRepository(ctx);
        return _INSTANCIA_UNICA;
    }

    //TODO agregar otras entidades acá
    private MyDatabase myDatabase;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;
    private PedidoDao pedidoDao;
    private PedidoDetalleDao pedidoDetalleDao;

    // constructor privado para poder implementar SINGLETON
    // al ser privado solo puede ser invocado dentro de esta clase
    // el único lugar donde se invoca es en la linea 16 de esta clase
    // y se invocará UNA Y SOLO UNA VEZ, cuando _INSTANCIA_UNICA sea null
    // luego ya no se invoca nunca más. Nos aseguramos de que haya una
    // sola instancia en toda la aplicacion
    private MyRepository(Context ctx) {
        myDatabase = Room.databaseBuilder(ctx,
                MyDatabase.class, "database-tp")
                .fallbackToDestructiveMigration()
                .build();

        //TODO agregar otras entidades acá
        categoriaDao = myDatabase.categoriaDao();
        productoDao = myDatabase.productoDao();
        pedidoDao = myDatabase.pedidoDao();
        pedidoDetalleDao = myDatabase.pedidoDetalleDao();
    }

    //TODO agregar otras entidades acá
    public CategoriaDao getCategoriaDao() {
        return categoriaDao;
    }

    public void setCategoriaDao(CategoriaDao categoriaDao) {
        this.categoriaDao = categoriaDao;
    }

    public ProductoDao getProductoDao() {
        return productoDao;
    }

    public void setProductoDao(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }

    public PedidoDao getPedidoDao() {
        return pedidoDao;
    }

    public void setPedidoDao(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }

    public PedidoDetalleDao getPedidoDetalleDao() {
        return pedidoDetalleDao;
    }

    public void setPedidoDetalleDao(PedidoDetalleDao pedidoDetalleDao) {
        this.pedidoDetalleDao = pedidoDetalleDao;
    }
}
