package dao;

import db.ConexionDB;
import model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** 
        Implementación del DAO para la entidad Cliente.
        Las operaciones de escritura insertan en DOS tablas (usuarios + clientes)
        dentro de una sola transacción atómica con rollback en caso de error.
*/

public class ClienteDAOImpl implements ClienteDAO {

    // Insertar

    @Override
    public boolean insertar(Cliente cliente) {
        String sqlUsuario = "INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) " + "VALUES (?, ?, ?, ?, ?, ?, 'cliente')";
        
        String sqlCliente = "INSERT INTO clientes (usuario_id, telefono, direccion) VALUES (?, ?, ?)"; 

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            ConexionDB.iniciarTransaccion(conn); // setAutoCommit(false)

            // 1. Insertar en tabla usuarios
            int idGenerado;

            try(PreparedStatement ps = conn.prepareStatement(sqlUsuario, new String[]{"id"})){
                
                ps.setString(1, cliente.getUsername());
                ps.setString(2, cliente.getPassword());
                ps.setString(3, cliente.getEmail());
                ps.setString(4, cliente.getNombre());
                ps.setString(5, cliente.getApellidos());
                ps.setString(6, cliente.getDni());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()){
                    if (keys.next()){
                        idGenerado = keys.getInt(1);
                    } else {
                        throw new SQLException("No se obtuvo el id generado para el usuario");
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCliente)) {
                ps.setInt(1, idGenerado);
                ps.setString(2, cliente.getTelefono());
                ps.setString(3, cliente.getDireccion());
                ps.executeUpdate();
            }

            ConexionDB.commit(conn); // Todo ha salido correcto
            return true;

        
        } catch (SQLException e){
            ConexionDB.rollback(conn); // algo fallo, deshacemos todo
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return false;
        } finally{
            ConexionDB.cerrar(conn);
        }
    }

    // 2. ListarTodos
    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.password, u.email, u.nombre, u.apellidos, " +
                     "u.dni, u.activo, c.telefono, c.direccion, c.fecha_registro " +
                     "FROM usuarios u " +
                     "INNER JOIN clientes c ON u.id = c.usuario_id " +
                     "ORDER BY u.apellidos, u.nombre";
        
        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
           System.out.println("Error al listar clientes: " + e.getMessage());
        }
        
        return lista;
    }

    // buscarPorId

    @Override
    public Cliente buscarPorId(int id){
        String sql = "SELECT u.id, u.username, u.password, u.email, u.nombre, u.apellidos, " +
                     "u.dni, u.activo, c.telefono, c.direccion, c.fecha_registro " +
                     "FROM usuarios u " +
                     "INNER JOIN clientes c ON u.id = c.usuario_id " +
                     "WHERE u.id = ?";
        
        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    // Actualizar
    @Override
    public boolean actualizar(Cliente cliente) {
        String sqlUsuario ="UPDATE usuarios SET username = ?, email = ?, nombre = ?, " +
                            "apellidos = ?, dni = ?, activo = ? WHERE id = ?";
        String sqlCliente = "UPDATE clientes SET telefono = ?, direccion = ? WHERE usuario_id = ?";

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            ConexionDB.iniciarTransaccion(conn);

            try(PreparedStatement ps = conn.prepareStatement(sqlUsuario)){
                ps.setString(1, cliente.getUsername());
                ps.setString(2, cliente.getEmail());
                ps.setString(3, cliente.getNombre());
                ps.setString(4, cliente.getApellidos());
                ps.setString(5, cliente.getDni());
                ps.setBoolean(6, cliente.isActivo());
                ps.setInt(7, cliente.getId());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCliente)) {
                ps.setString(1, cliente.getTelefono());
                ps.setString(2, cliente.getDireccion());
                ps.setInt(3, cliente.getId());
                ps.executeUpdate();
            } 

            ConexionDB.commit(conn);
            return true;

        } catch (SQLException e) {
            ConexionDB.rollback(conn);
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        } finally {
            ConexionDB.cerrar(conn);
        }
    }

    // eliminar

    @Override
    public boolean eliminar(int id){
        // Al eliminar de usuarios, el CASCADE elimina automáticamente de clientes
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
        
        return false;
    }

    // mapearCliente (metodo privado)
    private Cliente mapearCliente(ResultSet rs) throws SQLException{
        return new Cliente(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("nombre"),
            rs.getString("apellidos"),
            rs.getString("dni"),
            rs.getBoolean("activo"),
            rs.getString("telefono"),
            rs.getString("direccion"),
            rs.getString("fecha_registro")
        );
    }
}
