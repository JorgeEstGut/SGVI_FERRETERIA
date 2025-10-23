package Model;

import java.util.Date;
import java.util.*;

public class Ventas {
    private int id_factura;
    private Date fecha;
    private int id_cliente;
    private String nombre_clientes;
    private String cedula_clientes;
    private String telefono_clientes;
    private int id_trabajador;
    private String nombre_trabajadores;
    private int id_producto;
    private String nombre_productos;
    private int cantidad;
    private double precio;
    private double subtotal;
    private double total;
    
    public Ventas() {
    }

    public Ventas(int id_factura, Date fecha, int id_cliente, String nombre_clientes, String cedula_clientes, String telefono_clientes, int id_trabajador, String nombre_trabajadores, int id_producto, String nombre_productos, int cantidad, double precio, double subtotal, double total) {
        this.id_factura = id_factura;
        this.fecha = fecha;
        this.id_cliente = id_cliente;
        this.nombre_clientes = nombre_clientes;
        this.cedula_clientes = cedula_clientes;
        this.telefono_clientes = telefono_clientes;
        this.id_trabajador = id_trabajador;
        this.nombre_trabajadores = nombre_trabajadores;
        this.id_producto = id_producto;
        this.nombre_productos = nombre_productos;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
        this.total = total;
    }

public int getId_factura() { return id_factura; }
    public void setId_factura(int id_factura) { this.id_factura = id_factura; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public int getId_cliente() { return id_cliente; }
    public void setId_cliente(int id_cliente) { this.id_cliente = id_cliente; }

    public String getNombre_clientes() { return nombre_clientes; }
    public void setNombre_clientes(String nombre_clientes) { this.nombre_clientes = nombre_clientes; }

    public String getCedula_clientes() { return cedula_clientes; }
    public void setCedula_clientes(String cedula_clientes) { this.cedula_clientes = cedula_clientes; }

    public String getTelefono_clientes() { return telefono_clientes; }
    public void setTelefono_clientes(String telefono_clientes) { this.telefono_clientes = telefono_clientes; }

    public int getId_trabajador() { return id_trabajador; }
    public void setId_trabajador(int id_trabajador) { this.id_trabajador = id_trabajador; }

    public String getNombre_trabajadores() { return nombre_trabajadores; }
    public void setNombre_trabajadores(String nombre_trabajadores) { this.nombre_trabajadores = nombre_trabajadores; }

    public int getId_producto() { return id_producto; }
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }

    public String getNombre_productos() { return nombre_productos; }
    public void setNombre_productos(String nombre_productos) { this.nombre_productos = nombre_productos; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    private List<DetalleVenta> detalleVenta;

    public List<DetalleVenta> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(List<DetalleVenta> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }
}
