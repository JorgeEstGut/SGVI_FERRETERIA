package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public List<Factura> listar() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT f.id_factura, f.fecha, f.id_cliente, c.nombre AS nombre_cliente, f.id_trabajador, t.nombre AS nombre_trabajador, f.total " 
                + "FROM facturas f "
                + "JOIN trabajadores t ON t.id_trabajador = f.id_trabajador " 
                + "JOIN clientes c ON c.id_cliente = f.id_cliente";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Factura f = new Factura();
                f.setId_factura(rs.getInt("id_factura"));
                f.setFecha(rs.getString("fecha"));
                f.setId_cliente(rs.getInt("id_cliente"));
                f.setNombreCliente(rs.getString("nombre_cliente"));
                f.setId_trabajador(rs.getInt("id_trabajador"));
                f.setNombreTrabajador(rs.getString("nombre_trabajador"));
                f.setTotal(rs.getFloat("total"));
                lista.add(f);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar facturas: " + e.getMessage());
        }

        return lista;
    }
    
    public List<FacturaDetalle> obtenerDetallesFactura(int idFactura) {
        List<FacturaDetalle> lista = new ArrayList<>();

        String sql = "SELECT f.id_factura, f.fecha, c.nombre AS nombre_cliente, c.cedula, c.telefono, " +
                     "t.nombre AS nombre_trabajador, p.nombre AS nombre_producto, df.cantidad, p.precio, df.subtotal, f.total " +
                     "FROM detalles_factura df " +
                     "JOIN facturas f ON f.id_factura = df.id_factura " +
                     "JOIN clientes c ON c.id_cliente = f.id_cliente " +
                     "JOIN trabajadores t ON t.id_trabajador = f.id_trabajador " +
                     "JOIN productos p ON p.id_producto = df.id_producto " +
                     "WHERE f.id_factura = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFactura); // Se pasa el ID de la factura que quieres ver

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FacturaDetalle fd = new FacturaDetalle();
                    fd.setIdFactura(rs.getInt("id_factura"));
                    fd.setFecha(rs.getString("fecha"));
                    fd.setNombreCliente(rs.getString("nombre_cliente"));
                    fd.setCedula(rs.getString("cedula"));
                    fd.setTelefono(rs.getString("telefono"));
                    fd.setNombreTrabajador(rs.getString("nombre_trabajador"));
                    fd.setNombreProducto(rs.getString("nombre_producto"));
                    fd.setCantidad(rs.getInt("cantidad"));
                    fd.setPrecio(rs.getDouble("precio"));
                    fd.setSubtotal(rs.getDouble("subtotal"));
                    fd.setTotal(rs.getDouble("total"));
                    lista.add(fd);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener detalles de factura: " + e.getMessage());
        }

        return lista;
    }
    
    // Método para obtener el TOTAL vendido hoy (suma de todos los totales de facturas del día)
    public double obtenerTotalVendidoHoy() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM facturas WHERE DATE(fecha) = CURRENT_DATE";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener total vendido hoy: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // Método para obtener la CANTIDAD de facturas generadas hoy
    public int obtenerFacturasGeneradasHoy() {
        String sql = "SELECT COUNT(*) FROM facturas WHERE DATE(fecha) = CURRENT_DATE";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener facturas generadas hoy: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    // Método para obtener ventas del día
    public double obtenerVentasDelDia() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM facturas WHERE DATE(fecha) = CURRENT_DATE";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ventas del día: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // Método para obtener cantidad de productos vendidos hoy
    public int obtenerCantidadProductosVendidosHoy() {
        String sql = "SELECT COALESCE(SUM(df.cantidad), 0) " +
                     "FROM detalles_factura df " +
                     "JOIN facturas f ON f.id_factura = df.id_factura " +
                     "WHERE DATE(f.fecha) = CURRENT_DATE";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cantidad de productos vendidos hoy: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Método para obtener ganancia del día (versión simplificada sin costo)
    public double obtenerGananciaDelDia() {
        // Como no veo campo "costo" en productos, usamos un porcentaje estimado
        double ventasDelDia = obtenerVentasDelDia();
        return ventasDelDia * 0.25; // 25% de ganancia estimada (ajusta según necesites)
    }

}

