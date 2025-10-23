
package Model;
public class Factura {
    private int id_factura;
    private String fecha;
    private int id_cliente;
    private String nombreCliente;
    private int id_trabajador;
    private String nombreTrabajador;
    private float total;



    public Factura() {    
    }

    public Factura(int id_factura, String fecha, int id_cliente, String nombreCliente, int id_trabajador, String nombreTrabajador, float total) {
        this.id_factura = id_factura;
        this.fecha = fecha;
        this.id_cliente = id_cliente;
        this.nombreCliente = nombreCliente;
        this.id_trabajador = id_trabajador;
        this.nombreTrabajador = nombreTrabajador;
        this.total = total;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getId_trabajador() {
        return id_trabajador;
    }

    public void setId_trabajador(int id_trabajador) {
        this.id_trabajador = id_trabajador;
    }

    public String getNombreTrabajador() {
        return nombreTrabajador;
    }

    public void setNombreTrabajador(String nombreTrabajador) {
        this.nombreTrabajador = nombreTrabajador;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    
    
    
}