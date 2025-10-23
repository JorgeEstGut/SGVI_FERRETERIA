package Controller;

import Model.Trabajador;
import Model.TrabajadorDAO;
import Model.Rol;
import View.frmEditarUsuario;
import View.frmMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class EditarUsuarioController {

    private frmEditarUsuario vista;
    private TrabajadorDAO dao;
    private frmMenu menuPrincipal;
    private Trabajador trabajadorActual;

    public EditarUsuarioController(frmEditarUsuario vista, TrabajadorDAO dao, frmMenu menuPrincipal) {
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

        vista.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
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
            trabajadorActual = dao.buscarPorId(id);

            if (trabajadorActual != null) {
                vista.getTxtNombre().setText(trabajadorActual.getNombre());
                vista.getTxtUsuario().setText(trabajadorActual.getUsuario());
                vista.getTxtClave().setText(trabajadorActual.getClave());
                
                cargarRoles();
                seleccionarRolActual(trabajadorActual.getId_rol());
                
                habilitarEdicion(true);
                
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró un usuario con el ID: " + id);
                limpiarCampos();
                habilitarEdicion(false);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido");
            limpiarCampos();
            habilitarEdicion(false);
        }
    }

    private void cargarRoles() {
        java.util.List<Rol> roles = dao.listarRoles();
        vista.getcmbRol().removeAllItems();
        for (Rol rol : roles) {
            vista.getcmbRol().addItem(rol);
        }
    }

    private void seleccionarRolActual(int idRolActual) {
        for (int i = 0; i < vista.getcmbRol().getItemCount(); i++) {
            Rol rol = (Rol) vista.getcmbRol().getItemAt(i);
            if (rol.getId_rol() == idRolActual) {
                vista.getcmbRol().setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardarCambios() {
        try {
            String nombre = vista.getTxtNombre().getText().trim();
            String usuario = vista.getTxtUsuario().getText().trim();
            String clave = new String(vista.getTxtClave().getPassword());
            Rol rolSeleccionado = (Rol) vista.getcmbRol().getSelectedItem();

            if (nombre.isEmpty() || usuario.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Complete todos los campos");
                return;
            }

            if (rolSeleccionado == null) {
                JOptionPane.showMessageDialog(vista, "Seleccione un rol válido");
                return;
            }

            if (!usuario.equals(trabajadorActual.getUsuario())) {
                if (dao.existeUsuario(usuario)) {
                    JOptionPane.showMessageDialog(vista, 
                        "❌ El nombre de usuario '" + usuario + "' ya está registrado.\n\n" +
                        "Por favor, elija un nombre de usuario diferente.", 
                        "Usuario duplicado", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Confirmación antes de guardar
            int confirmacion = JOptionPane.showConfirmDialog(
                vista, 
                "¿Está seguro que desea guardar los cambios para el usuario:\n" +
                "ID: " + trabajadorActual.getId_trabajador() + "\n" +
                "Nombre: " + nombre + "?",
                "Confirmar cambios",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                trabajadorActual.setNombre(nombre);
                trabajadorActual.setUsuario(usuario);
                trabajadorActual.setClave(clave);
                trabajadorActual.setId_rol(rolSeleccionado.getId_rol());

                if (dao.actualizar(trabajadorActual)) {
                    JOptionPane.showMessageDialog(vista, 
                        "Usuario actualizado correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    if (menuPrincipal != null) {
                        menuPrincipal.actualizarTablaUsuarios();
                    }
                    
                    limpiarCampos();
                    habilitarEdicion(false);
                    
                } else {
                    JOptionPane.showMessageDialog(vista, 
                        "Error al actualizar el usuario",
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
        vista.getTxtUsuario().setEnabled(habilitar);
        vista.getTxtClave().setEnabled(habilitar);
        vista.getcmbRol().setEnabled(habilitar);
        vista.getBtnGuardar().setEnabled(habilitar);
    }

    private void limpiarCampos() {
        vista.getTxtIdUsuario().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtUsuario().setText("");
        vista.getTxtClave().setText("");
        vista.getcmbRol().removeAllItems();
    }
}