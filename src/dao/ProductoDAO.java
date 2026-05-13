package dao;

import java.util.List;

import model.Producto;

/**
    Interfaz DAO para la entidad Producto.
    Gestiona la carta de la pizzería: pizzas, pastas, bebidas, etc.
*/
public interface ProductoDAO {
   
    /**
        Inserta un nuevo producto en la carta.
        @param producto objeto con los datos del producto
        @return true si se insertó correctamente
    */

    boolean insertar(Producto producto);
    
    /**
        Devuelve todos los productos de la carta.
        @return lista de productos 
    */

    List<Producto> listarTodos();

    /**
        Devuelve solo los productos disponibles de una categoría.
        @param categoria 'pizza', 'pasta', 'entrante', 'postre' o 'bebida'
        @return lista de productos filtrados por categoría
    */

    List<Producto> listarPorCategoria(String categoria);

    /**
        Busca un producto por su id.
        @param id identificador del producto
        @return Producto encontrado o null si no existe
    */

    Producto buscar(int id);

    /**
        Actualiza los datos de un producto existente.
        @param producto objeto con los datos actualizados
        @return true si se actualizó correctamente
    */

    boolean actualizar(Producto producto);

    /**
        Elimina un producto de la carta.
        @param id identificador del producto a eliminar
        @return true si se eliminó correctamente
    */

    boolean eliminar(int id);

    /**
        Cambia la disponibilidad de un producto (activo/inactivo).
        @param id identificador del producto
        @param disponible true para activar, false para desactivar
        @return true si se actualizó correctamente
    */

    boolean cambiarDisponibilidad(int id, boolean disponible);

}
