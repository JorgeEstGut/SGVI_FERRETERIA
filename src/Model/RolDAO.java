package Model;

import java.sql.*;

public class RolDAO {
    public int obtenerIdPorNombre(String nombreRol) {
    String sql = "SELECT id_rol FROM roles WHERE nombre_rol = ?";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, nombreRol);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_rol");
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener id_rol: " + e.getMessage());
    }
    return -1;
}

}
