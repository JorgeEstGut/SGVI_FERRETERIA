package Controller;

import Model.Clientes;
import Model.VentasDAO;
import View.frmNuevoCliente;
import View.frmNuevaVenta;
import View.frmMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class NuevoClienteController {
   
   private frmNuevoCliente vista;
   private VentasDAO dao;
   private frmMenu menuPrincipal;
   
   public NuevoClienteController (frmNuevoCliente vista, VentasDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        initController();
    }
   
   public void initController() {
       vista.getBtnRegistrar().addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarCliente();
            }   
       });
   }
   
    public void registrarCliente(){
       String nombre = vista.getTxtNombre().getText();
       String telefono = vista.getTxtTelefono().getText();
       String cedula = vista.getTxtCedula().getText();
       
       if (nombre.isEmpty() || cedula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese el nombre y cedula del cliente.");
            return;
        }
       
       if(dao.existeCliente(cedula)){
           JOptionPane.showMessageDialog(vista, 
                "❌ El cliente '" + nombre + "' ya está registrado.\n\n", 
                "Cliente duplicado", 
                JOptionPane.WARNING_MESSAGE);
            return;
       }
       
       Clientes nuevoCliente = new Clientes();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setTelefono(telefono);
        nuevoCliente.setCedula(cedula);
        
        boolean registrado = dao.registrarCliente(nuevoCliente);
        
        if (registrado) {
        JOptionPane.showMessageDialog(vista, "Cliente registrado correctamente.");
        limpiarCampos();
    } else {
        JOptionPane.showMessageDialog(vista, "Error al registrar el cliente.");
    }
    }
    
    private void limpiarCampos() {
            vista.getTxtNombre().setText("");
            vista.getTxtCedula().setText("");
            vista.getTxtTelefono().setText("");
    }
}
