package Model;

import java.sql.*;

public class TrabajadorDAO {

   public Trabajador login(String usuario, String clave) {
        String sql = "SELECT t.id_trabajador, r.id_rol, t.nombre, t.usuario, t.clave, r.nombre_rol " +
                     "FROM trabajadores t " +
                     "JOIN roles r ON t.id_rol = r.id_rol " +
                     "WHERE t.usuario=? AND t.clave=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Trabajador t = new Trabajador();
                t.setId_trabajador(rs.getInt("id_trabajador"));
                t.setId_rol(rs.getInt("id_rol"));
                t.setUsuario(rs.getString("usuario"));
                t.setClave(rs.getString("clave"));
                t.setRol(rs.getString("nombre_rol"));
                t.setNombre(rs.getString("nombre"));
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    // Listar todos los trabajadores con su rol
    public java.util.List<Trabajador> listar() {
        java.util.List<Trabajador> lista = new java.util.ArrayList<>();
        String sql = "SELECT t.id_trabajador, t.nombre, t.usuario, t.clave, r.nombre_rol " +
                     "FROM trabajadores t " +
                     "JOIN roles r ON t.id_rol = r.id_rol";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Trabajador t = new Trabajador();
                t.setId_trabajador(rs.getInt("id_trabajador"));
                t.setNombre(rs.getString("nombre"));
                t.setUsuario(rs.getString("usuario"));
                t.setClave(rs.getString("clave"));
                t.setRol(rs.getString("nombre_rol"));
                lista.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    // Insertar un nuevo trabajador
    public boolean insertar(Trabajador t) {
        String sql = "INSERT INTO trabajadores (nombre, id_rol, usuario, clave) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setInt(2, t.getId_rol());
            ps.setString(3, t.getUsuario());
            ps.setString(4, t.getClave());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Método para verificar si el usuario ya existe
    public boolean existeUsuario(String usuario) {
        String sql = "SELECT COUNT(*) FROM trabajadores WHERE usuario = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Actualizar trabajador
    public boolean actualizar(Trabajador trabajador) {
        String sql = "UPDATE trabajadores SET nombre=?, id_rol=?, usuario=?, clave=? WHERE id_trabajador=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trabajador.getNombre());
            ps.setInt(2, trabajador.getId_rol());
            ps.setString(3, trabajador.getUsuario());
            ps.setString(4, trabajador.getClave());
            ps.setInt(5, trabajador.getId_trabajador());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }  

    // Eliminar trabajador
    public boolean eliminar(int idTrabajador) {
        String sql = "DELETE FROM trabajadores WHERE id_trabajador=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTrabajador);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Buscar por trabajador por ID
    public Trabajador buscarPorId(int idTrabajador) {
        String sql = "SELECT t.*, r.nombre_rol as rol FROM trabajadores t " +
                     "INNER JOIN roles r ON t.id_rol = r.id_rol " +
                     "WHERE t.id_trabajador = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTrabajador);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Trabajador t = new Trabajador();
                t.setId_trabajador(rs.getInt("id_trabajador"));
                t.setNombre(rs.getString("nombre"));
                t.setUsuario(rs.getString("usuario"));
                t.setClave(rs.getString("clave"));
                t.setId_rol(rs.getInt("id_rol"));
                t.setRol(rs.getString("rol"));
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //Listar roles
    public java.util.List<Rol> listarRoles() {
        java.util.List<Rol> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM roles";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol();
                rol.setId_rol(rs.getInt("id_rol"));
                rol.setNombre_rol(rs.getString("nombre_rol"));
                lista.add(rol);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


}
