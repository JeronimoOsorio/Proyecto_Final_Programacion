package dao;

import java.util.List;

import model.Usuario;

/**
    Interfaz DAO para la entidad Usuario.
    Define las operaciones CRUD que debe implementar UsuarioDAOImpl.
    Las vistas NUNCA acceden directamente a esta interfaz con SQL,
    solo usan los objetos de dominio que devuelven estos métodos.
*/

public interface UsuarioDAO {
    /**
        Valida las credenciales de login.
        @param username nombre de usuario
        @param password contraseña en texto plano
        @return Usuario si las credenciales son correctas, null si no
    */
    
    Usuario validar(String username, String password);

    /**
        Registra un nuevo usuario en la tabla usuarios.
        NO inserta en tablas hijas (clientes/empleados).
        Usar ClienteDAO o EmpleadoDAO para la transacción completa.
        @param usuario objeto con los datos a insertar
        @return id generado por la base de datos, -1 si falla
    */

        int insertar(Usuario usuario);

    /**
        Devuelve todos los usuarios de la base de datos.
        @return lista de usuarios (vacía si no hay ninguno)
    */

        List<Usuario> listarTodos();

    /**
        Actualiza los datos de un usuario existente.
        @param usuario objeto con los datos actualizados (debe tener id)
        @return true si se actualizó correctamente
    */
    
        boolean actualizar(Usuario usuario);

    /**
        Elimina un usuario por su id.
        El CASCADE de la BD eliminará automáticamente el cliente/empleado asociado.
        @param id identificador del usuario a eliminar
        @return true si se eliminó correctamente
    */

    boolean eliminar(int id);

    /**
        Cambia la contraseña de un usuario.
        @param id identificador del usuario
        @param nuevaPassword nueva contraseña en texto plano
        @return true si se cambió correctamente
    */

    boolean cambiarPassword(int id, String nuevaPassword);


}