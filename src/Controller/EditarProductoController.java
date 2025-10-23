package Controller;

import Model.Inventario;
import Model.InventarioDAO; // 🔹 Importamos el DAO de inventario
import Model.Producto;
import Model.TipoProducto;
import Model.Proveedor;
import View.frmEditarProducto;
import Model.ProductoDAO;
import View.frmMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class EditarProductoController {
    
    private frmEditarProducto vista;
    private ProductoDAO dao;
    private InventarioDAO inventarioDAO;
    private frmMenu menuPrincipal;
    private Producto productoActual;
    private Inventario cantidad;

    public EditarProductoController(frmEditarProducto vista, ProductoDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        this.inventarioDAO = new InventarioDAO(); //inicializamos el DAO


        initComponent();
    }
    
    public void initComponent() {
        vista.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorID();
            }
        });
        
        vista.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
            }
        });
    }
    
    private void buscarProductoPorID() {
        try {
            String idText = vista.getTxtIdProducto().getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese un ID válido");
                return;
            }

            int id = Integer.parseInt(idText);
            productoActual = dao.buscarPorId(id);

            if (productoActual != null) {
                vista.getTxtNombre().setText(productoActual.getNombre());
                vista.getTxtDescripcion().setText(productoActual.getDescripcion());
                vista.getTxtPrecio().setText(String.valueOf(productoActual.getPrecio()));

                // Obtener stock real desde InventarioDAO
                cantidad = inventarioDAO.buscarPorProductoId(id);
                if (cantidad != null) {
                    vista.getTxtCantidad().setText(String.valueOf(cantidad.getStock_actual()));
                } else {
                    vista.getTxtCantidad().setText("0");
                }

                vista.getcmbTipo().setSelectedItem(
                    new TipoProducto(productoActual.getId_tipo(), productoActual.getNombre_tipo())
                );
                vista.getcmbProveedor().setSelectedItem(
                    new Proveedor(productoActual.getId_Proveedor(), productoActual.getNombre_Proveedor())
                );

                habilitarEdicion(true);
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró un producto con el ID: " + id);
                limpiarCampos();
                habilitarEdicion(false);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido");
            limpiarCampos();
            habilitarEdicion(false);
        }
    }

private void guardarCambios() {
    try {
        String nombre = vista.getTxtNombre().getText().trim();
        String descripcion = vista.getTxtDescripcion().getText().trim();
        int precio = Integer.parseInt(vista.getTxtPrecio().getText().trim());
        int stock = Integer.parseInt(vista.getTxtCantidad().getText().trim()); // ✅ cantidad leída
        TipoProducto tipo = (TipoProducto) vista.getcmbTipo().getSelectedItem();
        Proveedor proveedor = (Proveedor) vista.getcmbProveedor().getSelectedItem();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete todos los campos");
            return;
        }

        // Verificar duplicado
        if (!nombre.equals(productoActual.getNombre())) {
            if (dao.existeProducto(nombre)) {
                JOptionPane.showMessageDialog(vista,
                    "❌ El producto '" + nombre + "' ya está registrado.\n\n",
                    "Producto duplicado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Confirmación
        int confirmacion = JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro que desea guardar los cambios para el producto?\n\n" +
            "ID: " + productoActual.getId_producto() + "\n" +
            "Nombre: " + nombre + "\n" +
            "Descripción: " + descripcion + "\n" +
            "Precio: " + precio + "\n" +
            "Tipo: " + tipo.getNombre_tipo() + "\n" +
            "Proveedor: " + proveedor.getNombre_proveedor() + "\n" +
            "Stock: " + stock,
            "Confirmar cambios",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            
            productoActual.setNombre(nombre);
            productoActual.setDescripcion(descripcion);
            productoActual.setPrecio(precio);
            productoActual.setId_tipo(tipo.getId_tipo());
            productoActual.setId_Proveedor(proveedor.getId_proveedor());

            boolean actualizadoProducto = dao.actualizar(productoActual);

            
            InventarioDAO inventarioDAO = new InventarioDAO();
            if (inventarioDAO.existeInventario(productoActual.getId_producto())) {
                
                inventarioDAO.actualizarStock(productoActual.getId_producto(), stock);
            } else {
                
                Inventario inv = new Inventario();
                inv.setId_producto(productoActual.getId_producto());
                inv.setStock_actual(stock);
                inv.setStock_minimo(1); 
                inventarioDAO.insertarInventario(inv);
            }

            // 🔹 Verificación final
            if (actualizadoProducto) {
                JOptionPane.showMessageDialog(vista,
                    "Producto y stock actualizados correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

                if (menuPrincipal != null) {
                    menuPrincipal.actualizarTablaProductos();
                }

                limpiarCampos();
                habilitarEdicion(false);
            } else {
                JOptionPane.showMessageDialog(vista,
                    "Error al actualizar el producto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(vista,
            "Error inesperado: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    }
    
    private void habilitarEdicion(boolean habilitar) {
        vista.getTxtNombre().setEnabled(habilitar);
        vista.getTxtDescripcion().setEnabled(habilitar);
        vista.getTxtCantidad().setEnabled(habilitar);
        vista.getTxtPrecio().setEnabled(habilitar);
        vista.getcmbTipo().setEnabled(habilitar);       
        vista.getcmbProveedor().setEnabled(habilitar);
        vista.getBtnGuardar().setEnabled(habilitar);
    }

    
    private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtDescripcion().setText("");
        vista.getTxtCantidad().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtIdProducto().setText("");
    }
    
}
