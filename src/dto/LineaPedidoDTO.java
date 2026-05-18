package dto;

public class LineaPedidoDTO {

    // Datos de la línea (Atributos)

    private int    id;
    private int    pedidoId;
    private int    cantidad;
    private double precioUnitario;
    private double subtotal;
 
    // Datos del producto (del JOIN)

    private int    productoId;
    private String productoNombre;
    private String productoCategoria;
 
    // Constructor vacío

    public LineaPedidoDTO() {

    }
 
    // Constructor lleno
    public LineaPedidoDTO(int id, int pedidoId, int cantidad, double precioUnitario,
                          double subtotal, int productoId, String productoNombre,
                          String productoCategoria) {
        this.id                = id;
        this.pedidoId          = pedidoId;
        this.cantidad          = cantidad;
        this.precioUnitario    = precioUnitario;
        this.subtotal          = subtotal;
        this.productoId        = productoId;
        this.productoNombre    = productoNombre;
        this.productoCategoria = productoCategoria;
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
    public void setPedidoId(int p) { 
        this.pedidoId = p;
    }
 
    public int getCantidad() { 
        return cantidad;
    }

    public void setCantidad(int c) {
        this.cantidad = c;
    }
 
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double p) {
        this.precioUnitario = p;
    }
 
    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double s) {
        this.subtotal = s;
    }
 
    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int p) {
        this.productoId = p;
    }
 
    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String p) {
        this.productoNombre = p;
    }
 
    public String getProductoCategoria() {
        return productoCategoria; 
    }

    public void setProductoCategoria(String p) {
        this.productoCategoria = p;
    }
 
    // toString
    @Override
    public String toString() {
        return "LineaPedidoDTO{producto='" + productoNombre + "', cantidad=" + cantidad +
               ", precioUnitario=" + precioUnitario + ", subtotal=" + subtotal + "}";
    }
}
