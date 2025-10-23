package Model;

import java.sql.*;

public class ProveedorDAO {

    //Listar tabla proveedores
    public java.util.List<Proveedor> listar() {
        java.util.List<Proveedor> lista = new java.util.ArrayList<>();
        String sql = "SELECT p.id_proveedor, p.nombre_proveedor, p.email FROM proveedores p";
        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            
            while(rs.next()) {
                Proveedor p = new Proveedor();
                p.setId_Proveedor(rs.getInt("id_proveedor"));
                p.setNombre_proveedor(rs.getString("nombre_proveedor"));
                p.setEmail(rs.getString("email"));
                lista.add(p);
            }
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
    
    //listar jcombo de proveedor
        public java.util.List<Proveedor> listarProveedor() {
        java.util.List<Proveedor> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM proveedores";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId_Proveedor(rs.getInt("id_Proveedor"));
                proveedor.setNombre_proveedor(rs.getString("nombre_proveedor"));
                lista.add(proveedor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    //Verificar nombre proveedor
    public boolean existeProveedor(String nombre) {
        String sql = "SELECT COUNT(*) FROM proveedores WHERE nombre_proveedor = ?";
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
    
    //Insetar Proveedor
    public boolean insertar(Proveedor p) {
        String sql = "INSERT INTO proveedores (nombre_proveedor, email) VALUES (?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre_proveedor());
            ps.setString(2, p.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Eliminar Proveedor
    public boolean eliminar(int id_proveedor) {
        String sql = "DELETE FROM proveedores WHERE id_proveedor = ?";
        try(Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id_proveedor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Buscar Proveedor por ID
    public Proveedor buscarPorId(int id_proveedor) {
        String sql = "SELECT * FROM proveedores p WHERE p.id_proveedor = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_proveedor);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Proveedor p = new Proveedor();
                p.setId_Proveedor(rs.getInt("id_proveedor"));
                p.setNombre_proveedor(rs.getString("nombre_proveedor"));
                p.setEmail(rs.getString("email"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean actualizar(Proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre_proveedor=?, email=? WHERE id_proveedor=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, proveedor.getNombre_proveedor());
            ps.setString(2, proveedor.getEmail());
            ps.setInt(3, proveedor.getId_proveedor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
