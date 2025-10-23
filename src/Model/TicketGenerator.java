package Model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Generador de tickets en formato texto con diseño tipo impresora térmica
 * Formato optimizado para ancho pequeño y centrado
 */
public class TicketGenerator {
    
    private static final String RUTA_FACTURAS = "C:/SGVI/Facturas/";
    private static final int ANCHO_TICKET = 32; // Ancho fijo para ticket térmico
    
    /**
     * Genera un ticket en formato texto para una venta
     * @param idFactura ID de la factura
     * @param fecha Fecha de la venta
     * @param nombreCajero Nombre del cajero
     * @param total Total de la venta
     * @param efectivo Efectivo recibido
     * @param cambio Cambio devuelto
     * @param detalles Lista de productos vendidos
     * @return true si se generó correctamente, false en caso contrario
     */
    public static boolean generarTicket(int idFactura, String fecha, String nombreCajero, 
                                      double total, double efectivo, double cambio, 
                                      List<DetalleVenta> detalles) {
        try {
            // Crear directorio si no existe
            crearDirectorioSiNoExiste();
            
            // Generar nombre del archivo
            String nombreArchivo = generarNombreArchivo(idFactura);
            String rutaCompleta = RUTA_FACTURAS + nombreArchivo;
            
            // Crear archivo de texto
            try (FileWriter writer = new FileWriter(rutaCompleta)) {
                // Generar contenido del ticket
                generarContenidoTicket(writer, idFactura, fecha, nombreCajero, total, 
                                     efectivo, cambio, detalles);
            }
            
            System.out.println("✅ Ticket generado: " + rutaCompleta);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error al generar ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static void crearDirectorioSiNoExiste() {
        java.io.File directorio = new java.io.File(RUTA_FACTURAS);
        if (!directorio.exists()) {
            directorio.mkdirs();
            System.out.println("📁 Directorio creado: " + RUTA_FACTURAS);
        }
    }
    
    private static String generarNombreArchivo(int idFactura) {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return String.format("Factura_%d_%s.txt", idFactura, ahora.format(formatter));
    }
    
    private static void generarContenidoTicket(FileWriter writer, int idFactura, String fecha, 
                                             String nombreCajero, double total, double efectivo, 
                                             double cambio, List<DetalleVenta> detalles) 
                                             throws IOException {
        
        // Línea superior
        writer.write(centrarTexto("==========================") + "\n");
        
        // Título
        writer.write(centrarTexto("SGVI - Sistema de Gestión") + "\n");
        writer.write(centrarTexto("de Ventas e Inventarios") + "\n");
        
        // Línea separadora
        writer.write(centrarTexto("--------------------------") + "\n");
        
        // Información de la factura
        writer.write("Factura: " + String.format("%05d", idFactura) + "\n");
        writer.write("Fecha: " + fecha + "\n");
        writer.write("Cajero: " + nombreCajero + "\n");
        
        // Línea separadora
        writer.write(centrarTexto("--------------------------") + "\n");
        
        // Productos
        for (DetalleVenta detalle : detalles) {
            String nombreProducto = obtenerNombreProducto(detalle.getId_producto());
            
            // Formatear línea del producto con alineación correcta
            String lineaProducto = formatearLineaProducto(nombreProducto, detalle.getCantidad(), detalle.getSubtotal());
            writer.write(lineaProducto + "\n");
        }
        
        // Línea separadora
        writer.write(centrarTexto("--------------------------") + "\n");
        
        // Totales con alineación correcta
        writer.write(formatearLineaTotal("TOTAL:", total, true) + "\n");
        writer.write(formatearLineaTotal("EFECTIVO:", efectivo, false) + "\n");
        writer.write(formatearLineaTotal("CAMBIO:", cambio, false) + "\n");
        
        // Línea inferior
        writer.write(centrarTexto("==========================") + "\n");
        
        // Mensaje de agradecimiento
        writer.write(centrarTexto("¡GRACIAS POR SU COMPRA!") + "\n");
        
        // Línea final
        writer.write(centrarTexto("==========================") + "\n");
    }
    
    /**
     * Centra el texto en el ancho del ticket
     */
    private static String centrarTexto(String texto) {
        int espacios = (ANCHO_TICKET - texto.length()) / 2;
        if (espacios < 0) espacios = 0;
        return " ".repeat(espacios) + texto;
    }
    
    /**
     * Formatea una línea de producto con alineación correcta
     */
    private static String formatearLineaProducto(String nombre, int cantidad, double precio) {
        String productoInfo = nombre + " x" + cantidad;
        String precioStr = "$" + String.format("%.0f", precio);
        
        // Calcular espacios para alineación
        int espacios = ANCHO_TICKET - productoInfo.length() - precioStr.length();
        if (espacios < 1) espacios = 1;
        
        return productoInfo + " ".repeat(espacios) + precioStr;
    }
    
    /**
     * Formatea una línea de total con alineación correcta
     */
    private static String formatearLineaTotal(String etiqueta, double valor, boolean esNegrita) {
        String valorStr = "$" + String.format("%.0f", valor);
        
        // Calcular espacios para alineación
        int espacios = ANCHO_TICKET - etiqueta.length() - valorStr.length();
        if (espacios < 1) espacios = 1;
        
        return etiqueta + " ".repeat(espacios) + valorStr;
    }
    
    private static String obtenerNombreProducto(int idProducto) {
        // Crear instancia del DAO para obtener el nombre del producto
        VentasDAO ventasDAO = new VentasDAO();
        return ventasDAO.obtenerNombreProducto(idProducto);
    }
}
