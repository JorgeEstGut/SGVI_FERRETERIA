package Controller;

import Model.TipoProducto;
import Model.ProductoDAO;
import View.frmMenu;
import View.frmNuevoTipo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


public class NuevoTipoController {
   private frmNuevoTipo vista;
   private ProductoDAO dao;
   private frmMenu menuPrincipal;

    public NuevoTipoController(frmNuevoTipo vista, ProductoDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        initController();
    }
   
   public void initController() {
       vista.getBtnRegistrar().addActionListener(new ActionListener () {
            @Override
                public void actionPerformed(ActionEvent e) {
                    registrarNuevoTipoProducto();
                }   
       });
   }
   
   public void registrarNuevoTipoProducto(){
       String nombre = vista.getTxtNombre().getText();
 
       
 
       
       if(dao.existeTipoProductos(nombre)){
           JOptionPane.showMessageDialog(vista, 
                "❌ El tipo '" + nombre + "' ya está registrado.\n\n", 
                "Producto duplicado", 
                JOptionPane.WARNING_MESSAGE);
            return;
       }
       
        TipoProducto p = new TipoProducto();
        p.setNombre_tipo(nombre);


        if (dao.insertarTipo(p)) {
            JOptionPane.showMessageDialog(vista, "Tipo de producto registrado correctamente.");
            limpiarCampos();
        

        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar el tipo producto.");
        }
   }
   
   private void limpiarCampos() {
        vista.getTxtNombre().setText("");

    }
}
