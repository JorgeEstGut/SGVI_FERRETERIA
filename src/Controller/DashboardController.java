package Controller;

import Model.FacturaDAO;
import Model.ProductoDAO;
import Model.ProductoStockBajo;
import View.frmMenu;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class DashboardController {
    
    private frmMenu vista;
    private FacturaDAO facturaDAO;
    private ProductoDAO productoDAO;
    
    public DashboardController(frmMenu vista) {
        this.vista = vista;
        this.facturaDAO = new FacturaDAO();
        this.productoDAO = new ProductoDAO();
        initController();
    }
    
    private void initController() {
        cargarDashboard();
    }
    
    public void cargarDashboard() {
        cargarVentasDelDia();
        cargarFacturasDelDia();
        cargarCantidadProductos();
        cargarProductosStockBajo();
    }
    
    private void cargarVentasDelDia() {
        try {
            double totalVendido = facturaDAO.obtenerTotalVendidoHoy();
            vista.getTxtVentasDia().setText(String.format("$%,.0f", totalVendido));
            System.out.println("Total vendido hoy: $" + totalVendido);
        } catch (Exception e) {
            vista.getTxtVentasDia().setText("$0.00");
            System.out.println("Error cargando total vendido: " + e.getMessage());
        }
    }
    
    private void cargarFacturasDelDia() {
        try {
            int facturasHoy = facturaDAO.obtenerFacturasGeneradasHoy();
            vista.getTxtGananciaDia().setText(String.valueOf(facturasHoy));
            System.out.println("Facturas generadas hoy: " + facturasHoy);
        } catch (Exception e) {
            vista.getTxtGananciaDia().setText("0");
            System.out.println("Error cargando facturas generadas: " + e.getMessage());
        }
    }
    
    private void cargarCantidadProductos() {
        try {
            int cantidad = facturaDAO.obtenerCantidadProductosVendidosHoy();

            vista.getTxtCantidadProductos().setText(String.valueOf(cantidad));
        } catch (Exception e) {
            vista.getTxtCantidadProductos().setText("0");
            System.out.println("Error cargando cantidad productos: " + e.getMessage());
        }
    }
    
    private void cargarProductosStockBajo() {
        try {
            List<ProductoStockBajo> productos = productoDAO.obtenerProductosStockBajo();
            DefaultTableModel modelo = new DefaultTableModel();
            
            // Definir columnas
            modelo.setColumnIdentifiers(new Object[]{
                "Producto", 
                "Stock Actual", 
                "Stock Mínimo", 
                "Estado"
            });
            
            // Llenar tabla
            for (ProductoStockBajo producto : productos) {
                String estado = producto.getStockActual() == 0 ? "AGOTADO" : "BAJO STOCK";
                modelo.addRow(new Object[]{
                    producto.getNombre(),
                    producto.getStockActual(),
                    producto.getStockMinimo(),
                    estado
                });
            }
            
            vista.getTblStockBajo().setModel(modelo);
            configurarTablaStockBajo();
            
        } catch (Exception e) {
            System.out.println("Error cargando productos stock bajo: " + e.getMessage());
            // Crear modelo vacío en caso de error
            DefaultTableModel modeloVacio = new DefaultTableModel();
            modeloVacio.setColumnIdentifiers(new Object[]{"Producto", "Stock Actual", "Stock Mínimo", "Estado"});
            vista.getTblStockBajo().setModel(modeloVacio);
        }
    }
    
    private void configurarTablaStockBajo() {
        // Aplicar el mismo diseño que usas en cargarUsuarios()
        vista.getTblStockBajo().setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16)); 
        vista.getTblStockBajo().setRowHeight(30); // Aumenta la altura de las filas

        // Cambiar el tamaño de la fuente del encabezado
        javax.swing.table.JTableHeader header = vista.getTblStockBajo().getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

        // Centrar texto en las celdas
        javax.swing.table.DefaultTableCellRenderer centrado = new javax.swing.table.DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        for (int i = 0; i < vista.getTblStockBajo().getColumnCount(); i++) {
            vista.getTblStockBajo().getColumnModel().getColumn(i).setCellRenderer(centrado);
        }

        // Renderer personalizado para colorear filas según estado (MANTENIENDO LOS COLORES)
        vista.getTblStockBajo().setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
                centrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

                if (!isSelected) {
                    String estado = (String) table.getValueAt(row, 3);
                    if ("AGOTADO".equals(estado)) {
                        c.setBackground(new java.awt.Color(255, 200, 200)); // Rojo claro
                    } else if ("BAJO STOCK".equals(estado)) {
                        c.setBackground(new java.awt.Color(255, 255, 200)); // Amarillo claro
                    } else {
                        c.setBackground(java.awt.Color.WHITE);
                    }
                }

                return c;
            }
        });

        // Ajustar anchos de columnas
        vista.getTblStockBajo().getColumnModel().getColumn(0).setPreferredWidth(250); // Producto
        vista.getTblStockBajo().getColumnModel().getColumn(1).setPreferredWidth(120); // Stock Actual
        vista.getTblStockBajo().getColumnModel().getColumn(2).setPreferredWidth(120); // Stock Mínimo
        vista.getTblStockBajo().getColumnModel().getColumn(3).setPreferredWidth(150); // Estado
    }
    
    // Método para actualizar el dashboard
    public void actualizarDashboard() {
        cargarDashboard();
    }
}