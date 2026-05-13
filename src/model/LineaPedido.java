package model;

public class LineaPedido {
    
    // Atributos
    private int id;
    private int pedidoId;
    private int productoId;
    private int cantidad;
    private double precioUnitario;
    private double subtotal; // calculado: cantidad * precio unitario
    
    // CONSTRUCTOR VACIO
    
    public LineaPedido() {
    }

    // CONSTRUCTOR LLENO

    public LineaPedido(int id, int pedidoId, int productoId, int cantidad, double precioUnitario, double subtotal) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    // Recalcula al cambiar cantidad
    public void setCantidad(int c) {
        this.cantidad = c;
        this.subtotal = c * this.precioUnitario;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    // Recalcula al cambiar precio
    public void setPrecioUnitario(double p) {
        this.precioUnitario = p;
        this.subtotal = this.cantidad * p;
    }

    public double getSubtotal() {
        return subtotal;
    }

    // toString 

    @Override
    public String toString() {
        return "LineaPedido [id=" + id + ", pedidoId=" + pedidoId + ", productoId=" + productoId + ", cantidad="
                + cantidad + ", precioUnitario=" + precioUnitario + ", subtotal=" + subtotal + "]";
    }

    
    

    
}
