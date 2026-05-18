package dao;

import db.ConexionDB;
import model.LineaPedido;
import model.Pedido;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {
    
    // Insertar

    @Override
    public boolean insertar(Pedido pedido, List<LineaPedido> lineas) {
        String sqlPedido = "INSERT INTO pedidos (cliente_id, empleado_id, fecha, estado, tipo, total, observaciones) " +
                           "VALUES (?, ?, NOW(), ?, ?, ?, ?)";
        String sqlLinea  = "INSERT INTO lineas_pedido (pedido_id, producto_id, cantidad, precio_unitario) " +
                           "VALUES (?, ?, ?, ?)";
 
        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            ConexionDB.iniciarTransaccion(conn);
 
            // 1. Insertar cabecera del pedido
            int pedidoId;
            try (PreparedStatement ps = conn.prepareStatement(sqlPedido, new String[]{"id"})) {
                ps.setInt(1, pedido.getClienteId());
                ps.setInt(2, pedido.getEmpleadoId());
                ps.setString(3, pedido.getEstado());
                ps.setString(4, pedido.getTipo());
                ps.setDouble(5, pedido.getTotal());
                ps.setString(6, pedido.getObservaciones());
                ps.executeUpdate();
 
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        pedidoId = keys.getInt(1);
                    } else {
                        throw new SQLException("No se obtuvo el id del pedido");
                    }
                }
            }

         // 2. Insertar cada línea del pedido
            try (PreparedStatement ps = conn.prepareStatement(sqlLinea)) {
                for (LineaPedido linea : lineas) {
                    ps.setInt(1, pedidoId);
                    ps.setInt(2, linea.getProductoId());
                    ps.setInt(3, linea.getCantidad());
                    ps.setDouble(4, linea.getPrecioUnitario());
                    ps.addBatch(); // añade cada línea al lote
                }
                ps.executeBatch(); // ejecuta todas las líneas de golpe
            }  
            
            ConexionDB.commit(conn);
            return true;
 
        } catch (SQLException e) {
            ConexionDB.rollback(conn);
            System.err.println("Error al insertar pedido: " + e.getMessage());
            return false;
        } finally {
            ConexionDB.cerrar(conn);
        }
    }

    // listarTodos
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY fecha DESC";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                lista.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        }
        return lista;
    }

    // ── listarPorCliente ─────────────────────────────────────────────
    @Override
    public List<Pedido> listarPorCliente(int clienteId) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE cliente_id = ? ORDER BY fecha DESC";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, clienteId);
 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPedido(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos por cliente: " + e.getMessage());
        }
        return lista;
    }

    // listarLineas
    @Override
    public List<LineaPedido> listarLineas(int pedidoId){
        List<LineaPedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM lineas_pedido WHERE pedido_id =  ?";

        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, pedidoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearLinea(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar lineas del pedido: " + e.getMessage());
        }
        return lista;
    }

    // buscarPorId
    @Override
    public Pedido buscarPorId(int id){
        String sql = "SELECT * FROM pedidos WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return mapearPedido(rs);
                }
            } 
        } catch (SQLException e) {
            System.out.println("Error al buscar pedido: " + e.getMessage());
        }

        return null;
    }

    // actualizarEstado
    @Override
    public boolean actualizarEstado(int id, String estado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion(); 
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0; 

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado del pedido: " + e.getMessage());
        }

        return false;
    }

    // eliminar
    @Override
    public boolean eliminar(int id) {
        // El CASCADE elimina automáticamente las líneas del pedido
        String sql = "DELETE FROM pedidos WHERE id = ?";
 
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
        }
        return false;
    }
    // mapearPedido
    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        return new Pedido(
            rs.getInt("id"),
            rs.getInt("cliente_id"),
            rs.getInt("empleado_id"),
            rs.getString("fecha"),
            rs.getString("estado"),
            rs.getString("tipo"),
            rs.getDouble("total"),
            rs.getString("observaciones")
        );
    }

    // ── mapearLinea (método privado auxiliar) ─────────────────────────
    private LineaPedido mapearLinea(ResultSet rs) throws SQLException {
        return new LineaPedido(
            rs.getInt("id"),
            rs.getInt("pedido_id"),
            rs.getInt("producto_id"),
            rs.getInt("cantidad"),
            rs.getDouble("precio_unitario")
        );
    }
}
