package Model;

import java.sql.*;

public class ProductoDAO {

    // Listar tabla productos
    public java.util.List<Producto> listar() {
        java.util.List<Producto> lista = new java.util.ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.descripcion, p.precio, " +
                     "t.nombre_tipo AS nombre_tipo, pr.nombre_proveedor AS nombre_proveedor, " +
                     "COALESCE(i.stock_actual, 0) AS stock " +
                     "FROM productos p " +
                     "JOIN tipos_producto t ON p.id_tipo = t.id_tipo " +
                     "JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor " +
                     "LEFT JOIN inventario i ON p.id_producto = i.id_producto";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId_producto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getFloat("precio"));
                p.setNombre_tipo(rs.getString("nombre_tipo"));
                p.setNombre_Proveedor(rs.getString("nombre_proveedor"));
                p.setStock(rs.getInt("stock"));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Listar combo tipos de producto
    public java.util.List<TipoProducto> listarProducto() {
        java.util.List<TipoProducto> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM tipos_producto";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoProducto tipo = new TipoProducto();
                tipo.setId_tipo(rs.getInt("id_tipo"));
                tipo.setNombre_tipo(rs.getString("nombre_tipo"));
                lista.add(tipo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean existeTipoProductos(String nombre) {
        String sql = "SELECT COUNT(*) FROM tipos_producto WHERE nombre_tipo = ?";
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
    
    public int obtenerStock(int idProducto) {
        String sql = "SELECT stock_actual FROM inventario WHERE id_producto = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("stock_actual");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }


    public boolean insertarTipo(TipoProducto p) {
        String sql = "INSERT INTO tipos_producto (nombre_tipo) VALUES (?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre_tipo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeProducto(String nombre) {
        String sql = "SELECT COUNT(*) FROM productos WHERE nombre = ?";
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

    public int insertarProductoYObtenerID(Producto p) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, id_tipo, id_proveedor) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id_producto";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setFloat(3, p.getPrecio());
            ps.setInt(4, p.getId_tipo());
            ps.setInt(5, p.getId_Proveedor());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_producto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int obtenerIdPorNombre(String nombre) {
        String sql = "SELECT id_producto FROM productos WHERE nombre = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_producto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 🔹 Nuevo método corregido para eliminar producto
    public boolean eliminar(int id_producto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_producto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Producto buscarPorId(int id_producto) {
        String sql = """
            SELECT p.id_producto, p.nombre, p.descripcion, p.precio,
                   p.id_tipo, t.nombre_tipo,
                   p.id_proveedor, pr.nombre_proveedor
            FROM productos p
            JOIN tipos_producto t ON p.id_tipo = t.id_tipo
            JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor
            WHERE p.id_producto = ?
        """;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_producto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Producto p = new Producto();
                p.setId_producto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getFloat("precio"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setNombre_tipo(rs.getString("nombre_tipo"));
                p.setId_Proveedor(rs.getInt("id_proveedor"));
                p.setNombre_Proveedor(rs.getString("nombre_proveedor"));
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, id_tipo = ?, id_proveedor = ? WHERE id_producto = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setFloat(3, producto.getPrecio());
            ps.setInt(4, producto.getId_tipo());
            ps.setInt(5, producto.getId_Proveedor());
            ps.setInt(6, producto.getId_producto());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Agrega estos métodos a tu ProductoDAO actual

    // Método para obtener cantidad total de productos
    public int obtenerCantidadProductos() {
        String sql = "SELECT COUNT(*) FROM productos";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cantidad de productos: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Método para obtener productos con stock bajo
    public java.util.List<ProductoStockBajo> obtenerProductosStockBajo() {
        java.util.List<ProductoStockBajo> lista = new java.util.ArrayList<>();
        String sql = "SELECT p.nombre, COALESCE(i.stock_actual, 0) as stock_actual, " +
                     "COALESCE(i.stock_minimo, 5) as stock_minimo " +
                     "FROM productos p " +
                     "LEFT JOIN inventario i ON p.id_producto = i.id_producto " +
                     "WHERE COALESCE(i.stock_actual, 0) <= COALESCE(i.stock_minimo, 5) " +
                     "ORDER BY COALESCE(i.stock_actual, 0) ASC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductoStockBajo producto = new ProductoStockBajo();
                producto.setNombre(rs.getString("nombre"));
                producto.setStockActual(rs.getInt("stock_actual"));
                producto.setStockMinimo(rs.getInt("stock_minimo"));
                lista.add(producto);
            }

            System.out.println("Productos con stock bajo encontrados: " + lista.size());

        } catch (SQLException e) {
            System.out.println("Error al obtener productos con stock bajo: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}
