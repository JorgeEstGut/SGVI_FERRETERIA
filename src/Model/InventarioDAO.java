package Model;

import java.sql.*;

public class InventarioDAO {

    public boolean insertarInventario(Inventario inv) {
        String sql = "INSERT INTO inventario (id_producto, stock_actual, stock_minimo) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, inv.getId_producto());
            ps.setInt(2, inv.getStock_actual());
            ps.setInt(3, inv.getStock_minimo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarStock(int idProducto, int cantidad) {
        String sql = "UPDATE inventario SET stock_actual = stock_actual + ? WHERE id_producto = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeInventario(int idProducto) {
        String sql = "SELECT COUNT(*) FROM inventario WHERE id_producto = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Inventario buscarPorProductoId(int idProducto) {
        String sql = "SELECT * FROM inventario WHERE id_producto = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Inventario inv = new Inventario();
                inv.setId_producto(rs.getInt("id_producto"));
                inv.setStock_actual(rs.getInt("stock_actual"));
                inv.setStock_minimo(rs.getInt("stock_minimo"));
                return inv;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Nuevo método para eliminar inventario según producto
    public boolean eliminarPorProductoId(int idProducto) {
        String sql = "DELETE FROM inventario WHERE id_producto = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.executeUpdate(); // No importa si no existe
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
