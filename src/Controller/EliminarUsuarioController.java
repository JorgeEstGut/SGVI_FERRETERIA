package Controller;

import Model.Trabajador;
import Model.TrabajadorDAO;
import Model.Rol;
import View.frmEliminarUsuario;
import View.frmMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class EliminarUsuarioController {

    private frmEliminarUsuario vista;
    private TrabajadorDAO dao;
    private frmMenu menuPrincipal;

    public EliminarUsuarioController(frmEliminarUsuario vista, TrabajadorDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        initController();
    }

    private void initController() {
        vista.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuarioPorID();
            }
        });

        vista.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario();
            }
        });
    }

    private void buscarUsuarioPorID() {
        try {
            String idText = vista.getTxtIdUsuario().getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese un ID válido");
                return;
            }

            int id = Integer.parseInt(idText);
            Trabajador trabajador = dao.buscarPorId(id);

            if (trabajador != null) {
                // Llenar los campos con los datos del trabajador
                vista.getTxtNombre().setText(trabajador.getNombre());
                vista.getTxtUsuario().setText(trabajador.getUsuario());
                vista.getTxtClave().setText(trabajador.getClave());
                
                // Configurar el ComboBox del rol
                vista.getcmbRol().removeAllItems();
                vista.getcmbRol().addItem(new Rol(trabajador.getId_rol(), trabajador.getRol()));
                
                // Habilitar el botón de eliminar
                vista.getBtnEliminar().setEnabled(true);
                
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró un usuario con el ID: " + id);
                limpiarCampos();
                vista.getBtnEliminar().setEnabled(false);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido");
            limpiarCampos();
            vista.getBtnEliminar().setEnabled(false);
        }
    }

    private void eliminarUsuario() {
        try {
            int id = Integer.parseInt(vista.getTxtIdUsuario().getText().trim());
            String nombreUsuario = vista.getTxtNombre().getText();
            
            // Confirmación antes de eliminar
            int confirmacion = JOptionPane.showConfirmDialog(
                vista, 
                "¿Está seguro que desea eliminar al usuario:\n" +
                "Nombre: " + nombreUsuario + "\n" +
                "ID: " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (dao.eliminar(id)) {
                    JOptionPane.showMessageDialog(vista, 
                        "Usuario '" + nombreUsuario + "' eliminado correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    if (menuPrincipal != null) {
                        menuPrincipal.actualizarTablaUsuarios();
                    }
                    
                    limpiarCampos();
                    vista.getBtnEliminar().setEnabled(false);
                    
                } else {
                    JOptionPane.showMessageDialog(vista, 
                        "Error al eliminar el usuario",
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
        vista.getTxtUsuario().setText("");
        vista.getTxtClave().setText("");
        vista.getcmbRol().removeAllItems();
        vista.getTxtIdUsuario().setText("");
    }
}