package model;

public class Empleado extends Usuario{
    
    // Atributos
    private String cargo;   // camarero, cocinero, repartidor o admin
    private double salario;
    private String fechaAlta;

    // CONSTRUCTOR VACIO

    public Empleado() {
        super();
    }

    // CONSTRUCTOR COMPLETO

    public Empleado(int id, String username, String password, String email, String nombre, String apellidos, String dni,
            boolean activo, String cargo, double salario, String fechaAlta) {
        super(id, username, password, email, nombre, apellidos, dni, "empleado", activo);
        this.cargo = cargo;
        this.salario = salario;
        this.fechaAlta = fechaAlta;
    }

    // GETTERS Y SETTERS

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    //toString de empleado
    
    @Override
    public String toString() {
        return "Empleado [ Id= " + getId() + ", Nombre= " + getNombre()
                + ", Apellidos= " + getApellidos() + " cargo= " + cargo + " ]";
    }    
    
    

    

}
