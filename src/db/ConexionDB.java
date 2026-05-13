package src.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // Configuración
    private static final String URL = "jdbc:mysql://localhost:3306/restaurante_db?useSSL=false&serverTimezone=Europe/Madrid&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";
   
    // Constructor privado para que nadie instancie esta clase
    private ConexionDB() {
    }

    /**
        Devuelve una nueva conexión a la base de datos.
        El autoCommit está activado por defecto (true).
     
        @return Connection abierta lista para usar
        @throws SQLException si no se puede establecer la conexión
    */

    public static Connection getConexion() throws SQLException{
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
    
    /**
        Inicia una transacción manual desactivando el autoCommit.
        Usar siempre junto a commit() o rollback().
    
        @param conn Conexión sobre la que iniciar la transacción
        @throws SQLException si falla la operación
    */

    public static void iniciarTransaccion(Connection conn) throws SQLException{
        conn.setAutoCommit(false);
    }

    /**
        Confirma los cambios de la transacción actual.
    
        @param conn Conexión activa con transacción abierta
        @throws SQLException si falla el commit
    */
    
        public static void commit(Connection conn) throws SQLException{
        conn.commit();
    }

    /**
        Deshace todos los cambios de la transacción actual.
        Llamar siempre en el bloque catch cuando algo falla.

        @param conn Conexión activa con transacción abierta
    */

    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.out.println("ERROR al hacer rollback: " + e.getMessage());
            }
        }
    }
    
     /**
     * Cierra la conexión de forma segura.
     * Llamar siempre en el bloque finally.
     *
     * @param conn Conexión a cerrar (puede ser null)
     */

     public static void cerrar (Connection conn) {
        if (conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("ERROR al cerrar la conexión: " + e.getMessage());
            }
        }
     }
}

