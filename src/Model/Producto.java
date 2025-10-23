
package Model;


public class Producto {
    
    private String nombre ;
    private String descripcion ;
    private float Precio ;
    private int Id_tipo;
    private int Id_Proveedor;
    private int Id_producto;
    private String Nombre_tipo;
    private String Nombre_Proveedor;
    

    public int getId_producto() {
        return Id_producto;
    }

    public void setId_producto(int Id_producto) {
        this.Id_producto = Id_producto;
    }

    public Producto(int Id_prodcuto) {
        this.Id_producto = Id_prodcuto;
    }
    
     public Producto() {
    }

    public Producto(String Nombre_tipo,String nombre_Proveedor,String nombre, String descripcion, float Precio, int Id_tipo, int Id_Proveedor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.Precio = Precio;
        this.Id_tipo = Id_tipo;
        this.Id_Proveedor = Id_Proveedor;
        
        
    }

    public String getNombre_tipo() {
        return Nombre_tipo;
    }

    public void setNombre_tipo(String Nombre_tipo) {
        this.Nombre_tipo = Nombre_tipo;
    }

    public String getNombre_Proveedor() {
        return Nombre_Proveedor;
    }

    public void setNombre_Proveedor(String Nombre_Proveedor) {
        this.Nombre_Proveedor = Nombre_Proveedor;
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

    public float getPrecio() {
        return Precio;
    }

    public void setPrecio(float Precio) {
        this.Precio = Precio;
    }

    public int getId_tipo() {
        return Id_tipo;
    }

    public void setId_tipo(int Id_tipo) {
        this.Id_tipo = Id_tipo;
    }

    public int getId_Proveedor() {
        return Id_Proveedor;
    }

    public void setId_Proveedor(int Id_Proveedor) {
        this.Id_Proveedor = Id_Proveedor;
    }
private int stock;

public int getStock() {
    return stock;
}

public void setStock(int stock) {
    this.stock = stock;
}

@Override
public String toString() {
    return nombre;
} 
    
    
            
    
}
