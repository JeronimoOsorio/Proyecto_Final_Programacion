package model;

public class Pedido {
   
    // Atributos

    private int id;
    private int clienteId;
    private int empleadoId;
    private String fecha;
    private String estado; // pendiente, en_cocina, listo, entregado o cancelado
    private String tipo; // local o domicilio
    private double total;
    private String observaciones;
    
    // CONSTRUCTOR VACIO
    public Pedido() {
    }

    // CONSTRUCTOR LLENO

    public Pedido(int id, int clienteId, int empleadoId, String fecha, String estado, String tipo, double total,
            String observaciones) {
        this.id = id;
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.fecha = fecha;
        this.estado = estado;
        this.tipo = tipo;
        this.total = total;
        this.observaciones = observaciones;
    }

    // GETTERS Y SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
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

    // toString

    @Override
    public String toString() {
        return "Pedido [id=" + id + ", clienteId=" + clienteId + ", estado=" + estado + ", tipo=" + tipo + ", total="
                + total + "]";
    }
}
