package Controller;

import Model.Proveedor;
import Model.ProveedorDAO;
import View.frmMenu;
import View.frmNuevoProveedor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


public class ProveedoresController {
   private frmNuevoProveedor vista;
   private ProveedorDAO dao;
   private frmMenu menuPrincipal;

    public ProveedoresController(frmNuevoProveedor vista, ProveedorDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        initController();
    }
   
   public void initController() {
       vista.getBtnRegistrar().addActionListener(new ActionListener () {
            @Override
                public void actionPerformed(ActionEvent e) {
                    registrarProveedor();
                }   
       });
   }
   
   public void registrarProveedor(){
       String nombre = vista.getTxtNombre().getText();
       String email = vista.getTxtEmail().getText();
       
       if (nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete todos los campos.");
            return;
        }
       
       if(dao.existeProveedor(nombre)){
           JOptionPane.showMessageDialog(vista, 
                "❌ El proveedor '" + nombre + "' ya está registrado.\n\n", 
                "Proveedor duplicado", 
                JOptionPane.WARNING_MESSAGE);
            return;
       }
       
        Proveedor p = new Proveedor();
        p.setNombre_proveedor(nombre);
        p.setEmail(email);

        if (dao.insertar(p)) {
            JOptionPane.showMessageDialog(vista, "Proveedor registrado correctamente.");
            limpiarCampos();
            if (menuPrincipal != null) {
                menuPrincipal.actualizarTablaProveedores();
            }

        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar proveedor.");
        }
   }
   
   private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtEmail().setText("");
    }
}
