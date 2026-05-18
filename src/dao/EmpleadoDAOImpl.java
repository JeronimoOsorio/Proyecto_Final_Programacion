package dao;

import db.ConexionDB;
import model.Empleado;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
    Implementación del DAO para la entidad Empleado.
    Las operaciones de escritura insertan en DOS tablas (usuarios + empleados)
    dentro de una sola transacción atómica con rollback en caso de error.
*/

public class EmpleadoDAOImpl implements EmpleadoDAO {
    
    // Insertar
    @Override
    public boolean insertar(Empleado empleado) {
        
        String sqlUsuario = "INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) " +
                            "VALUES (?, ?, ?, ?, ?, ?, 'empleado')";

        String sqlEmpleado = "INSERT INTO empleados (usuario_id, cargo, salario) VALUES (?, ?, ?)";

        Connection conn = null;

        try{
            conn = ConexionDB.getConexion();
            ConexionDB.iniciarTransaccion(conn);

            // 1. Insertar en tabla usuarios
            int idGenerado;
            try (PreparedStatement ps = conn.prepareStatement(sqlUsuario, new String[]{"id"})) {
                ps.setString(1, empleado.getUsername());
                ps.setString(2, empleado.getPassword());
                ps.setString(3, empleado.getEmail());
                ps.setString(4, empleado.getNombre());
                ps.setString(5, empleado.getApellidos());
                ps.setString(6, empleado.getDni());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if(keys.next()) {
                        idGenerado = keys.getInt(1);
                    }else{
                        throw new SQLException("No se obtuvo el id generado para el usuario");
                    }
                }
            }

            // 2. Insertar en tabla empleadps con el id obtenido

            try (PreparedStatement ps = conn.prepareStatement(sqlEmpleado)) {
                ps.setInt(1, idGenerado);
                ps.setString(2, empleado.getCargo());
                ps.setDouble(3, empleado.getSalario());
                ps.executeUpdate();
            }

            ConexionDB.commit(conn);
            return true;

        }catch (SQLException e) {
                ConexionDB.rollback(conn);
                System.out.println("Error al insertar empleado: " + e.getMessage());
                return false;
        } finally {
            ConexionDB.cerrar(conn);
        }
    }

    // listarTodos
    @Override
    public List<Empleado> listarTodos(){
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.password, u.email, u.nombre, u.apellidos, " +
                     "u.dni, u.activo, e.cargo, e.salario, e.fecha_alta " +
                     "FROM usuarios u " +
                     "INNER JOIN empleados e ON u.id = e.usuario_id " +
                     "ORDER BY u.apellidos, u.nombre";

        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearEmpleado(rs));
                }

            
        } catch (SQLException e) {
            System.out.println("Error al listar empleados: " + e.getMessage());
        }

        return lista;
    }

    // buscarPorId
    @Override
    public Empleado buscarPorId(int id) {
        String sql = "SELECT u.id, u.username, u.password, u.email, u.nombre, u.apellidos, " +
                     "u.dni, u.activo, e.cargo, e.salario, e.fecha_alta " +
                     "FROM usuarios u " +
                     "INNER JOIN empleados e ON u.id = e.usuario_id " +
                     "WHERE u.id = ?";
        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar empleado: " + e.getMessage());
        }

        return null;
    }

    // actualizar

    public boolean actualizar(Empleado empleado){
        String sqlUsuario = "UPDATE usuarios SET username = ?, email = ?, nombre = ?, " +
                            "apellidos = ?, dni = ?, activo = ? WHERE id = ?";
        String sqlEmpleado = "UPDATE empleados SET cargo = ?, salario = ? WHERE usuario_id = ?";

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            ConexionDB.iniciarTransaccion(conn);

            try (PreparedStatement ps = conn.prepareStatement(sqlUsuario)) {
                ps.setString(1, empleado.getUsername());
                ps.setString(2, empleado.getEmail());
                ps.setString(3, empleado.getNombre());
                ps.setString(4, empleado.getApellidos());
                ps.setString(5, empleado.getDni());
                ps.setBoolean(6, empleado.isActivo());
                ps.setInt(7, empleado.getId());
                ps.executeUpdate();


            } 

            try (PreparedStatement ps = conn.prepareStatement(sqlEmpleado)) {
                ps.setString(1, empleado.getCargo());
                ps.setDouble(2, empleado.getSalario());
                ps.setInt(3, empleado.getId());
                ps.executeUpdate();
            } 

            ConexionDB.commit(conn);
            return true;

        } catch (SQLException e){
            ConexionDB.rollback(conn);
            System.out.println("Error al actualizar empleado: " + e.getMessage());
            return false;
        } finally {
            ConexionDB.cerrar(conn);
        }
    }

    @Override
    public boolean eliminar(int id) {
        // Al eliminar de usuarios, el CASCADE elimina automáticamente de clientes
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
        }
        return false;
    }

    // ── mapearCliente (método privado) ───────────────────────
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        return new Empleado(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("nombre"),
            rs.getString("apellidos"),
            rs.getString("dni"),
            rs.getBoolean("activo"),
            rs.getString("cargo"),
            rs.getDouble("salario"),
            rs.getString("fecha_alta")
        );
    }
}