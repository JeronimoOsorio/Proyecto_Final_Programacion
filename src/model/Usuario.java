package model;

public class Usuario {
    
    // Atributos
    private int id;
    private String username;
    private String password;
    private String email;
    private String nombre;
    private String apellidos;
    private String dni;
    private String rol;
    private boolean activo;

    // CONSTRUCTOR VACIO

    public Usuario() {
    }

    // CONSTRUCTOR COMPLETO con todos los atributos

    public Usuario(int id, String username, String password, String email, String nombre, String apellidos, String dni,
            String rol, boolean activo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.rol = rol;
        this.activo = activo;
    }

    // GETTERS Y SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // toString para deuprar

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", username=" + username + ", rol=" + rol + ", activo=" + activo + "]";
    }
}
