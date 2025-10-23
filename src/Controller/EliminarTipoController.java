package Controller;

import Model.TipoProducto;
import Model.TipoProductoDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;
import View.frmEliminarTipo;
import View.frmMenu;
import Model.Conexion;

public class EliminarTipoController {
    private frmEliminarTipo vista;
    private TipoProductoDAO dao;
    private frmMenu menuPrincipal;

    public EliminarTipoController(frmEliminarTipo vista, TipoProductoDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        initController();
        javax.swing.SwingUtilities.invokeLater(() -> {
        System.out.println("Combo tipo (después de init): " + vista.getcmbTipo());
        cargarTipos();
    });
        cargarTipos();
    }

    private void initController() {
        vista.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarTipo();
            }
        });
    }

    // 🔹 Cargar nombres de tipos al ComboBox
private void cargarTipos() {
    vista.getcmbTipo().removeAllItems();
    String sql = "SELECT id_tipo, nombre_tipo FROM tipos_producto ORDER BY nombre_tipo";
    
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            TipoProducto tipo = new TipoProducto();
            tipo.setId_tipo(rs.getInt("id_tipo"));
            tipo.setNombre_tipo(rs.getString("nombre_tipo"));
            vista.getcmbTipo().addItem(tipo); // 👈 ahora sí agregas objetos TipoProducto
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    private void eliminarTipo() {
        TipoProducto tipoSeleccionado = (TipoProducto) vista.getcmbTipo().getSelectedItem();


        if (tipoSeleccionado == null) {
    JOptionPane.showMessageDialog(vista, "Por favor selecciona un tipo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
    return;
}

// Verificar si existen productos asociados
int productosAsociados = contarProductosAsociados(tipoSeleccionado.getId_tipo());
if (productosAsociados > 0) {
    int opcion = JOptionPane.showConfirmDialog(vista,
            "⚠️ El tipo '" + tipoSeleccionado.getNombre_tipo() + "' tiene " + productosAsociados +
            " productos asociados.\n¿Deseas eliminar también esos productos?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

    if (opcion == JOptionPane.NO_OPTION) return;
    eliminarProductosAsociados(tipoSeleccionado.getId_tipo());
}

// Eliminar el tipo
if (dao.eliminar(tipoSeleccionado.getId_tipo())) {
    JOptionPane.showMessageDialog(vista, "✅ Tipo '" + tipoSeleccionado.getNombre_tipo() + "' eliminado correctamente.");
    cargarTipos(); // refresca
    vista.getcmbTipo().setSelectedIndex(-1);
} else {
    JOptionPane.showMessageDialog(vista, "❌ No se pudo eliminar el tipo.", "Error", JOptionPane.ERROR_MESSAGE);
}
    }

    // 🔹 Contar productos asociados a un tipo
    private int contarProductosAsociados(int idTipo) {
        String sql = "SELECT COUNT(*) FROM productos WHERE id_tipo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTipo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 🔹 Eliminar productos asociados
    private void eliminarProductosAsociados(int idTipo) {
        String sql = "DELETE FROM productos WHERE id_tipo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTipo);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
