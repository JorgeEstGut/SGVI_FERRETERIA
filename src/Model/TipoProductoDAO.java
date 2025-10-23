/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Marjorie Acosta Diaz
 */
public class TipoProductoDAO {
    
    //buscarx nombre
    public TipoProducto buscarPorNombre(String nombre) {
    TipoProducto tipo = null;
    String sql = "SELECT * FROM tipo_producto WHERE nombre_tipo = ?";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            tipo = new TipoProducto();
            tipo.setId_tipo(rs.getInt("id_tipo"));
            tipo.setNombre_tipo(rs.getString("nombre_tipo"));
            
        }
    } catch (SQLException e) {
        System.err.println("Error al buscar tipo por nombre: " + e.getMessage());
    }
    return tipo;
    
    
    }
    
    public boolean eliminar(int idTipo) {
    String verificarProductos = "SELECT COUNT(*) FROM productos WHERE id_tipo = ?";
    String eliminarTipo = "DELETE FROM tipos_producto WHERE id_tipo = ?";

    try (Connection con = Conexion.getConexion();
         PreparedStatement psVerificar = con.prepareStatement(verificarProductos);
         PreparedStatement psEliminar = con.prepareStatement(eliminarTipo)) {

        // Verifica si hay productos asociados a ese tipo
        psVerificar.setInt(1, idTipo);
        ResultSet rs = psVerificar.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            JOptionPane.showMessageDialog(null,
                "❌ No se puede eliminar este tipo porque existen productos asociados.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Si no hay productos, elimina el tipo
        psEliminar.setInt(1, idTipo);
        int filas = psEliminar.executeUpdate();
        return filas > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }


    
}

