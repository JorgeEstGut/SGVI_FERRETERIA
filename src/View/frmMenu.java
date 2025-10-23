/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.DashboardController;
import Controller.EditarProductoController;
import Controller.EditarProveedorController;
import Controller.EditarUsuarioController;
import Controller.EditarVentaController;
import Controller.EliminarProductoController;
import Controller.EliminarProveedorController;
import Controller.EliminarTipoController;
import Controller.EliminarUsuarioController;
import Controller.EliminarVentaController;
import Controller.FacturaController;
import Controller.NuevoClienteController;
import Controller.NuevoProductoController;
import Controller.NuevoTipoController;
import Controller.ProveedoresController;
import Controller.UsuariosController;
import Controller.VentasController;
import Custom.IconScaler;
import Main.App;
import Model.Factura;
import Model.FacturaDAO;
import Model.Producto;
import Model.ProductoDAO;
import Model.Proveedor;
import Model.ProveedorDAO;
import Model.TipoProductoDAO;
import Model.Trabajador;
import Model.TrabajadorDAO;
import Model.Ventas;
import Model.VentasDAO;
import Security.Permisos;
import java.awt.Dialog;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class frmMenu extends javax.swing.JFrame {

    private String rolActual;
    private String nombreTrabajador;
    private int idTrabajador;
    
    public frmMenu(String rolActual, String nombreTrabajador, int idTrabajador) {
        this.rolActual = rolActual;
        this.nombreTrabajador = nombreTrabajador;
        this.idTrabajador = idTrabajador;
        initComponents();
        setLocationRelativeTo(null);
        setIcons();
        aplicarPermisos(rolActual);
        cargarUsuarios();
        cargarProveedores();
        cargarProductos();
        cargarFacturaComoLista();
        cargarVentas();
        inicializarDashboard();
        setIconImage(getToolkit().getImage(getClass().getResource("/Resources/iconoLogo2.png")));
    }
    
    public String getNombreTrabajador() {
        return nombreTrabajador;
    }
    
    public int getIdTrabajador() {
        return idTrabajador;
    }
    
    private void setIcons() {
        int size = 50;
        OpcionesLayout.setIconAt(0, IconScaler.getScaledIcon("/Resources/opIcons/Dashboard.png", size, size));
        OpcionesLayout.setIconAt(1, IconScaler.getScaledIcon("/Resources/opIcons/Productos.png", size, size));
        OpcionesLayout.setIconAt(2, IconScaler.getScaledIcon("/Resources/opIcons/Ventas.png", size, size));
        OpcionesLayout.setIconAt(3, IconScaler.getScaledIcon("/Resources/opIcons/Proveedores.png", size, size));
        OpcionesLayout.setIconAt(4, IconScaler.getScaledIcon("/Resources/opIcons/Factura.png", size, size));
        OpcionesLayout.setIconAt(5, IconScaler.getScaledIcon("/Resources/opIcons/Usuarios.png", size, size));
    }

    private void aplicarPermisos(String rol) {
        Set<String> permisos = Permisos.getPermisos(rol);

        if (!permisos.contains("Dashboard")) {
            OpcionesLayout.remove(jpDashboard);
        }
        if (!permisos.contains("Productos")) {
            OpcionesLayout.remove(jpProductos);
        }
        if (!permisos.contains("Ventas")) {
            OpcionesLayout.remove(jpVentas);
        }
        if (!permisos.contains("Proveedores")) {
            OpcionesLayout.remove(jpProveedores);
        }
        if (!permisos.contains("Facturas")) {
            OpcionesLayout.remove(jpFacturas);
        }
        if (!permisos.contains("Usuarios")) {
            OpcionesLayout.remove(jpUsuarios);
        }
    }
    
    private void inicializarDashboard() {
        try {
            DashboardController dashboardController = new DashboardController(this);
        } catch (Exception e) {
            System.out.println("Error inicializando dashboard: " + e.getMessage());
        }
    }

    // Método público para actualizar desde otros lugares
    public void actualizarDashboard() {
        DashboardController dashboardController = new DashboardController(this);
        dashboardController.actualizarDashboard();
    }
    
    // Getters para el Dashboard
    public javax.swing.JTextField getTxtVentasDia() {
        return txtVentasDia;
    }

    public javax.swing.JTextField getTxtGananciaDia() {
        return txtGananciaDia;
    }

    public javax.swing.JTextField getTxtCantidadProductos() {
        return txtCantidadProductos;
    }

    public javax.swing.JTable getTblStockBajo() {
        return tblStockBajo;
    }
    
    //Cargar tabla en Panel Usuarios
    private void cargarUsuarios() {
        TrabajadorDAO dao = new TrabajadorDAO();
        java.util.List<Trabajador> lista = dao.listar();

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{"ID", "Nombre", "Usuario", "Clave", "Rol"});

        for (Trabajador t : lista) {
            modelo.addRow(new Object[]{t.getId_trabajador(), t.getNombre(), t.getUsuario(), t.getClave(), t.getRol()});
        }

        tblUsuarios.setModel(modelo);
        // Cambiar el tamaño de la fuente del contenido de la tabla
        tblUsuarios.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16)); 
        tblUsuarios.setRowHeight(30); // Aumenta la altura de las filas

        // Cambiar el tamaño de la fuente del encabezado
        javax.swing.table.JTableHeader header = tblUsuarios.getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        
        // Centrar texto en las celdas
        javax.swing.table.DefaultTableCellRenderer centrado = new javax.swing.table.DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        for (int i = 0; i < tblUsuarios.getColumnCount(); i++) {
            tblUsuarios.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
    
    public void actualizarTablaUsuarios() {
        cargarUsuarios();
    }
    
    //Cargar tabla en Panel Proveedores
    private void cargarProveedores() {
        ProveedorDAO dao = new ProveedorDAO();
        java.util.List<Proveedor> lista = dao.listar();
        
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[] {"ID", "Nombre", "Email"});
        
        for (Proveedor p : lista) {
            modelo.addRow(new Object[]{p.getId_proveedor(), p.getNombre_proveedor(), p.getEmail()});
        }
        
        tblProveedor.setModel(modelo);
        tblProveedor.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        tblProveedor.setRowHeight(30);
        
        javax.swing.table.JTableHeader header = tblProveedor.getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        
        javax.swing.table.DefaultTableCellRenderer centrado = new javax.swing.table.DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        for (int i = 0; i < tblProveedor.getColumnCount(); i++){
            tblProveedor.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
    
    public void actualizarTablaProveedores() {
        cargarProveedores();
    }
    
    //crhar tabla en panel preoductos
    private void cargarProductos() {
        ProductoDAO dao = new ProductoDAO();
        java.util.List<Producto> lista = dao.listar();
        
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[] {"ID", "Nombre", "Descripcion", "Precio", "Tipo", "Proveedor", "Stock"});
        
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
            p.getId_producto(), 
            p.getNombre(), 
            p.getDescripcion(), 
            p.getPrecio(), 
            p.getNombre_tipo(), 
            p.getNombre_Proveedor(),
            p.getStock()
        });
        if (p.getStock() >5){
            
        }
      }
        
        tblProducto.setModel(modelo);
        tblProducto.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        tblProducto.setRowHeight(30);
        
        javax.swing.table.JTableHeader header = tblProducto.getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        
        javax.swing.table.DefaultTableCellRenderer centrado = new javax.swing.table.DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        for (int i = 0; i < tblProducto.getColumnCount(); i++){
            tblProducto.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
    
    public void actualizarTablaProductos() {
        cargarProductos();
    }
    
    public void cargarFacturaComoLista() {
        FacturaDAO dao = new FacturaDAO();
        java.util.List<Factura> lista = dao.listar();

        // Crear un modelo para la lista
        DefaultListModel<String> modeloLista = new DefaultListModel<>();

        for (Factura f : lista) {
            String item = String.format("ID: %d | Fecha: %s | Cliente: %s | Trabajador: %s | Total: %.2f",
                f.getId_factura(), f.getFecha(), f.getNombreCliente(), f.getNombreTrabajador(), f.getTotal());
            modeloLista.addElement(item);
        }

        // Asignar el modelo a la JList
        lstFacturas.setModel(modeloLista);

        // Opcional: cambiar fuente y tamaño
        lstFacturas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
    }
        
    public void actualizarListaFacturas() {
        cargarFacturaComoLista();
    }
    
    //Cargar tabla en Panel Ventas
    private void cargarVentas() {
        VentasDAO dao = new VentasDAO();
        java.util.List<Ventas> lista = dao.listar();
        
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[] {"ID Factura", "Fecha", "Nombre Cliente", 
            "Cedula Cliente", "Telefono Cliente", "ID Trabajador", "Nombre Trabajador", 
            "Nombre Producto", "Cantidad", "Precio", "Subtotal", "Total"});
        
        for (Ventas v : lista) {
            modelo.addRow(new Object[]{v.getId_factura(), v.getFecha(), v.getNombre_clientes(),
            v.getCedula_clientes(), v.getTelefono_clientes(), v.getId_trabajador(), v.getNombre_trabajadores(), 
            v.getNombre_productos(), v.getCantidad(), v.getPrecio(), v.getSubtotal(), v.getTotal()});
        }
        
        tblVentas.setModel(modelo);
        tblVentas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        tblVentas.setRowHeight(30);
        
        javax.swing.table.JTableHeader header = tblVentas.getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        
        javax.swing.table.DefaultTableCellRenderer centrado = new javax.swing.table.DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        for (int i = 0; i < tblVentas.getColumnCount(); i++){
            tblVentas.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
    
    public void actualizarTablaVentas() {
        cargarVentas();
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        OpcionesLayout = new javax.swing.JTabbedPane();
        jpDashboard = new javax.swing.JPanel();
        etiProductosBajos = new javax.swing.JLabel();
        txtVentasDia = new Custom.RoundTextField(7);
        etiGananciaDia = new javax.swing.JLabel();
        txtGananciaDia = new Custom.RoundTextField(7);
        txtCantidadProductos = new Custom.RoundTextField(7);
        etiCantidadVendidos = new javax.swing.JLabel();
        jScrollStockBajo = new javax.swing.JScrollPane();
        tblStockBajo = new javax.swing.JTable();
        etiVentasDia = new javax.swing.JLabel();
        jpProductos = new javax.swing.JPanel();
        jScrollProducto = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        btnNuevoTipo = new Custom.RoundButton("Eliminar Usuario");
        btnEditarProducto = new Custom.RoundButton("Eliminar Usuario");
        btnEliminarProducto1 = new Custom.RoundButton("Eliminar Usuario");
        btnAgregaProducto = new Custom.RoundButton("Eliminar Usuario");
        btnEliminarTipo = new Custom.RoundButton("Eliminar Usuario");
        jpVentas = new javax.swing.JPanel();
        jScrollVentas = new javax.swing.JScrollPane();
        tblVentas = new javax.swing.JTable();
        btnEditarVenta1 = new Custom.RoundButton("Eliminar Usuario");
        btnEliminarVenta1 = new Custom.RoundButton("Eliminar Usuario");
        btnAgregaVenta1 = new Custom.RoundButton("Eliminar Usuario");
        btnAgregaCliente1 = new Custom.RoundButton("Eliminar Usuario");
        jpProveedores = new javax.swing.JPanel();
        jScrollProveedores = new javax.swing.JScrollPane();
        tblProveedor = new javax.swing.JTable();
        btnEditarProveedor = new Custom.RoundButton("Eliminar Usuario");
        btnEliminarProveedor = new Custom.RoundButton("Eliminar Usuario");
        btnAgregaProveedor = new Custom.RoundButton("Eliminar Usuario");
        jpFacturas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstFacturas = new javax.swing.JList<>();
        jScrollFactura1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        btnImprimir = new Custom.RoundButton("Eliminar Usuario");
        jpUsuarios = new javax.swing.JPanel();
        jScrollUsuarios = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        btnEliminarUsuario = new Custom.RoundButton("Eliminar Usuario");
        btnAgregarUsuario = new Custom.RoundButton("Eliminar Usuario");
        btnEditarUsuario = new Custom.RoundButton("Eliminar Usuario");
        brrMenu = new javax.swing.JMenuBar();
        opSalir = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SGVI - FERRETERIA");
        setResizable(false);

        OpcionesLayout.setBackground(new java.awt.Color(238, 217, 75));
        OpcionesLayout.setForeground(new java.awt.Color(0, 0, 0));
        OpcionesLayout.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        OpcionesLayout.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        OpcionesLayout.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jpDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        etiProductosBajos.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        etiProductosBajos.setText("Productos bajos de stock");

        txtVentasDia.setEditable(false);
        txtVentasDia.setBackground(new java.awt.Color(244, 244, 244));
        txtVentasDia.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtVentasDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVentasDiaActionPerformed(evt);
            }
        });

        etiGananciaDia.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        etiGananciaDia.setText("Facturas generadas hoy");

        txtGananciaDia.setEditable(false);
        txtGananciaDia.setBackground(new java.awt.Color(244, 244, 244));
        txtGananciaDia.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtGananciaDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGananciaDiaActionPerformed(evt);
            }
        });

        txtCantidadProductos.setEditable(false);
        txtCantidadProductos.setBackground(new java.awt.Color(244, 244, 244));
        txtCantidadProductos.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtCantidadProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadProductosActionPerformed(evt);
            }
        });

        etiCantidadVendidos.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        etiCantidadVendidos.setText("Cant. Productos vendidos");

        tblStockBajo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblStockBajo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblStockBajo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblStockBajo.setEnabled(false);
        jScrollStockBajo.setViewportView(tblStockBajo);

        etiVentasDia.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        etiVentasDia.setText("Total vendido hoy");

        javax.swing.GroupLayout jpDashboardLayout = new javax.swing.GroupLayout(jpDashboard);
        jpDashboard.setLayout(jpDashboardLayout);
        jpDashboardLayout.setHorizontalGroup(
            jpDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDashboardLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jpDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpDashboardLayout.createSequentialGroup()
                        .addGroup(jpDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtVentasDia, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etiVentasDia))
                        .addGap(53, 53, 53)
                        .addGroup(jpDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpDashboardLayout.createSequentialGroup()
                                .addComponent(etiGananciaDia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(etiCantidadVendidos))
                            .addComponent(txtGananciaDia, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtCantidadProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jpDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(etiProductosBajos)
                            .addComponent(jScrollStockBajo, javax.swing.GroupLayout.PREFERRED_SIZE, 914, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(94, Short.MAX_VALUE))
        );
        jpDashboardLayout.setVerticalGroup(
            jpDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDashboardLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jpDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpDashboardLayout.createSequentialGroup()
                        .addComponent(etiCantidadVendidos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCantidadProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpDashboardLayout.createSequentialGroup()
                        .addComponent(etiGananciaDia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtGananciaDia, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpDashboardLayout.createSequentialGroup()
                        .addComponent(etiVentasDia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVentasDia, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(etiProductosBajos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollStockBajo, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        OpcionesLayout.addTab("Dashboard", jpDashboard);

        jpProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jScrollProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblProducto.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblProducto.setEnabled(false);
        jScrollProducto.setViewportView(tblProducto);

        btnNuevoTipo.setBackground(new java.awt.Color(51, 51, 51));
        btnNuevoTipo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnNuevoTipo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevoTipo.setText("Nuevo Tipo ");
        btnNuevoTipo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnNuevoTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoTipoActionPerformed(evt);
            }
        });

        btnEditarProducto.setBackground(new java.awt.Color(204, 153, 0));
        btnEditarProducto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditarProducto.setText("Editar Producto");
        btnEditarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEditarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto1.setBackground(new java.awt.Color(51, 51, 51));
        btnEliminarProducto1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminarProducto1.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProducto1.setText("Eliminar Producto");
        btnEliminarProducto1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEliminarProducto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProducto1ActionPerformed(evt);
            }
        });

        btnAgregaProducto.setBackground(new java.awt.Color(238, 217, 75));
        btnAgregaProducto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregaProducto.setText("Agregar Producto");
        btnAgregaProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAgregaProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregaProductoMouseClicked(evt);
            }
        });
        btnAgregaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregaProductoActionPerformed(evt);
            }
        });

        btnEliminarTipo.setBackground(new java.awt.Color(51, 51, 51));
        btnEliminarTipo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminarTipo.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarTipo.setText("Eliminar Tipo");
        btnEliminarTipo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEliminarTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTipoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpProductosLayout = new javax.swing.GroupLayout(jpProductos);
        jpProductos.setLayout(jpProductosLayout);
        jpProductosLayout.setHorizontalGroup(
            jpProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpProductosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 967, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpProductosLayout.createSequentialGroup()
                        .addComponent(btnEliminarTipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNuevoTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(56, 56, 56))
        );
        jpProductosLayout.setVerticalGroup(
            jpProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpProductosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevoTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        OpcionesLayout.addTab("Productos", jpProductos);

        jpVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jScrollVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tblVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblVentas.setEnabled(false);
        jScrollVentas.setViewportView(tblVentas);

        btnEditarVenta1.setBackground(new java.awt.Color(204, 153, 0));
        btnEditarVenta1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditarVenta1.setText("Editar Venta");
        btnEditarVenta1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarVenta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarVenta1ActionPerformed(evt);
            }
        });

        btnEliminarVenta1.setBackground(new java.awt.Color(51, 51, 51));
        btnEliminarVenta1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminarVenta1.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarVenta1.setText("Eliminar Venta");
        btnEliminarVenta1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarVenta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVenta1ActionPerformed(evt);
            }
        });

        btnAgregaVenta1.setBackground(new java.awt.Color(238, 217, 75));
        btnAgregaVenta1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregaVenta1.setText("Nueva Venta");
        btnAgregaVenta1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregaVenta1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregaVenta1MouseClicked(evt);
            }
        });
        btnAgregaVenta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregaVenta1ActionPerformed(evt);
            }
        });

        btnAgregaCliente1.setBackground(new java.awt.Color(238, 217, 75));
        btnAgregaCliente1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregaCliente1.setText("Nuevo Cliente");
        btnAgregaCliente1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregaCliente1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregaCliente1MouseClicked(evt);
            }
        });
        btnAgregaCliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregaCliente1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpVentasLayout = new javax.swing.GroupLayout(jpVentas);
        jpVentas.setLayout(jpVentasLayout);
        jpVentasLayout.setHorizontalGroup(
            jpVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpVentasLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jpVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 927, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpVentasLayout.createSequentialGroup()
                        .addComponent(btnAgregaCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnEditarVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregaVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        jpVentasLayout.setVerticalGroup(
            jpVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpVentasLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditarVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregaVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregaCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 18, Short.MAX_VALUE))
        );

        OpcionesLayout.addTab("Ventas", jpVentas);

        jpProveedores.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jScrollProveedores.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tblProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblProveedor.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblProveedor.setEnabled(false);
        jScrollProveedores.setViewportView(tblProveedor);

        btnEditarProveedor.setBackground(new java.awt.Color(204, 153, 0));
        btnEditarProveedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditarProveedor.setText("Editar Proveedor");
        btnEditarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProveedorActionPerformed(evt);
            }
        });

        btnEliminarProveedor.setBackground(new java.awt.Color(51, 51, 51));
        btnEliminarProveedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProveedor.setText("Eliminar Proveedor");
        btnEliminarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProveedorActionPerformed(evt);
            }
        });

        btnAgregaProveedor.setBackground(new java.awt.Color(238, 217, 75));
        btnAgregaProveedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregaProveedor.setText("Nuevo Proveedor");
        btnAgregaProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregaProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregaProveedorMouseClicked(evt);
            }
        });
        btnAgregaProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregaProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpProveedoresLayout = new javax.swing.GroupLayout(jpProveedores);
        jpProveedores.setLayout(jpProveedoresLayout);
        jpProveedoresLayout.setHorizontalGroup(
            jpProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpProveedoresLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jpProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpProveedoresLayout.createSequentialGroup()
                        .addComponent(btnEditarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 959, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jpProveedoresLayout.setVerticalGroup(
            jpProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpProveedoresLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jpProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        OpcionesLayout.addTab("Proveedores", jpProveedores);

        jpFacturas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jScrollPane1.setViewportView(lstFacturas);

        jScrollFactura1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("FACTURA DE VENTA");

        btnImprimir.setBackground(new java.awt.Color(204, 153, 0));
        btnImprimir.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnImprimir.setText("IMPRIMIR");
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpFacturasLayout = new javax.swing.GroupLayout(jpFacturas);
        jpFacturas.setLayout(jpFacturasLayout);
        jpFacturasLayout.setHorizontalGroup(
            jpFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFacturasLayout.createSequentialGroup()
                .addGroup(jpFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpFacturasLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jpFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollFactura1, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpFacturasLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(141, 141, 141)
                                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jpFacturasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 838, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(185, Short.MAX_VALUE))
        );
        jpFacturasLayout.setVerticalGroup(
            jpFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFacturasLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jpFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollFactura1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        OpcionesLayout.addTab("Facturas", jpFacturas);

        jpUsuarios.setForeground(new java.awt.Color(255, 255, 255));
        jpUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblUsuarios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblUsuarios.setEnabled(false);
        jScrollUsuarios.setViewportView(tblUsuarios);

        btnEliminarUsuario.setBackground(new java.awt.Color(51, 51, 51));
        btnEliminarUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminarUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarUsuario.setText("Eliminar Usuario");
        btnEliminarUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUsuarioActionPerformed(evt);
            }
        });

        btnAgregarUsuario.setBackground(new java.awt.Color(238, 217, 75));
        btnAgregarUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregarUsuario.setText("Nuevo Usuario");
        btnAgregarUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarUsuarioMouseClicked(evt);
            }
        });
        btnAgregarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarUsuarioActionPerformed(evt);
            }
        });

        btnEditarUsuario.setBackground(new java.awt.Color(204, 153, 0));
        btnEditarUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditarUsuario.setText("Editar Usuario");
        btnEditarUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpUsuariosLayout = new javax.swing.GroupLayout(jpUsuarios);
        jpUsuarios.setLayout(jpUsuariosLayout);
        jpUsuariosLayout.setHorizontalGroup(
            jpUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpUsuariosLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jpUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpUsuariosLayout.createSequentialGroup()
                        .addComponent(btnEditarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 959, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jpUsuariosLayout.setVerticalGroup(
            jpUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpUsuariosLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jpUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        OpcionesLayout.addTab("Usuarios", jpUsuarios);

        brrMenu.setBackground(new java.awt.Color(51, 51, 51));
        brrMenu.setForeground(new java.awt.Color(255, 255, 255));
        brrMenu.setPreferredSize(new java.awt.Dimension(38, 30));

        opSalir.setForeground(new java.awt.Color(255, 255, 255));
        opSalir.setText("Salir");
        opSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opSalirMouseClicked(evt);
            }
        });
        opSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opSalirActionPerformed(evt);
            }
        });
        brrMenu.add(opSalir);

        setJMenuBar(brrMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OpcionesLayout)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OpcionesLayout, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void opSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opSalirActionPerformed
   
    }//GEN-LAST:event_opSalirActionPerformed

    private void opSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opSalirMouseClicked
        this.dispose();      
        App.abrirLogin(); 
    }//GEN-LAST:event_opSalirMouseClicked

    private void btnEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUsuarioActionPerformed
        JDialog dialogo = new JDialog(this, "Eliminar Usuario", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(500, 700);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Crear el formulario y el controlador CON LA REFERENCIA AL MENÚ
        frmEliminarUsuario formulario = new frmEliminarUsuario();
        TrabajadorDAO dao = new TrabajadorDAO();
        EliminarUsuarioController controller = new EliminarUsuarioController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnEliminarUsuarioActionPerformed

    private void btnAgregarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarUsuarioActionPerformed
        JDialog dialogo = new JDialog(this, "Registro de nuevo usuario", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(500, 620);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmNuevoUsuario formulario = new frmNuevoUsuario();
        TrabajadorDAO dao = new TrabajadorDAO();
        UsuariosController controller = new UsuariosController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnAgregarUsuarioActionPerformed

    private void btnEditarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarUsuarioActionPerformed
        JDialog dialogo = new JDialog(this, "Editar Usuario", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(500, 700);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmEditarUsuario formulario = new frmEditarUsuario();
        TrabajadorDAO dao = new TrabajadorDAO();
        EditarUsuarioController controller = new EditarUsuarioController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnEditarUsuarioActionPerformed

    private void btnAgregarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarUsuarioMouseClicked

    }//GEN-LAST:event_btnAgregarUsuarioMouseClicked

    private void btnEditarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProveedorActionPerformed
        JDialog dialogo = new JDialog(this, "Editar Proveedor", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(500, 550);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmEditarProveedor formulario = new frmEditarProveedor();
        ProveedorDAO dao = new ProveedorDAO();
        EditarProveedorController controller = new EditarProveedorController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnEditarProveedorActionPerformed

    private void btnEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProveedorActionPerformed
        JDialog dialogo = new JDialog(this, "Eliminar Proveedor", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(500, 550);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmEliminarProveedor formulario = new frmEliminarProveedor();
        ProveedorDAO dao = new ProveedorDAO();
        EliminarProveedorController controller = new EliminarProveedorController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnEliminarProveedorActionPerformed

    private void btnAgregaProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregaProveedorMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregaProveedorMouseClicked

    private void btnAgregaProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregaProveedorActionPerformed
        JDialog dialogo = new JDialog(this, "Nuevo Proveedor", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(500, 550);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmNuevoProveedor formulario = new frmNuevoProveedor();
        ProveedorDAO dao = new ProveedorDAO();
        ProveedoresController controller = new ProveedoresController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnAgregaProveedorActionPerformed

    private void btnNuevoTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoTipoActionPerformed
        JDialog dialogo = new JDialog(this, "Nuevo Tipo", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(500, 550);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmNuevoTipo formulario = new frmNuevoTipo();
        ProductoDAO dao = new ProductoDAO();
        NuevoTipoController controller = new NuevoTipoController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnNuevoTipoActionPerformed

    private void btnEditarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProductoActionPerformed
        // TODO add your handling code here:
        JDialog dialogo = new JDialog(this, "Editar Producto", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(600, 520);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmEditarProducto formulario = new frmEditarProducto();
        ProductoDAO dao = new ProductoDAO();
        EditarProductoController controller = new EditarProductoController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnEditarProductoActionPerformed

    private void btnEliminarProducto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProducto1ActionPerformed
        // TODO add your handling code here:
        JDialog dialogo = new JDialog(this, "Eliminar Producto", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(580, 520);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        EliminarProducto formulario = new EliminarProducto();
        ProductoDAO dao = new ProductoDAO();
        EliminarProductoController controller = new EliminarProductoController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnEliminarProducto1ActionPerformed

    private void btnAgregaProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregaProductoMouseClicked
       // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregaProductoMouseClicked

    private void btnAgregaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregaProductoActionPerformed
        JDialog dialogo = new JDialog(this, "Nuevo Producto", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(610, 570);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmNuevoProducto formulario = new frmNuevoProducto();
        ProductoDAO dao = new ProductoDAO();
        NuevoProductoController controller = new NuevoProductoController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnAgregaProductoActionPerformed

    private void btnEliminarTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTipoActionPerformed
        JDialog dialogo = new JDialog(this, "Eliminar producto", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(590, 550);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmEliminarTipo formulario = new frmEliminarTipo();
        TipoProductoDAO dao = new TipoProductoDAO();
        EliminarTipoController controller = new EliminarTipoController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnEliminarTipoActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // Obtener la factura seleccionada del JList
        String facturaSeleccionada = lstFacturas.getSelectedValue();

        if (facturaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione una factura de la lista.",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extraer el ID de la factura del texto seleccionado
        int idFactura = extraerIdFactura(facturaSeleccionada);

        if (idFactura <= 0) {
            JOptionPane.showMessageDialog(this,
                "No se pudo obtener el ID de la factura seleccionada.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialogo = new JDialog(this, "Ver Factura - ID: " + idFactura, Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(780, 700);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmFactura formulario = new frmFactura();
        FacturaDAO dao = new FacturaDAO();

        // Pasar el ID de la factura al controlador
        FacturaController controller = new FacturaController(formulario, dao, this, idFactura);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);

    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnEditarVenta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarVenta1ActionPerformed
        // TODO add your handling code here:
        JDialog dialogo = new JDialog(this, "Editar Venta", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(600, 670); // Un poco más grande por el campo adicional
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmEditarVenta formulario = new frmEditarVenta();
        VentasDAO dao = new VentasDAO();

        EditarVentaController controller = new EditarVentaController(
            formulario, 
            dao, 
            this, 
            this.nombreTrabajador,
            this.idTrabajador
        );

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);

        // Actualizar lista después de editar
        actualizarListaFacturas();
    }//GEN-LAST:event_btnEditarVenta1ActionPerformed

    private void btnEliminarVenta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVenta1ActionPerformed
        // TODO add your handling code here:
        JDialog dialogo = new JDialog(this, "Eliminar Venta", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(460, 420);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmEliminarVenta formulario = new frmEliminarVenta();
        VentasDAO dao = new VentasDAO();
        EliminarVentaController controller = new EliminarVentaController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnEliminarVenta1ActionPerformed

    private void btnAgregaVenta1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregaVenta1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregaVenta1MouseClicked

    private void btnAgregaVenta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregaVenta1ActionPerformed
        JDialog dialogo = new JDialog(this, "Nueva Venta", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(580, 720);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmNuevaVenta formulario = new frmNuevaVenta();
        VentasDAO dao = new VentasDAO();
        ProductoDAO stock = new ProductoDAO();

        // Pasar la información del trabajador al controlador
        VentasController controller = new VentasController(formulario, dao, this, this.nombreTrabajador, this.idTrabajador, stock);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnAgregaVenta1ActionPerformed

    private void btnAgregaCliente1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregaCliente1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregaCliente1MouseClicked

    private void btnAgregaCliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregaCliente1ActionPerformed
        // TODO add your handling code here:
        JDialog dialogo = new JDialog(this, "Nuevo Cliente", Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(500, 550);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        frmNuevoCliente formulario = new frmNuevoCliente();
        VentasDAO dao = new VentasDAO();
        NuevoClienteController controller = new NuevoClienteController(formulario, dao, this);

        dialogo.add(formulario.getContentPane());
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnAgregaCliente1ActionPerformed

    private void txtVentasDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVentasDiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVentasDiaActionPerformed

    private void txtGananciaDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGananciaDiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGananciaDiaActionPerformed

    private void txtCantidadProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadProductosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadProductosActionPerformed

    
    // Método auxiliar para extraer el ID de la factura del texto del JList
    private int extraerIdFactura(String textoFactura) {
        try {
            // El formato es: "ID: 1 | Fecha: ..."
            if (textoFactura != null && textoFactura.contains("ID:")) {
                String[] partes = textoFactura.split("\\|");
                if (partes.length > 0) {
                    String idPart = partes[0].replace("ID:", "").trim();
                    return Integer.parseInt(idPart);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error al extraer ID de factura: " + e.getMessage());
        }
        return -1;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMenu("Admin", "Administrador", 1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane OpcionesLayout;
    private javax.swing.JMenuBar brrMenu;
    private javax.swing.JButton btnAgregaCliente1;
    private javax.swing.JButton btnAgregaProducto;
    private javax.swing.JButton btnAgregaProveedor;
    private javax.swing.JButton btnAgregaVenta1;
    private javax.swing.JButton btnAgregarUsuario;
    private javax.swing.JButton btnEditarProducto;
    private javax.swing.JButton btnEditarProveedor;
    private javax.swing.JButton btnEditarUsuario;
    private javax.swing.JButton btnEditarVenta1;
    private javax.swing.JButton btnEliminarProducto1;
    private javax.swing.JButton btnEliminarProveedor;
    private javax.swing.JButton btnEliminarTipo;
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JButton btnEliminarVenta1;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNuevoTipo;
    private javax.swing.JLabel etiCantidadVendidos;
    private javax.swing.JLabel etiGananciaDia;
    private javax.swing.JLabel etiProductosBajos;
    private javax.swing.JLabel etiVentasDia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollFactura1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollProducto;
    private javax.swing.JScrollPane jScrollProveedores;
    private javax.swing.JScrollPane jScrollStockBajo;
    private javax.swing.JScrollPane jScrollUsuarios;
    private javax.swing.JScrollPane jScrollVentas;
    private javax.swing.JPanel jpDashboard;
    private javax.swing.JPanel jpFacturas;
    private javax.swing.JPanel jpProductos;
    private javax.swing.JPanel jpProveedores;
    private javax.swing.JPanel jpUsuarios;
    private javax.swing.JPanel jpVentas;
    private javax.swing.JList<String> lstFacturas;
    private javax.swing.JMenu opSalir;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTable tblProveedor;
    private javax.swing.JTable tblStockBajo;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTable tblVentas;
    private javax.swing.JTextField txtCantidadProductos;
    private javax.swing.JTextField txtGananciaDia;
    private javax.swing.JTextField txtVentasDia;
    // End of variables declaration//GEN-END:variables
}
