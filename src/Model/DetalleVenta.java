package Model;


public class DetalleVenta {
    
    private int id_detalle;
    private int id_factura;
    private int id_producto;
    private int cantidad;
    private double precio;
    private double subtotal;

    public int getId_detalle() { return id_detalle; }
    public void setId_detalle(int id_detalle) { this.id_detalle = id_detalle; }

    public int getId_factura() { return id_factura; }
    public void setId_factura(int id_factura) { this.id_factura = id_factura; }

    public int getId_producto() { return id_producto; }
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
