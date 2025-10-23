package Model;

public class Trabajador {
    private int id_trabajador;
    private int id_rol;
    private String usuario;
    private String clave;
    private String rol;
    private String nombre;

    public Trabajador() {
    }
    
    // Constructor que coincide con el orden usado en el DAO
    public Trabajador(int id_trabajador, int id_rol, String usuario, String clave, String rol, String nombre) {
        this.id_trabajador = id_trabajador;
        this.id_rol = id_rol;
        this.usuario = usuario;
        this.clave = clave;
        this.rol = rol;
        this.nombre = nombre;
    }
    
    // Getters
    public int getId_trabajador() { return id_trabajador; }
    public String getNombre() { return nombre; }
    public int getId_rol() { return id_rol; }
    public String getUsuario() { return usuario; }
    public String getClave() { return clave; }
    public String getRol() { return rol; }
    
    // Setters si los necesitas
    public void setId_trabajador(int id_trabajador) { this.id_trabajador = id_trabajador; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setId_rol(int id_rol) { this.id_rol = id_rol; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setClave(String clave) { this.clave = clave; }
    public void setRol(String rol) { this.rol = rol; }
}
