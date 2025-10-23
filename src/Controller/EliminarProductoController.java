package Controller;

import Model.Inventario;
import Model.InventarioDAO;
import Model.Producto;
import View.frmEditarProducto;
import Model.ProductoDAO;
import View.EliminarProducto;
import View.frmMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class EliminarProductoController {
    
    private EliminarProducto vista;
    private ProductoDAO dao;
    private InventarioDAO inventarioDAO;
    private frmMenu menuPrincipal;
    private Producto productoActual;

    public EliminarProductoController(EliminarProducto vista, ProductoDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        this.inventarioDAO = new InventarioDAO();

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
                eliminarProducto();
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

                Inventario inv = inventarioDAO.buscarPorProductoId(id);
                if (inv != null) {
                    vista.getTxtCantidad().setText(String.valueOf(inv.getStock_actual()));
                } else {
                    vista.getTxtCantidad().setText("0");
                }

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

private void eliminarProducto() {
    if (productoActual == null) {
        JOptionPane.showMessageDialog(vista, "Primero busque un producto para eliminar");
        return;
    }

    // 🔹 Obtenemos datos adicionales
    String nombre = productoActual.getNombre();
    String descripcion = productoActual.getDescripcion();
    float precio = productoActual.getPrecio();

    String tipo = (productoActual.getNombre_tipo() != null) 
        ? productoActual.getNombre_tipo() 
        : "No especificado";

    String proveedor = (productoActual.getNombre_Proveedor() != null)
        ? productoActual.getNombre_Proveedor()
        : "No especificado";

    Inventario inv = inventarioDAO.buscarPorProductoId(productoActual.getId_producto());
    int stock = (inv != null) ? inv.getStock_actual() : 0;

    // 🔹 Confirmación detallada
    int confirmacion = JOptionPane.showConfirmDialog(
        vista,
        "️ ¿Está seguro que desea eliminar este producto?\n\n" +
        "ID: " + productoActual.getId_producto() + "\n" +
        "Nombre: " + nombre + "\n" +
        "Descripción: " + descripcion + "\n" +
        "Precio: " + precio + "\n" +
        "Tipo: " + tipo + "\n" +
        "Proveedor: " + proveedor + "\n" +
        "Stock actual: " + stock + "\n\n" +
        "Esta acción no se puede deshacer.",
        "Confirmar eliminación",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    // 🔹 Si confirma, eliminar producto e inventario
    if (confirmacion == JOptionPane.YES_OPTION) {
        boolean eliminadoInventario = inventarioDAO.eliminarPorProductoId(productoActual.getId_producto());
        boolean eliminadoProducto = dao.eliminar(productoActual.getId_producto());

        if (eliminadoProducto) {
            JOptionPane.showMessageDialog(vista,
                "✅ Producto eliminado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

            if (menuPrincipal != null) {
                menuPrincipal.actualizarTablaProductos();
                menuPrincipal.actualizarDashboard();
            }

            limpiarCampos();
            habilitarEdicion(false);
        } else {
            JOptionPane.showMessageDialog(vista,
                "❌ Error al eliminar el producto.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

    private void habilitarEdicion(boolean habilitar) {
        vista.getTxtNombre().setEnabled(false);
        vista.getTxtDescripcion().setEnabled(false);
        vista.getTxtCantidad().setEnabled(false);
        vista.getTxtPrecio().setEnabled(false);
        vista.getcmbTipo().setEnabled(false);
        vista.getcmbProveedor().setEnabled(false);
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
