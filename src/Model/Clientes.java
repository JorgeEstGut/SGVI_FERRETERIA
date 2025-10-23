package Model;


public class Clientes {
    private int id_cliente;
    private String nombre;
    private String cedula;
    private String telefono;
    
    public Clientes() {
    }

    public Clientes(int id_cliente, String nombre, String cedula, String telefono) {
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    @Override
    public String toString() {
        return nombre;
    }   
}

