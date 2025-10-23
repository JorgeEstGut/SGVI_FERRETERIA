package Controller;

import Model.Ventas;
import Model.VentasDAO;
import View.frmEliminarVenta;
import javax.swing.JOptionPane;
import View.frmMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EliminarVentaController {
    private frmEliminarVenta vista;
    private VentasDAO dao;
    private frmMenu menuPrincipal;
    
    public EliminarVentaController (frmEliminarVenta vista, VentasDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        initComponent();
    }
    
    public void initComponent() {       
        vista.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarVentaPorID();
            }
        });
    }
    
    private void buscarVentaPorID() {
        try {
            String idText = vista.getTxtIdVenta().getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese un ID válido");
                return;
            }

            int id = Integer.parseInt(idText);
            Ventas ventas = dao.buscarPorId(id);

            if (ventas != null) {              
                eliminarVenta();
                
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró una factura con el ID: " + id);
                limpiarCampos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido");
            limpiarCampos();
        }
    }
    
    private void eliminarVenta() {
        String idTexto = vista.getTxtIdVenta().getText().trim();

        // Validar campo
        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese el ID de la venta a eliminar.");
            return;
        }

        try {
            int idVenta = Integer.parseInt(idTexto);

            int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de que desea eliminar la venta con ID: " + idVenta + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean eliminado = dao.eliminarVenta(idVenta);

                if (eliminado) {
                    JOptionPane.showMessageDialog(vista, "Venta eliminada exitosamente.");
                    vista.getTxtIdVenta().setText("");
                    menuPrincipal.actualizarTablaVentas();
                    menuPrincipal.actualizarDashboard();
                    menuPrincipal.actualizarListaFacturas();
                    menuPrincipal.actualizarTablaProductos();
                } else {
                    JOptionPane.showMessageDialog(vista, "No se pudo eliminar la venta. Verifique el ID.");
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido.");
        }
        
    }
    private void limpiarCampos() {
            vista.getTxtIdVenta().setText("");
    }
}
