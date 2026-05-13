package model;

public class Producto {
    
    // Atributos
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria; //pizza, pasta, entrante, postre o bebida
    private boolean disponible;
    private String imagenUrl;

    // CONSTRUCTOR VACIO

    public Producto() {
    }

    // CONSTRUCTOR COMPLETO

    public Producto(int id, String nombre, String descripcion, double precio, String categoria, boolean disponible,
            String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.disponible = disponible;
        this.imagenUrl = imagenUrl;
    }

     // CONSTRUCTOR sin imagenURL (uso frecunte)

    public Producto(int id, String nombre, String descripcion, double precio, String categoria, boolean disponible) {
        this(id, nombre, descripcion, precio, categoria, disponible, null);
    }

    // GETTERS Y SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }


    // toString producto
    public Producto(int id, String nombre, double precio, String categoria, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    


    
    
    

}
