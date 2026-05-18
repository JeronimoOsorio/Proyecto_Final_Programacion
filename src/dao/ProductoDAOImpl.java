package dao;

import db.ConexionDB;
import model.Producto;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductoDAOImpl implements ProductoDAO {
   
     // Insertar
    @Override
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, categoria, disponible, imagen_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setString(4, producto.getCategoria());
            ps.setBoolean(5, producto.isDisponible());
            ps.setString(6, producto.getImagenUrl());
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
        }
        return false;
    }

    // listarTodos

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY categoria, nombre";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // listarPorCategoria
    @Override
    public List<Producto> listarPorCategoria(String categoria) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria = ? AND disponible = true ORDER BY nombre";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, categoria);
 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos por categoría: " + e.getMessage());
        }
        return lista;
    }
    
    // buscarPorId
    @Override
    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, id);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    // actualizar
    @Override
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, " +
                     "categoria = ?, disponible = ?, imagen_url = ? WHERE id = ?";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setString(4, producto.getCategoria());
            ps.setBoolean(5, producto.isDisponible());
            ps.setString(6, producto.getImagenUrl());
            ps.setInt(7, producto.getId());
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
        return false;
    }

    // eliminar
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
        return false;
    }

    // cambiarDisponibilidad
    @Override
    public boolean cambiarDisponibilidad(int id, boolean disponible) {
        String sql = "UPDATE productos SET disponible = ? WHERE id = ?";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setBoolean(1, disponible);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            System.err.println("Error al cambiar disponibilidad: " + e.getMessage());
        }
        return false;
    }

    // mapearProducto (método privado auxiliar)
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getDouble("precio"),
            rs.getString("categoria"),
            rs.getBoolean("disponible"),
            rs.getString("imagen_url")
        );
    }
}
