package Model;

public class Proveedor {
    private int id_proveedor;
    private String nombre_proveedor;
    private String email;

    public Proveedor() {
    }

    public Proveedor(int id_proveedor, String nombre_proveedor, String email) {
        this.id_proveedor = id_proveedor;
        this.nombre_proveedor = nombre_proveedor;
        this.email = email;
    }
    public Proveedor(int id_proveedor, String nombre_proveedor) {
        this.id_proveedor = id_proveedor;
        this.nombre_proveedor = nombre_proveedor;
}

    
    public int getId_proveedor(){ return id_proveedor; }
    public void setId_Proveedor( int id_proveedor ) { this.id_proveedor = id_proveedor; }
    
    public String getNombre_proveedor(){ return nombre_proveedor; }
    public void setNombre_proveedor( String nombre_proveedor ) { this.nombre_proveedor = nombre_proveedor; }
    
    public String getEmail(){ return email; }
    public void setEmail( String email ) { this.email = email; }

    @Override
    public String toString() {
        return nombre_proveedor ;
    }
    
    @Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Proveedor other = (Proveedor) obj;
    return id_proveedor == other.id_proveedor;
}

    

    
    
    
    
}
