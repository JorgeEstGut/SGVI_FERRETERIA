package Controller;

import Model.Proveedor;
import Model.ProveedorDAO;
import View.frmEliminarProveedor;
import View.frmMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;



public class EliminarProveedorController {
    
    private frmEliminarProveedor vista;
    private ProveedorDAO dao;
    private frmMenu menuPrincipal;

    public EliminarProveedorController(frmEliminarProveedor vista, ProveedorDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        initComponent();
    }
    
    public void initComponent() {
        vista.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProveedorPorID();
            }
        });
        
        vista.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProveedor();
            }
        });
    }
    
     private void buscarProveedorPorID() {
        try {
            String idText = vista.getTxtIdProveedor().getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese un ID válido");
                return;
            }

            int id = Integer.parseInt(idText);
            Proveedor proveedor = dao.buscarPorId(id);

            if (proveedor != null) {
                vista.getTxtNombre().setText(proveedor.getNombre_proveedor());
                vista.getTxtEmail().setText(proveedor.getEmail());
                
                vista.getBtnEliminar().setEnabled(true);
                
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró un proveedor con el ID: " + id);
                limpiarCampos();
                vista.getBtnEliminar().setEnabled(false);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido");
            limpiarCampos();
            vista.getBtnEliminar().setEnabled(false);
        }
    }
     
     private void eliminarProveedor() {
        try {
            int id = Integer.parseInt(vista.getTxtIdProveedor().getText().trim());
            String nombreProveedor = vista.getTxtNombre().getText();
            
            int confirmacion = JOptionPane.showConfirmDialog(
                vista, 
                "¿Está seguro que desea eliminar al proveedor:\n" +
                "Nombre: " + nombreProveedor + "\n" +
                "ID: " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (dao.eliminar(id)) {
                    JOptionPane.showMessageDialog(vista, 
                        "Proveedor '" + nombreProveedor + "' eliminado correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    if (menuPrincipal != null) {
                        menuPrincipal.actualizarTablaProveedores();
                    }
                    
                    limpiarCampos();
                    vista.getBtnEliminar().setEnabled(false);
                    
                } else {
                    JOptionPane.showMessageDialog(vista, 
                        "Error al eliminar el proveedor",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "ID inválido");
        }
    }
    
    private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtEmail().setText("");
        vista.getTxtIdProveedor().setText("");
    }
    
}
