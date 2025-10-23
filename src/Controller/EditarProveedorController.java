package Controller;

import Model.Proveedor;
import View.frmEditarProveedor;
import Model.ProveedorDAO;
import Model.Trabajador;
import View.frmMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class EditarProveedorController {
    
    private frmEditarProveedor vista;
    private ProveedorDAO dao;
    private frmMenu menuPrincipal;
    private Proveedor proveedorActual;

    public EditarProveedorController(frmEditarProveedor vista, ProveedorDAO dao, frmMenu menuPrincipal) {
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
        
        vista.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
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
            proveedorActual = dao.buscarPorId(id);

            if (proveedorActual != null) {
                vista.getTxtNombre().setText(proveedorActual.getNombre_proveedor());
                vista.getTxtEmail().setText(proveedorActual.getEmail());
                
                habilitarEdicion(true);
                
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró un proveedor con el ID: " + id);
                limpiarCampos();
                habilitarEdicion(false);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido");
            limpiarCampos();
            habilitarEdicion(false);
        }
    }
    
    private void guardarCambios() {
        try {
            String nombre = vista.getTxtNombre().getText().trim();
            String email = vista.getTxtEmail().getText().trim();

            if (nombre.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Complete todos los campos");
                return;
            }

            if (!nombre.equals(proveedorActual.getNombre_proveedor())) {
                if (dao.existeProveedor(nombre)) {
                    JOptionPane.showMessageDialog(vista, 
                        "❌ El proveedor '" + nombre + "' ya está registrado.\n\n", 
                        "Proveedor duplicado", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Confirmación antes de guardar
            int confirmacion = JOptionPane.showConfirmDialog(
                vista, 
                "¿Está seguro que desea guardar los cambios para el proveedor:\n" +
                "ID: " + proveedorActual.getId_proveedor()+ "\n" +
                "Nombre: " + nombre + "?",
                "Confirmar cambios",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                proveedorActual.setNombre_proveedor(nombre);
                proveedorActual.setEmail(email);

                if (dao.actualizar(proveedorActual)) {
                    JOptionPane.showMessageDialog(vista, 
                        "Proveedor actualizado correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    if (menuPrincipal != null) {
                        menuPrincipal.actualizarTablaProveedores();
                    }
                    
                    limpiarCampos();
                    habilitarEdicion(false);
                    
                } else {
                    JOptionPane.showMessageDialog(vista, 
                        "Error al actualizar el proveedor",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error inesperado: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void habilitarEdicion(boolean habilitar) {
        vista.getTxtNombre().setEnabled(habilitar);
        vista.getTxtEmail().setEnabled(habilitar);
        vista.getBtnGuardar().setEnabled(habilitar);
    }
    
    private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtEmail().setText("");
        vista.getTxtIdProveedor().setText("");
    }
    
}
