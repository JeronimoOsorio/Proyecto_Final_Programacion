package model;

public class Cliente extends Usuario {
    
    // Atributos
    
    private String telefono;
    private String direccion;
    private String fechaRegistro;

    // CONSTRUCTOR VACIO
    
    public Cliente() {
        super();
    }

    // CONSTRUCTOR COMPLETO

    public Cliente(int id, String username, String password, String email, String nombre, String apellidos, String dni,
            boolean activo, String telefono, String direccion, String fechaRegistro) {
        super(id, username, password, email, nombre, apellidos, dni, "cliente", activo);
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
    }

    // GETTERS Y SETTERS

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // toString para cliente
    @Override
    public String toString() {
        return "Cliente [ Id= " + getId() + ", Nombre= " + getNombre()
                + ", Apellidos= " + getApellidos() + " telefono= " + telefono +" ]";
    }
    
    
    
}
