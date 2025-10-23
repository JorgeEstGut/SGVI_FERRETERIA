
package Model;


public class TipoProducto {
    private int id_tipo;
    private String nombre_tipo;
    
    public TipoProducto() {
    }

    public TipoProducto(int id_tipo, String nombre_producto) {
        this.id_tipo = id_tipo;
        this.nombre_tipo = nombre_producto;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getNombre_tipo() {
        return nombre_tipo;
    }

    public void setNombre_tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }
    
    @Override
    public String toString() {
        return nombre_tipo ;
    }
    
    @Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    TipoProducto other = (TipoProducto) obj;
    return id_tipo == other.id_tipo;
}

}
