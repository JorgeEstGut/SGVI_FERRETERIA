package Security;

import java.util.*;

public class Permisos {

    private static final Map<String, Set<String>> permisosPorRol = new HashMap<>();

    static {
        permisosPorRol.put("Administrador", new HashSet<>(Arrays.asList(
                "Dashboard", "Productos", "NuevoProducto", "Ventas", "NuevaVenta",
                "Proveedores", "NuevoProveedor", "Facturas", "BuscarFactura",
                "Usuarios", "RegistrarUsuario"
        )));
        permisosPorRol.put("Vendedor", new HashSet<>(Arrays.asList(
                "Ventas", "NuevaVenta", "Facturas", "BuscarFactura"
        )));
        permisosPorRol.put("Bodega", new HashSet<>(Arrays.asList(
                "Productos", "NuevoProducto", "Proveedores", "NuevoProveedor"
        )));
    }

    public static Set<String> getPermisos(String rol) {
        return permisosPorRol.getOrDefault(rol, new HashSet<>());
    }
}
