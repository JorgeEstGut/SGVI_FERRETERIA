package Controller;

import Model.Inventario;
import Model.InventarioDAO;
import Model.Producto;
import Model.ProductoDAO;
import Model.Proveedor;
import Model.TipoProducto;
import View.frmMenu;
import View.frmNuevoProducto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


public class NuevoProductoController {
   private frmNuevoProducto vista;
   private ProductoDAO dao;
   private frmMenu menuPrincipal;

    public NuevoProductoController(frmNuevoProducto vista, ProductoDAO dao, frmMenu menuPrincipal) {
        this.vista = vista;
        this.dao = dao;
        this.menuPrincipal = menuPrincipal;
        initController();
    }
   
   public void initController() {
       vista.getBtnRegistrar().addActionListener(new ActionListener () {
            @Override
                public void actionPerformed(ActionEvent e) {
                    registrarProducto();
                }   
       });
   }
   
  public void registrarProducto() {
    String nombre = vista.getTxtNombre().getText();
    String descripcion = vista.getTxtDescripcion().getText();
    float precio = Float.parseFloat(vista.getTxtPrecio().getText());
    TipoProducto tipo = (TipoProducto) vista.getcmbTipo().getSelectedItem();
    Proveedor proveedor = (Proveedor) vista.getcmbProveedor().getSelectedItem();
    int cantidad = Integer.parseInt(vista.getTxtCantidad().getText());

    if (nombre.isEmpty() || cantidad <= 0) {
        JOptionPane.showMessageDialog(vista, "Complete todos los campos.");
        return;
    }

    if (dao.existeProducto(nombre)) {
        JOptionPane.showMessageDialog(vista, "El producto ya existe. Se actualizará el stock.");
        int idProductoExistente = dao.obtenerIdPorNombre(nombre);
        InventarioDAO invDAO = new InventarioDAO();
        invDAO.actualizarStock(idProductoExistente, cantidad);
        JOptionPane.showMessageDialog(vista, "Stock actualizado correctamente.");
        menuPrincipal.actualizarTablaProductos();
        return;
    }

    Producto p = new Producto();
    p.setNombre(nombre);
    p.setDescripcion(descripcion);
    p.setPrecio(precio);
    p.setId_tipo(tipo.getId_tipo());
    p.setId_Proveedor(proveedor.getId_proveedor());

    int idProductoNuevo = dao.insertarProductoYObtenerID(p);
    if (idProductoNuevo > 0) {
        Inventario inv = new Inventario();
        inv.setId_producto(idProductoNuevo);
        inv.setStock_actual(cantidad);
        inv.setStock_minimo(5); // puedes poner un valor fijo o configurable

        InventarioDAO invDAO = new InventarioDAO();
        invDAO.insertarInventario(inv);

        JOptionPane.showMessageDialog(vista, "Producto e inventario registrados correctamente.");
        limpiarCampos();
        menuPrincipal.actualizarTablaProductos();
    } else {
        JOptionPane.showMessageDialog(vista, "Error al registrar producto.");
    }
}

   
   private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtDescripcion().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtCantidad().setText("");
    }
}
