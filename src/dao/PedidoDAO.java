package dao;

import java.util.List;

import model.LineaPedido;
import model.Pedido;

/**
    Interfaz DAO para la entidad Pedido y sus líneas.
    Gestiona la relación N:M entre clientes y productos.
*/

public interface PedidoDAO {
    
    /**
        Inserta un pedido completo: cabecera + todas sus líneas en una sola transacción.
        Si falla cualquier inserción se hace rollback de todo.
        @param pedido      cabecera del pedido
        @param lineas      lista de líneas del pedido
        @return true si se insertó correctamente
    */

    boolean insertar(Pedido pedido, List<LineaPedido> lineas);

    /**
        Devuelve todos los pedidos con datos del cliente (JOIN).
        @return lista de pedidos
    */

    List<Pedido> listarTodos();

    /**
        Devuelve todos los pedidos de un cliente concreto.
        @param clienteId id del cliente
        @return lista de pedidos del cliente
    */

    List<Pedido> listarPorCliente(int clienteId);

    /**
        Devuelve las líneas de un pedido concreto.
        @param pedidoId id del pedido
        @return lista de líneas del pedido
    */

    List<LineaPedido> listarLineas(int pedidoId);

    /**
        Busca un pedido por su id.
        @param id identificador del pedido
        @return Pedido encontrado o null si no existe
    */

    Pedido buscarPorId(int id);

    /**
        Actualiza el estado de un pedido.
        @param id     identificador del pedido
        @param estado nuevo estado: 'pendiente','en_cocina','listo','entregado','cancelado'
        @return true si se actualizó correctamente
    */

    boolean actualizarEstado(int id, String estado);

    /**
        Elimina un pedido y todas sus líneas (CASCADE en BD).
        @param id identificador del pedido a eliminar
        @return true si se eliminó correctamente
    */

    boolean eliminar(int id);
}
