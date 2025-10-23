package Controller;

import Model.Ventas;
import Model.VentasDAO;
import Model.Clientes;
import Model.DetalleVenta;
import Model.Producto;
import Model.ProductoDAO;
import Model.TicketPDFGeneratorSimple;
import View.frmMenu;
import View.frmNuevaVenta;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class VentasController implements ActionListener {
    private frmNuevaVenta vista;
    private VentasDAO dao;
    private frmMenu menuPrincipal;
    private DefaultTableModel modeloDetalle;
    private String nombreTrabajador;
    private int idTrabajador;
    private ProductoDAO stock;
    
    public VentasController(frmNuevaVenta vista, VentasDAO dao, frmMenu menuPrincipal, String nombreTrabajador, int idTrabajador, ProductoDAO stock) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        this.nombreTrabajador = nombreTrabajador;
        this.idTrabajador = idTrabajador;
        this.stock = stock;
        initComponent();
        cargarClientes();
        cargarProductos();
        cargarDatosTrabajador();
    }
    
    public void initComponent() {
        // Configurar estado inicial de botones y campos
        vista.getBtnRegistrar().setEnabled(false);  // Deshabilitar inicialmente
        vista.getTxtEfectivo().setEditable(false);  // Bloquear efectivo inicialmente
        
        vista.getBtnRegistrar().addActionListener(new ActionListener () {
            @Override
                public void actionPerformed(ActionEvent e) {
                    registrarVenta();
                }  
       });
       
       vista.getBtnVerificar().addActionListener(new ActionListener () {
            @Override
                public void actionPerformed(ActionEvent e) {
                    verificarVenta();
                }  
       });
   
       vista.getBtnAgregar().addActionListener(new ActionListener () {
            @Override
                public void actionPerformed(ActionEvent e) {
                    agregarProducto();
                }   
       });
       
       vista.getCmbCliente().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Clientes clienteSeleccionado = (Clientes) vista.getCmbCliente().getSelectedItem();
            if (clienteSeleccionado != null) {
                vista.getTxtCedula().setText(clienteSeleccionado.getCedula());
                vista.getTxtTelefono().setText(clienteSeleccionado.getTelefono());
            }
        }
        });
        vista.getCmbProducto().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Producto productoSeleccionado = (Producto) vista.getCmbProducto().getSelectedItem();
            vista.getTxtPrecio().setText(String.valueOf(productoSeleccionado.getPrecio()));
        }
        });
        
        // Agregar listener para calcular cambio automáticamente
        vista.getTxtEfectivo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularCambio();
            }
        });
        
        // Agregar listener para calcular cambio cuando se escribe en el campo
        vista.getTxtEfectivo().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularCambio();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularCambio();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calcularCambio();
            }
        });

        modeloDetalle = new DefaultTableModel();
        modeloDetalle.addColumn("Producto");
        modeloDetalle.addColumn("Precio Unitario");
        modeloDetalle.addColumn("Cantidad");
        modeloDetalle.addColumn("Subtotal");
        vista.getjTable1().setModel(modeloDetalle);

        // Configurar la tabla
        configurarTablaDetalles();
   }
    
    private void configurarTablaDetalles() {
    // Configurar la tabla para que muestre los números formateados
    vista.getjTable1().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Formatear columnas de precios y subtotales
            if (column == 1 || column == 3) { // Columnas de Precio y Subtotal
                if (value instanceof Double) {
                    setText(String.format("$%.0f", (Double) value));
                } else if (value instanceof Float) {
                    setText(String.format("$%.0f", (Float) value));
                }
                setHorizontalAlignment(JLabel.RIGHT);
            } else if (column == 2) { // Columna de Cantidad
                setHorizontalAlignment(JLabel.CENTER);
            } else { // Columna de Producto
                setHorizontalAlignment(JLabel.LEFT);
            }
            
            return c;
        }
    });
}
    
    // Método para cargar los datos del trabajador en el formulario
    private void cargarDatosTrabajador() {
        if (nombreTrabajador != null && !nombreTrabajador.isEmpty()) {
            vista.getTxtTrabajador().setText(nombreTrabajador);
            // Si quieres mostrar también el ID, puedes hacerlo:
            // vista.getTxtTrabajador().setText(nombreTrabajador + " (ID: " + idTrabajador + ")");
        }

        // Hacer el campo de solo lectura
        vista.getTxtTrabajador().setEditable(false);
        vista.getTxtTrabajador().setBackground(new java.awt.Color(240, 240, 240));
    }
    
   private void cargarClientes() {
    List<Clientes> clientes = dao.listarClientes();
    vista.getCmbCliente().removeAllItems();

    for (Clientes c : clientes) {
        vista.getCmbCliente().addItem(c);
    }

    // Establecer por defecto "Comprador Final"
    for (int i = 0; i < vista.getCmbCliente().getItemCount(); i++) {
        Clientes cli = (Clientes) vista.getCmbCliente().getItemAt(i);
        if ("Comprador Final".equalsIgnoreCase(cli.getNombre())) {
            vista.getCmbCliente().setSelectedIndex(i);
            vista.getTxtCedula().setText(cli.getCedula());
            vista.getTxtTelefono().setText(cli.getTelefono());
            break;
        }
    }
}
   
    private void actualizarDatosCliente() {
        Clientes cliente = (Clientes) vista.getCmbCliente().getSelectedItem();
        if (cliente != null) {
            vista.getTxtCedula().setText(cliente.getCedula());
            vista.getTxtTelefono().setText(cliente.getTelefono());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // 🔸 Cuando se selecciona un cliente
        if (e.getSource() == vista.getCmbCliente()) {
            Clientes cliente = (Clientes) vista.getCmbCliente().getSelectedItem();
            if (cliente != null) {
                vista.getTxtCedula().setText(cliente.getCedula());
                vista.getTxtTelefono().setText(cliente.getTelefono());
            }
        }
        
        if (e.getSource() == vista.getCmbProducto()) {
            Producto productos = (Producto) vista.getCmbProducto().getSelectedItem();
            vista.getTxtPrecio().setText(String.valueOf(productos.getPrecio()));
        }
        
        if (e.getSource() == vista.getBtnRegistrar()) {
            registrarVenta();
        }
    }
    
    private void registrarVenta() {
        Clientes cliente = (Clientes) vista.getCmbCliente().getSelectedItem();
        int idCliente = (cliente != null) ? cliente.getId_cliente() : 1;
        int idTrabajador = this.idTrabajador;

        // Calcular el total y crear lista de detalles
        double total = 0;
        List<DetalleVenta> detalles = new ArrayList<>();

        for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
            String nombreProducto = (String) modeloDetalle.getValueAt(i, 0);
            double precio = (Double) modeloDetalle.getValueAt(i, 1);
            int cantidad = (Integer) modeloDetalle.getValueAt(i, 2);
            double subtotal = (Double) modeloDetalle.getValueAt(i, 3);

            total += subtotal;

            // Crear detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setId_producto(obtenerIdProductoPorNombre(nombreProducto)); // Necesitas este método
            detalle.setCantidad(cantidad);
            detalle.setPrecio(precio);
            detalle.setSubtotal(subtotal);

            detalles.add(detalle);
        }

        System.out.println("Total calculado desde tabla: " + total);

        if (total <= 0) {
            JOptionPane.showMessageDialog(vista, "El total debe ser mayor a 0.");
            return;
        }

        // Las validaciones ya se hicieron en verificarVenta()

        LocalDate FechaActual = LocalDate.now();
        Date FechaSQL = Date.valueOf(FechaActual);

        Ventas v = new Ventas();
        v.setId_cliente(idCliente);
        v.setId_trabajador(idTrabajador);
        v.setFecha(FechaSQL);
        v.setTotal(total);
        v.setDetalleVenta(detalles); // ← Pasar los detalles

        // Registrar venta y obtener ID de factura
        int idFacturaGenerada = dao.registrarYRetornarId(v);

        if (idFacturaGenerada > 0) {
            // Generar ticket PDF
            generarTicketPDF(idFacturaGenerada, v, detalles);
            
            JOptionPane.showMessageDialog(vista, "Venta registrada con éxito\nTicket PDF generado automáticamente");

            if (menuPrincipal != null) {
                menuPrincipal.actualizarListaFacturas();
                menuPrincipal.actualizarTablaVentas();
                menuPrincipal.actualizarDashboard();
                menuPrincipal.actualizarTablaProductos();
            }

            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar la venta");
        }   
    }
    

    private int obtenerIdProductoPorNombre(String nombreProducto) {
        ProductoDAO productoDAO = new ProductoDAO();
        return productoDAO.obtenerIdPorNombre(nombreProducto);
    }
    
    
    private void agregarProducto() {
        Producto producto = (Producto) vista.getCmbProducto().getSelectedItem();
        String precioStr = vista.getTxtPrecio().getText();
        String cantidadStr = vista.getTxtCantidad().getText();
        int stockDisponible = stock.obtenerStock(producto.getId_producto());

        if (producto == null || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete los datos del producto antes de agregar.");
            return;
        }

        if (Integer.parseInt(cantidadStr) > stockDisponible) {
            JOptionPane.showMessageDialog(vista,
                "No hay stock suficiente. Stock disponible: " + stockDisponible);
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);
            double subtotal = precio * cantidad;

            // Agregar la fila con 4 columnas: Producto, Precio, Cantidad, Subtotal
            modeloDetalle.addRow(new Object[]{
                producto.getNombre(),  // Columna 0: Nombre del producto
                precio,                // Columna 1: Precio unitario
                cantidad,              // Columna 2: Cantidad
                subtotal               // Columna 3: Subtotal
            });

            // Calcular el nuevo total sumando todos los subtotales
            double total = 0;   
            for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
                // Obtener el subtotal de la columna 3
                double subtotalFila = (double) modeloDetalle.getValueAt(i, 3);
                total += subtotalFila;
            }
            vista.getTxtTotal().setText(String.format("$%.0f", total));
            
            // Habilitar el campo de efectivo cuando hay productos
            vista.getTxtEfectivo().setEditable(true);
            vista.getTxtEfectivo().setBackground(new java.awt.Color(244, 244, 244));
            
            // Recalcular el cambio automáticamente
            calcularCambio();

            // Limpiar solo los campos de producto, NO la tabla
            vista.getTxtCantidad().setText("");
            vista.getTxtPrecio().setText("");
            vista.getCmbProducto().setSelectedIndex(0);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese valores numéricos válidos para precio y cantidad.");
        }
    }
    private void cargarProductos() {
        List<Producto> productos = dao.listarProductos();
        vista.getCmbProducto().removeAllItems();
        for (Producto p : productos) {
            vista.getCmbProducto().addItem(p);
        }
    }
    
    private void limpiarFormulario() {
        modeloDetalle.setRowCount(0);
        vista.getTxtTotal().setText("");
        vista.getTxtCantidad().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtEfectivo().setText("");
        vista.getTxtCambio().setText("");
        vista.getCmbCliente().setSelectedIndex(0);
        vista.getCmbProducto().setSelectedIndex(0);
        
        // Resetear botones y campos
        vista.getBtnRegistrar().setEnabled(false);
        vista.getBtnVerificar().setEnabled(true);
        
        // Bloquear el campo de efectivo hasta que se agreguen productos
        vista.getTxtEfectivo().setEditable(false);
        vista.getTxtEfectivo().setBackground(new java.awt.Color(244, 244, 244));
        
        // El campo trabajador se mantiene porque es el mismo usuario
    }
    
    /**
     * Verifica la venta: valida efectivo, calcula cambio y habilita registro
     */
    private void verificarVenta() {
        try {
            // Obtener valores
            String totalStr = vista.getTxtTotal().getText().replace("$", "").trim();
            String efectivoStr = vista.getTxtEfectivo().getText().replace("$", "").trim();
            
            // Validar que hay productos
            if (modeloDetalle.getRowCount() == 0) {
                JOptionPane.showMessageDialog(vista, "Debe agregar al menos un producto a la venta.");
                return;
            }
            
            // Validar que se ingresó efectivo
            if (efectivoStr.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el efectivo recibido.");
                return;
            }
            
            // Validar que hay total
            if (totalStr.isEmpty() || totalStr.equals("0")) {
                JOptionPane.showMessageDialog(vista, "El total debe ser mayor a 0.");
                return;
            }
            
            // Convertir a números
            double total = Double.parseDouble(totalStr);
            double efectivo = Double.parseDouble(efectivoStr);
            double cambio = efectivo - total;
            
            // Validar que el efectivo cubra el total
            if (efectivo < total) {
                JOptionPane.showMessageDialog(vista, 
                    "El efectivo recibido ($" + String.format("%.0f", efectivo) + 
                    ") no cubre el total de la venta ($" + String.format("%.0f", total) + 
                    ").\nFalta: $" + String.format("%.0f", Math.abs(cambio)));
                return;
            }
            
            // Si todo está correcto, calcular y mostrar el cambio
            vista.getTxtCambio().setText(String.format("$%.0f", cambio));
            
            // Habilitar el botón de registrar
            vista.getBtnRegistrar().setEnabled(true);
            vista.getBtnVerificar().setEnabled(false);
            
            // Mostrar mensaje de confirmación
            JOptionPane.showMessageDialog(vista, 
                "✅ Venta verificada correctamente\n" +
                "Total: $" + String.format("%.0f", total) + "\n" +
                "Efectivo: $" + String.format("%.0f", efectivo) + "\n" +
                "Cambio: $" + String.format("%.0f", cambio) + "\n\n" +
                "Ahora puede registrar la venta.");
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Ingrese valores numéricos válidos.");
        }
    }
    
    /**
     * Calcula el cambio automáticamente cuando se ingresa el efectivo
     */
    private void calcularCambio() {
        try {
            String totalStr = vista.getTxtTotal().getText().replace("$", "").trim();
            String efectivoStr = vista.getTxtEfectivo().getText().replace("$", "").trim();
            
            // Solo calcular si ambos campos tienen valores válidos
            if (!totalStr.isEmpty() && !efectivoStr.isEmpty() && !totalStr.equals("0")) {
                double total = Double.parseDouble(totalStr);
                double efectivo = Double.parseDouble(efectivoStr);
                double cambio = efectivo - total;
                
                if (cambio >= 0) {
                    vista.getTxtCambio().setText(String.format("$%.0f", cambio));
                } else {
                    vista.getTxtCambio().setText("$0");
                }
            } else {
                // Si no hay valores válidos, limpiar el cambio
                vista.getTxtCambio().setText("");
            }
        } catch (NumberFormatException e) {
            // Si hay error en el formato, limpiar el cambio
            vista.getTxtCambio().setText("");
        }
    }
    
    /**
     * Genera el ticket PDF para la venta registrada
     */
    private void generarTicketPDF(int idFactura, Ventas venta, List<DetalleVenta> detalles) {
        try {
            // Obtener fecha y hora actual
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("h:mm a");
            String fechaCompleta = ahora.format(formatterFecha) + "  " + ahora.format(formatterHora);
            
            // Obtener valores reales del formulario
            double total = venta.getTotal();
            String efectivoStr = vista.getTxtEfectivo().getText().replace("$", "").trim();
            String cambioStr = vista.getTxtCambio().getText().replace("$", "").trim();
            
            double efectivo = 0;
            double cambio = 0;
            
            try {
                efectivo = Double.parseDouble(efectivoStr);
                cambio = Double.parseDouble(cambioStr);
            } catch (NumberFormatException e) {
                // Si no se ingresaron valores, usar valores por defecto
                efectivo = total + 2000;
                cambio = 2000;
            }
            
            // Generar el ticket PDF
            boolean ticketGenerado = TicketPDFGeneratorSimple.generarTicketPDF(
                idFactura,
                fechaCompleta,
                nombreTrabajador,
                total,
                efectivo,
                cambio,
                detalles
            );
            
            if (ticketGenerado) {
                System.out.println("✅ Ticket PDF generado exitosamente para factura #" + idFactura);
            } else {
                System.out.println("❌ Error al generar ticket PDF para factura #" + idFactura);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al generar ticket PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
