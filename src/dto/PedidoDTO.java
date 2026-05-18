package dto;

public class PedidoDTO {
    
    // Datos del pedido (Atributos)
    private int id;
    private String fecha;
    private String estado;
    private String tipo;
    private double total;
    private String observaciones;

    // Datos del cliente (del JOIN)
    private int    clienteId;
    private String clienteNombre;
    private String clienteApellidos;
    private String clienteTelefono;
 
    // Datos del empleado (del JOIN)
    private int    empleadoId;
    private String empleadoNombre;
    private String empleadoApellidos;

    // Constructor vacío
    public PedidoDTO() {

    }

    // Constructor lleno

    public PedidoDTO(int id, String fecha, String estado, String tipo, double total, String observaciones,
            int clienteId, String clienteNombre, String clienteApellidos, String clienteTelefono, int empleadoId,
            String empleadoNombre, String empleadoApellidos) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.tipo = tipo;
        this.total = total;
        this.observaciones = observaciones;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.clienteApellidos = clienteApellidos;
        this.clienteTelefono = clienteTelefono;
        this.empleadoId = empleadoId;
        this.empleadoNombre = empleadoNombre;
        this.empleadoApellidos = empleadoApellidos;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteApellidos() {
        return clienteApellidos;
    }

    public void setClienteApellidos(String clienteApellidos) {
        this.clienteApellidos = clienteApellidos;
    }

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getEmpleadoNombre() {
        return empleadoNombre;
    }

    public void setEmpleadoNombre(String empleadoNombre) {
        this.empleadoNombre = empleadoNombre;
    }

    public String getEmpleadoApellidos() {
        return empleadoApellidos;
    }

    public void setEmpleadoApellidos(String empleadoApellidos) {
        this.empleadoApellidos = empleadoApellidos;
    }
    
    // Metodo util para mostrar el nombre completo del cliente

    public String getClienteNombreCompleto(){
        return clienteNombre + " " + clienteApellidos; 
    }

    // ── Método útil para mostrar nombre completo del empleado ────────
    public String getEmpleadoNombreCompleto() {
        return empleadoNombre + " " + empleadoApellidos;
    }

    // ── toString ─────────────────────────────────────────────────────
    @Override
    public String toString() {
        return "PedidoDTO {id= " + id + ", cliente= " + getClienteNombreCompleto() +
               ", estado= " + estado + "', total= " + total + "}";
    }
}