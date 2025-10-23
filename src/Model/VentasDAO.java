package Model;

import java.sql.*;
import java.util.*;

public class VentasDAO {
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    
    public List<Ventas> listar() {
        List<Ventas> lista = new ArrayList<>();
        String sql = """
            SELECT f.id_factura, f.fecha, f.total,
                   c.id_cliente, c.nombre AS nombre_clientes, c.cedula, c.telefono,
                   t.id_trabajador, t.nombre AS nombre_trabajadores,
                   p.id_producto, p.nombre AS nombre_productos,
                   d.cantidad, d.precio, d.subtotal
            FROM facturas f
            JOIN clientes c ON f.id_cliente = c.id_cliente
            JOIN trabajadores t ON f.id_trabajador = t.id_trabajador
            JOIN detalles_factura d ON f.id_factura = d.id_factura
            JOIN productos p ON d.id_producto = p.id_producto
        """;
        
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Ventas v = new Ventas();
                v.setId_factura(rs.getInt("id_factura"));
                v.setFecha(rs.getDate("fecha"));
                v.setId_cliente(rs.getInt("id_cliente"));
                v.setNombre_clientes(rs.getString("nombre_clientes"));
                v.setCedula_clientes(rs.getString("cedula"));
                v.setTelefono_clientes(rs.getString("telefono"));
                v.setId_trabajador(rs.getInt("id_trabajador"));
                v.setNombre_trabajadores(rs.getString("nombre_trabajadores"));
                v.setId_producto(rs.getInt("id_producto"));
                v.setNombre_productos(rs.getString("nombre_productos"));
                v.setCantidad(rs.getInt("cantidad"));
                v.setPrecio(rs.getDouble("precio"));
                v.setSubtotal(rs.getDouble("subtotal"));
                v.setTotal(rs.getDouble("total"));
                lista.add(v);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;    
    }    
    
   public boolean registrar(Ventas v) {
        String sqlFactura = "INSERT INTO facturas (fecha, id_cliente, id_trabajador, total) VALUES (NOW(), ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalles_factura (id_factura, id_producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
        boolean registrado = false;
        Connection con = null;

        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            // 1. Insertar factura y obtener el ID generado
            int idFacturaGenerada = 0;
            try (PreparedStatement psFactura = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS)) {
                psFactura.setInt(1, v.getId_cliente());
                psFactura.setInt(2, v.getId_trabajador());
                psFactura.setDouble(3, v.getTotal());

                int filasAfectadas = psFactura.executeUpdate();

                if (filasAfectadas > 0) {
                    // Obtener el ID generado automáticamente
                    try (ResultSet rs = psFactura.getGeneratedKeys()) {
                        if (rs.next()) {
                            idFacturaGenerada = rs.getInt(1);
                            System.out.println("✅ Nueva factura creada con ID: " + idFacturaGenerada);
                        }
                    }
                }
            }

            // 2. Insertar los detalles de la factura
            if (idFacturaGenerada > 0 && v.getDetalleVenta() != null) {
                for (DetalleVenta detalle : v.getDetalleVenta()) {
                    try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                        psDetalle.setInt(1, idFacturaGenerada);
                        psDetalle.setInt(2, detalle.getId_producto());
                        psDetalle.setInt(3, detalle.getCantidad());
                        psDetalle.setDouble(4, detalle.getPrecio());
                        psDetalle.setDouble(5, detalle.getSubtotal());

                        psDetalle.executeUpdate();
                        String sqlActualizarStock = "UPDATE inventario SET stock_actual = stock_actual - ? WHERE id_producto = ?";
                        try (PreparedStatement psStock = con.prepareStatement(sqlActualizarStock)) {
                            psStock.setInt(1, detalle.getCantidad());
                            psStock.setInt(2, detalle.getId_producto());
                            psStock.executeUpdate();
                        }
                        System.out.println("✅ Detalle insertado - Producto ID: " + detalle.getId_producto());
                    }
                }
            }

            con.commit();
            registrado = true;
            System.out.println("✅ Factura y detalles registrados exitosamente");

        } catch (SQLException e) {
            System.out.println("❌ Error al registrar venta: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return registrado;
    }
    
    /**
     * Registra una venta y retorna el ID de la factura generada
     * @param v Objeto Ventas con los datos de la venta
     * @return ID de la factura generada, -1 si hubo error
     */
    public int registrarYRetornarId(Ventas v) {
        String sqlFactura = "INSERT INTO facturas (fecha, id_cliente, id_trabajador, total) VALUES (NOW(), ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalles_factura (id_factura, id_producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
        int idFacturaGenerada = -1;
        Connection con = null;

        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            // 1. Insertar factura y obtener el ID generado
            try (PreparedStatement psFactura = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS)) {
                psFactura.setInt(1, v.getId_cliente());
                psFactura.setInt(2, v.getId_trabajador());
                psFactura.setDouble(3, v.getTotal());

                int filasAfectadas = psFactura.executeUpdate();

                if (filasAfectadas > 0) {
                    // Obtener el ID generado automáticamente
                    try (ResultSet rs = psFactura.getGeneratedKeys()) {
                        if (rs.next()) {
                            idFacturaGenerada = rs.getInt(1);
                            System.out.println("✅ Nueva factura creada con ID: " + idFacturaGenerada);
                        }
                    }
                }
            }

            // 2. Insertar los detalles de la factura
            if (idFacturaGenerada > 0 && v.getDetalleVenta() != null) {
                for (DetalleVenta detalle : v.getDetalleVenta()) {
                    try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                        psDetalle.setInt(1, idFacturaGenerada);
                        psDetalle.setInt(2, detalle.getId_producto());
                        psDetalle.setInt(3, detalle.getCantidad());
                        psDetalle.setDouble(4, detalle.getPrecio());
                        psDetalle.setDouble(5, detalle.getSubtotal());

                        psDetalle.executeUpdate();
                        String sqlActualizarStock = "UPDATE inventario SET stock_actual = stock_actual - ? WHERE id_producto = ?";
                        try (PreparedStatement psStock = con.prepareStatement(sqlActualizarStock)) {
                            psStock.setInt(1, detalle.getCantidad());
                            psStock.setInt(2, detalle.getId_producto());
                            psStock.executeUpdate();
                        }
                        System.out.println("✅ Detalle insertado - Producto ID: " + detalle.getId_producto());
                    }
                }
            }

            con.commit();
            System.out.println("✅ Factura y detalles registrados exitosamente");

        } catch (SQLException e) {
            System.out.println("❌ Error al registrar venta: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            idFacturaGenerada = -1;
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return idFacturaGenerada;
    }
    
    public boolean eliminarVenta(int id_factura) {
        String sqlDetalles = "DELETE FROM detalles_factura WHERE id_factura=?";
        String sqlFactura = "DELETE FROM facturas WHERE id_factura=?";
        Connection con = null;

        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            PreparedStatement psDetalles = con.prepareStatement(sqlDetalles);
            psDetalles.setInt(1, id_factura);
            psDetalles.executeUpdate();

            PreparedStatement psFactura = con.prepareStatement(sqlFactura);
            psFactura.setInt(1, id_factura);
            psFactura.executeUpdate();

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    } 
    
    public Ventas buscarPorId(int id_factura) {
        String sql = "SELECT f.*, c.nombre as nombre_clientes, t.nombre as nombre_trabajadores " +
                 "FROM facturas f " +
                 "JOIN clientes c ON f.id_cliente = c.id_cliente " +
                 "JOIN trabajadores t ON f.id_trabajador = t.id_trabajador " +
                 "WHERE f.id_factura = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_factura);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Ventas v = new Ventas();
                v.setId_factura(rs.getInt("id_factura"));
                v.setFecha(rs.getDate("fecha"));
                v.setId_cliente(rs.getInt("id_cliente"));
                v.setNombre_clientes(rs.getString("nombre_clientes"));
                v.setId_trabajador(rs.getInt("id_trabajador"));
                v.setNombre_trabajadores(rs.getString("nombre_trabajadores"));
                v.setTotal(rs.getDouble("total"));
;
                return v;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean modificarVenta(Ventas v) {
        String sqlFactura = "UPDATE facturas SET id_cliente=?, id_trabajador=?, total=? WHERE id_factura=?";
        String sqlEliminarDetalles = "DELETE FROM detalles_factura WHERE id_factura=?";
        String sqlInsertarDetalle = "INSERT INTO detalles_factura (id_factura, id_producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        boolean actualizado = false;

        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            System.out.println("✏️ Actualizando factura ID: " + v.getId_factura());
            System.out.println("   - Cliente: " + v.getId_cliente());
            System.out.println("   - Trabajador: " + v.getId_trabajador());
            System.out.println("   - Total: " + v.getTotal());
            System.out.println("   - Detalles: " + (v.getDetalleVenta() != null ? v.getDetalleVenta().size() : 0));

            // 1. Actualizar la factura principal
            try (PreparedStatement psFactura = con.prepareStatement(sqlFactura)) {
                psFactura.setInt(1, v.getId_cliente());
                psFactura.setInt(2, v.getId_trabajador());
                psFactura.setDouble(3, v.getTotal());
                psFactura.setInt(4, v.getId_factura());

                int filasFactura = psFactura.executeUpdate();
                System.out.println("✅ Factura actualizada - Filas afectadas: " + filasFactura);
            }

            // 2. Eliminar los detalles anteriores
            try (PreparedStatement psEliminar = con.prepareStatement(sqlEliminarDetalles)) {
                psEliminar.setInt(1, v.getId_factura());
                int filasEliminadas = psEliminar.executeUpdate();
                System.out.println("🗑️ Detalles eliminados: " + filasEliminadas);
            }

            // 3. Insertar los nuevos detalles
            if (v.getDetalleVenta() != null && !v.getDetalleVenta().isEmpty()) {
                int contadorDetalles = 0;
                for (DetalleVenta d : v.getDetalleVenta()) {
                    try (PreparedStatement psDetalle = con.prepareStatement(sqlInsertarDetalle)) {
                        psDetalle.setInt(1, v.getId_factura());
                        psDetalle.setInt(2, d.getId_producto());
                        psDetalle.setInt(3, d.getCantidad());
                        psDetalle.setDouble(4, d.getPrecio());
                        psDetalle.setDouble(5, d.getSubtotal());

                        psDetalle.executeUpdate();
                        contadorDetalles++;
                        System.out.println("   📦 Detalle " + contadorDetalles + " - Producto: " + d.getId_producto() + 
                                         ", Cantidad: " + d.getCantidad() + ", Subtotal: " + d.getSubtotal());
                    }
                }
                System.out.println("✅ Detalles insertados: " + contadorDetalles);
            } else {
                System.out.println("⚠️ No hay detalles para insertar");
            }

            con.commit();
            actualizado = true;
            System.out.println("🎉 Factura modificada exitosamente");

        } catch (SQLException e) {
            System.out.println("❌ Error al modificar venta: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                    System.out.println("🔄 Rollback realizado");
                }
            } catch (SQLException ex) {
                System.out.println("❌ Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                    System.out.println("🔒 Conexión cerrada");
                }
            } catch (SQLException e) {
                System.out.println("❌ Error al cerrar conexión: " + e.getMessage());
            }
        }

        return actualizado;
    }
    
    public List<DetalleVenta> obtenerDetallesPorFactura(int idFactura) {
    List<DetalleVenta> detalles = new ArrayList<>();
    String sql = "SELECT df.*, p.nombre as nombre_producto " +
                 "FROM detalles_factura df " +
                 "JOIN productos p ON df.id_producto = p.id_producto " +
                 "WHERE df.id_factura = ?";
    
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, idFactura);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setId_detalle(rs.getInt("id_detalle"));
            detalle.setId_factura(rs.getInt("id_factura"));
            detalle.setId_producto(rs.getInt("id_producto"));
            detalle.setCantidad(rs.getInt("cantidad"));
            detalle.setPrecio(rs.getDouble("precio"));
            detalle.setSubtotal(rs.getDouble("subtotal"));
            detalles.add(detalle);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return detalles;
}
    public List<Clientes> listarClientes() {
    List<Clientes> lista = new ArrayList<>();
    String sql = "SELECT id_cliente, nombre, cedula, telefono FROM clientes";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Clientes c = new Clientes();
            c.setId_cliente(rs.getInt("id_cliente"));
            c.setNombre(rs.getString("nombre"));
            c.setCedula(rs.getString("cedula"));
            c.setTelefono(rs.getString("telefono"));
            lista.add(c);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
    }
    
    public boolean existeCliente(String nombre) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE cedula = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean registrarCliente(Clientes c) {
    String sql = "INSERT INTO clientes (nombre, cedula, telefono) VALUES (?, ?, ?)";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, c.getNombre());
        ps.setString(2, c.getCedula());
        ps.setString(3, c.getTelefono());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
        }
    }
    
    public List<Producto> listarProductos() {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT id_producto, nombre, precio FROM productos";
    
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            Producto p = new Producto();
            p.setId_producto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setPrecio((float) rs.getDouble("precio"));
            lista.add(p);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}
    
    public String obtenerNombreProducto(int idProducto) {
    String sql = "SELECT nombre FROM productos WHERE id_producto = ?";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, idProducto);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getString("nombre");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "Producto no encontrado";
}

}


