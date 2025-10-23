package Controller;

import Model.Trabajador;
import Model.TrabajadorDAO;
import Model.Rol;
import View.frmNuevoUsuario;
import View.frmMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class UsuariosController {

    private frmNuevoUsuario vista;
    private TrabajadorDAO dao;
    private frmMenu menuPrincipal;

    public UsuariosController(frmNuevoUsuario vista, TrabajadorDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        initController();
    }

    private void initController() {
        vista.getBtnRegistrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String usuario = vista.getTxtUsuario().getText();
        String clave = new String(vista.getTxtClave().getPassword());
        Rol rol = (Rol) vista.getCmbRol().getSelectedItem();
        String nombre = vista.getTxtNombre().getText();

        if (usuario.isEmpty() || clave.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete todos los campos.");
            return;
        }
        
        if (dao.existeUsuario(usuario)) {
            JOptionPane.showMessageDialog(vista, 
                "❌ El nombre de usuario '" + usuario + "' ya está registrado.\n\n" +
                "Por favor, elija un nombre de usuario diferente.", 
                "Usuario duplicado", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Trabajador t = new Trabajador();
        t.setUsuario(usuario);
        t.setClave(clave);
        t.setNombre(nombre);
        t.setId_rol(rol.getId_rol());

        if (dao.insertar(t)) {
            JOptionPane.showMessageDialog(vista, "Usuario registrado correctamente.");
            vista.limpiarCampos();
            if (menuPrincipal != null) {
                menuPrincipal.actualizarTablaUsuarios();
            }

        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar usuario.");
        }
    }
}