package Controller;

import Model.FacturaDAO;
import Model.FacturaDetalle;
import View.frmFactura;
import View.frmMenu;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class FacturaController {
 
    private frmFactura vista;
    private FacturaDAO dao;
    private frmMenu menuPrincipal;
    private int idFacturaSeleccionada;

    // Constructor modificado para recibir el ID de la factura
    public FacturaController(frmFactura vista, FacturaDAO dao, frmMenu menuPrincipal, int idFactura) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        this.idFacturaSeleccionada = idFactura;
        initController();
    }

    private void initController() {
       cargarDatosFactura();
    }

    private void cargarDatosFactura() {
        if (idFacturaSeleccionada <= 0) {
            System.out.println("ID de factura no válido");
            return;
        }

        List<FacturaDetalle> detalles = dao.obtenerDetallesFactura(idFacturaSeleccionada);
        
        if (detalles.isEmpty()) {
            System.out.println("No se encontraron detalles para la factura ID: " + idFacturaSeleccionada);
            return;
        }

        // Tomar el primer detalle para llenar los campos generales (todos tienen los mismos datos generales)
        FacturaDetalle primerDetalle = detalles.get(0);
        
        // Llenar los campos de texto
        vista.getTxtidFactura().setText(String.valueOf(primerDetalle.getIdFactura()));
        vista.getTxtfecha().setText(primerDetalle.getFecha());
        vista.getTxtnombreCliente().setText(primerDetalle.getNombreCliente());
        vista.getTxtcedula().setText(primerDetalle.getCedula());
        vista.getTxtnombreTrabajador().setText(primerDetalle.getNombreTrabajador());
        vista.getTxtTotal().setText(String.format("%.2f", primerDetalle.getTotal()));

        // Llenar la tabla con los detalles
        cargarTablaDetalles(detalles);
    }

    private void cargarTablaDetalles(List<FacturaDetalle> detalles) {
        DefaultTableModel modelo = new DefaultTableModel();
        
        // Definir las columnas para la tabla de detalles
        modelo.setColumnIdentifiers(new Object[]{
            "Producto", 
            "Cantidad", 
            "Precio Unitario", 
            "Subtotal"
        });

        // Llenar la tabla con los detalles
        for (FacturaDetalle detalle : detalles) {
            modelo.addRow(new Object[]{
                detalle.getNombreProducto(),
                detalle.getCantidad(),
                String.format("$%.2f", detalle.getPrecio()),
                String.format("$%.2f", detalle.getSubtotal())
            });
        }

        vista.getTblFacturaVista().setModel(modelo);
        
        // Configuración de la tabla
        vista.getTblFacturaVista().setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14)); 
        vista.getTblFacturaVista().setRowHeight(25);

        // Configurar el encabezado
        javax.swing.table.JTableHeader header = vista.getTblFacturaVista().getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        
        // Centrar texto en las celdas
        javax.swing.table.DefaultTableCellRenderer centrado = new javax.swing.table.DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Aplicar centrado a todas las columnas
        for (int i = 0; i < vista.getTblFacturaVista().getColumnCount(); i++) {
            vista.getTblFacturaVista().getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
        
        // Ajustar el ancho de las columnas
        vista.getTblFacturaVista().getColumnModel().getColumn(0).setPreferredWidth(200); // Producto
        vista.getTblFacturaVista().getColumnModel().getColumn(1).setPreferredWidth(80);  // Cantidad
        vista.getTblFacturaVista().getColumnModel().getColumn(2).setPreferredWidth(120); // Precio
        vista.getTblFacturaVista().getColumnModel().getColumn(3).setPreferredWidth(120); // Subtotal
    }
    
    public void actualizarTablaFactura() {
        cargarDatosFactura();
    }
}