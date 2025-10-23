package Controller;

import Model.Clientes;
import Model.Ventas;
import Model.VentasDAO;
import View.frmEditarVenta;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import javax.swing.JOptionPane;
import Model.DetalleVenta;
import Model.Producto;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import Model.ProductoDAO;
import View.frmMenu;
import javax.swing.table.DefaultTableModel;
import Model.ProductoDAO;

public class EditarVentaController {
    
    private frmEditarVenta vista;
    private VentasDAO dao;
    private Ventas ventaActual;
    private frmMenu menuPrincipal;
    private DefaultTableModel modeloDetalle;
    private String nombreTrabajador;
    private int idTrabajador;
    private int idFacturaActual;
    
    public EditarVentaController (frmEditarVenta vista, VentasDAO dao, frmMenu menuPrincipal, String nombreTrabajador, int idTrabajador) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        this.nombreTrabajador = nombreTrabajador;
        this.idTrabajador = idTrabajador;
        this.idFacturaActual = 0;
        initController();
        cargarClientes();
        cargarProductos();
        cargarDatosTrabajador();
    }
    
    private void initController() {
        vista.getBtnEditar().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guardarCambios();
            }
        });
        
        vista.getBtnAgregar().addActionListener(new ActionListener() {
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
        
        vista.getBtnBuscarId().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFacturaPorID();
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
    
    private void buscarFacturaPorID() {
        try {
            String idText = vista.getTxtIdFactura().getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese un ID de factura válido");
                return;
            }

            int idFactura = Integer.parseInt(idText);
            Ventas venta = dao.buscarPorId(idFactura);

            if (venta != null) {
                this.idFacturaActual = idFactura;
                cargarDatosFactura(venta);
                JOptionPane.showMessageDialog(vista, "Factura encontrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró una factura con el ID: " + idFactura);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido");
        }
    }
    
    private void cargarDatosFactura(Ventas venta) {
    try {
        System.out.println("Cargando datos de factura ID: " + venta.getId_factura());
        
        // Cargar datos básicos de la factura
        vista.getTxtTrabajador().setText(venta.getNombre_trabajadores());
        vista.getTxtTotal().setText(String.format("%.2f", venta.getTotal()));
        
        // Seleccionar cliente en combobox
        seleccionarCliente(venta.getId_cliente());
        
        // Cargar detalles de la factura en la tabla
        cargarDetallesFactura(venta.getId_factura());
        
        System.out.println("Datos de factura cargados exitosamente");
        
    } catch (Exception e) {
        System.out.println("Error en cargarDatosFactura: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(vista, "Error al cargar los datos de la factura");
    }
}
    
    private void seleccionarCliente(int idCliente) {
        for (int i = 0; i < vista.getCmbCliente().getItemCount(); i++) {
            Clientes cliente = (Clientes) vista.getCmbCliente().getItemAt(i);
            if (cliente.getId_cliente() == idCliente) {
                vista.getCmbCliente().setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void cargarDetallesFactura(int idFactura) {
        // Necesitas un método en VentasDAO para obtener los detalles de una factura
        List<DetalleVenta> detalles = dao.obtenerDetallesPorFactura(idFactura);
        modeloDetalle.setRowCount(0); // Limpiar tabla
        
        for (DetalleVenta detalle : detalles) {
            // Necesitas un método para obtener el nombre del producto por ID
            String nombreProducto = dao.obtenerNombreProducto(detalle.getId_producto());
            
            modeloDetalle.addRow(new Object[]{
                nombreProducto,
                detalle.getPrecio(),
                detalle.getCantidad(),
                detalle.getSubtotal()
            });
        }
        
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
    
    private void guardarCambios() {
        if (this.idFacturaActual <= 0) {
            JOptionPane.showMessageDialog(vista, "Primero busque una factura válida para editar");
            return;
        }

        // DEBUG: Mostrar estado actual
        System.out.println("=== GUARDAR CAMBIOS ===");
        System.out.println("ID Factura: " + this.idFacturaActual);
        System.out.println("Filas en tabla: " + modeloDetalle.getRowCount());

        Clientes cliente = (Clientes) vista.getCmbCliente().getSelectedItem();
        int idCliente = (cliente != null) ? cliente.getId_cliente() : 1;
        System.out.println("ID Cliente: " + idCliente);

        double total;
        try {
            String totalText = vista.getTxtTotal().getText().trim();
            System.out.println("Texto del total: '" + totalText + "'");

            // Limpiar el texto
            totalText = totalText.replace("$", "").replace(",", "").trim();
            System.out.println("Texto limpio: '" + totalText + "'");

            total = Double.parseDouble(totalText);
            System.out.println("Total convertido: " + total);

        } catch (NumberFormatException e) {
            System.out.println("❌ Error al convertir total: " + e.getMessage());
            JOptionPane.showMessageDialog(vista, 
                "Error en el campo total: " + e.getMessage() + 
                "\nAsegúrese de que sea un número válido.", 
                "Error en total", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (modeloDetalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "Debe agregar al menos un producto a la venta.");
            return;
        }

        // Crear objeto Ventas con los nuevos datos
        Ventas v = new Ventas();
        v.setId_factura(this.idFacturaActual);
        v.setId_cliente(idCliente);
        v.setId_trabajador(this.idTrabajador);
        v.setTotal(total);

        // Crear lista de detalles desde la tabla
        List<DetalleVenta> detalles = obtenerDetallesDesdeTabla();
        v.setDetalleVenta(detalles);

        System.out.println("Detalles a guardar: " + detalles.size());
        for (int i = 0; i < detalles.size(); i++) {
            DetalleVenta d = detalles.get(i);
            System.out.println("   Detalle " + i + ": Producto=" + d.getId_producto() + 
                             ", Cantidad=" + d.getCantidad() + ", Subtotal=" + d.getSubtotal());
        }

        // Confirmación antes de guardar
        int confirmacion = JOptionPane.showConfirmDialog(
            vista, 
            "¿Está seguro que desea guardar los cambios?\n" +
            "Factura ID: " + this.idFacturaActual + "\n" +
            "Total: $" + total + "\n" +
            "Productos: " + detalles.size(),
            "Confirmar cambios",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            System.out.println("🔄 Llamando a modificarVenta...");
            boolean actualizado = dao.modificarVenta(v);

            if (actualizado) {
                System.out.println("✅ Factura actualizada exitosamente");
                JOptionPane.showMessageDialog(vista, 
                    "Factura actualizada con éxito", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);

                if (menuPrincipal != null) {
                    menuPrincipal.actualizarListaFacturas();
                    menuPrincipal.actualizarTablaVentas();
                    menuPrincipal.actualizarDashboard();
                    menuPrincipal.actualizarTablaProductos();
                }
                limpiarFormulario();
            } else {
                System.out.println("❌ Error al actualizar factura");
                JOptionPane.showMessageDialog(vista, 
                    "Error al actualizar la factura", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("❌ Usuario canceló la operación");
        }
    }
    
     private List<DetalleVenta> obtenerDetallesDesdeTabla() {
        List<DetalleVenta> detalles = new ArrayList<>();

        try {
            System.out.println("📊 Obteniendo detalles desde tabla...");

            for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
                String nombreProducto = (String) modeloDetalle.getValueAt(i, 0);
                double precio = (Double) modeloDetalle.getValueAt(i, 1);
                int cantidad = (Integer) modeloDetalle.getValueAt(i, 2);
                double subtotal = (Double) modeloDetalle.getValueAt(i, 3);

                int idProducto = obtenerIdPorNombre(nombreProducto);

                DetalleVenta detalle = new DetalleVenta();
                detalle.setId_producto(idProducto);
                detalle.setCantidad(cantidad);
                detalle.setPrecio(precio);
                detalle.setSubtotal(subtotal);

                detalles.add(detalle);

                System.out.println("   Fila " + i + ": " + nombreProducto + " (ID:" + idProducto + 
                                 ") x" + cantidad + " = $" + subtotal);
            }

            System.out.println("✅ Total detalles obtenidos: " + detalles.size());

        } catch (Exception e) {
            System.out.println("❌ Error en obtenerDetallesDesdeTabla: " + e.getMessage());
            e.printStackTrace();
        }

        return detalles;
    }
    
    private void agregarProducto() {
        Producto producto = (Producto) vista.getCmbProducto().getSelectedItem();
        String precioStr = vista.getTxtPrecio().getText();
        String cantidadStr = vista.getTxtCantidad().getText();

        if (producto == null || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete los datos del producto antes de agregar.");
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
    try {
        // Limpiar campos de búsqueda
        vista.getTxtIdFactura().setText("");
        
        // Limpiar campos de cliente
        vista.getCmbCliente().setSelectedIndex(0);
        vista.getTxtCedula().setText("");
        vista.getTxtTelefono().setText("");
        
        // Limpiar campos de producto
        vista.getCmbProducto().setSelectedIndex(0);
        vista.getTxtCantidad().setText("");
        vista.getTxtPrecio().setText("");
        
        // Limpiar campos generales
        vista.getTxtTotal().setText("");
        
        // Limpiar tabla
        modeloDetalle.setRowCount(0);
        
        // Resetear ID de factura
        this.idFacturaActual = 0;
        
        System.out.println("Formulario limpiado - idFacturaActual: " + this.idFacturaActual);
        
    } catch (Exception e) {
        System.out.println("Error al limpiar formulario: " + e.getMessage());
        e.printStackTrace();
    }
}

    private int obtenerIdPorNombre(String nombreProducto) {
    try {
        // Si ya tienes un método en ProductoDAO para esto, úsalo:
        ProductoDAO productoDAO = new ProductoDAO();
        return productoDAO.obtenerIdPorNombre(nombreProducto);
    } catch (Exception e) {
        System.out.println("Error al obtener ID del producto: " + e.getMessage());
        return 1; // Valor por defecto temporal
    }
}
}
