package dao;

import java.util.List;

import model.Empleado;

/**
    Interfaz DAO para la entidad Empleado.
    Igual que Cliente, ocupa dos tablas: usuarios + empleados.
*/
public interface EmpleadoDAO {

    /**
        Inserta un empleado completo en usuarios + empleados en una sola transacción.
        Si falla cualquier inserción se hace rollback de todo.
        @param empleado objeto con todos los datos del empleado
        @return true si se insertó correctamente
    */
    
    boolean insertar(Empleado empleado);
    
    /**
        Devuelve todos los empleados con sus datos completos (JOIN usuarios + empleados).
        @return lista de empleados
    */

    List<Empleado> listarTodos();

    /**
        Busca un empleado por su id de usuario.
        @param id identificador del usuario/empleado
        @return Empleado encontrado o null si no existe
    */

    Empleado buscarPorId(int id);

    /**
        Actualiza los datos de un empleado en ambas tablas.
        @param empleado objeto con los datos actualizados
        @return true si se actualizó correctamente
    */

    boolean actualizar(Empleado empleado);

   /**
        Elimina un empleado. El CASCADE en BD elimina también su fila en usuarios.
        @param id identificador del empleado a eliminar
        @return true si se eliminó correctamente
    */ 

    boolean eliminar(int id);

}
