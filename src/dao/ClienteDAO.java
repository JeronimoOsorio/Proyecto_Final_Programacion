package dao;

import java.util.List;

import model.Cliente;

/**
    Interfaz DAO para la entidad Cliente.
    Cliente ocupa dos tablas: usuarios + clientes (Joined Table Inheritance).
    Por eso las operaciones de escritura requieren transacciones atómicas.
*/

public interface ClienteDAO {

    /**
        Inserta un cliente completo en usuarios + clientes en una sola transacción.
        Si falla cualquier inserción se hace rollback de todo.
        @param cliente objeto con todos los datos del cliente
        @return true si se insertó correctamente
    */

    boolean insertar(Cliente cliente);

    /**
        Devuelve todos los clientes con sus datos completos (JOIN usuarios + clientes).
        @return lista de clientes
    */

    List<Cliente> listarTodos();

    /**
        Busca un cliente por su id de usuario.
        @param id identificador del usuario/cliente
        @return Cliente encontrado o null si no existe
    */

    Cliente buscarPorId(int id);

    /**
        Actualiza los datos de un cliente en ambas tablas.
        @param cliente objeto con los datos actualizados
        @return true si se actualizó correctamente
    */

        boolean actualizar(Cliente cliente);

    /**
        Elimina un cliente. El CASCADE en BD elimina también su fila en usuarios.
        @param id identificador del cliente a eliminar
        @return true si se eliminó correctamente
    */

        boolean eliminar(int id);

}