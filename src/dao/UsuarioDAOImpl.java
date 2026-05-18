package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.ConexionDB;
import model.Usuario;

/**
    Implementación del DAO para la entidad Usuario.
    Toda la lógica SQL está aquí, nunca en las vistas.
    Usa PreparedStatement y try-with-resources en todos los métodos.
*/
public class UsuarioDAOImpl implements UsuarioDAO{

    // Validar
    @Override
    public Usuario validar(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND activo = true";
        
        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            } catch (SQLException e) {
                System.out.println("Error al validar usuario: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
        }
        return null;
    }

    // Insertar
    @Override
    public int insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {
            
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getNombre());
            ps.setString(5, usuario.getApellidos());
            ps.setString(6, usuario.getDni());
            ps.setString(7, usuario.getRol());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0){
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if(keys.next()){
                        return keys.getInt(1); // Aca devolvemos el id generado
                    }
                }
            } 
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
        return -1;
    }

    //Listar todos

    @Override
    public List<Usuario> listarTodos(){
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY apellidos, nombre";

        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // actualizar
    
    @Override
    public boolean actualizar(Usuario usuario){
        String sql = "UPDATE usuarios SET username = ?, email = ?, nombre ?, " +
                    "apellidos = ?, dni = ?, rol = ?, activo = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellidos());
            ps.setString(5, usuario.getDni());
            ps.setString(6, usuario.getRol());
            ps.setBoolean(7, usuario.isActivo());
            ps.setInt(8, usuario.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }

        return false;

    }
    
    // Eliminar
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
            
        try (Connection conn = ConexionDB.getConexion(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {
                
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
                System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
            
        return false;
    }

    // Cambiar password

    @Override
    public boolean cambiarPassword(int id, String nuevaPasswprd) {
        String sql = "UPDATE usuarios SET password = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion(); 
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, nuevaPasswprd);
            ps.setInt(2, id);
            
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar contraseña: " + e.getMessage());
        }
        
        return false;
    }

    // ── mapearUsuario (método privado auxiliar) ───────────────────────
    /**
        Convierte una fila del ResultSet en un objeto Usuario.
        Método privado reutilizado por todos los métodos de consulta.
    */

    private Usuario mapearUsuario(ResultSet rs) throws SQLException{
        return new Usuario(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("nombre"),
            rs.getString("apellidos"),
            rs.getString("dni"),
            rs.getString("rol"),
            rs.getBoolean("activo")
        );
    }
}
