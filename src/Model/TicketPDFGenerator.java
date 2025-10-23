package Model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketPDFGenerator {
    
    private static final String RUTA_FACTURAS = "C:/SGVI/Facturas/";
    
    /**
     * Genera un ticket PDF para una venta
     * @param idFactura ID de la factura
     * @param fecha Fecha de la venta
     * @param nombreCajero Nombre del cajero
     * @param total Total de la venta
     * @param efectivo Efectivo recibido
     * @param cambio Cambio devuelto
     * @param detalles Lista de productos vendidos
     * @return true si se generó correctamente, false en caso contrario
     */
    public static boolean generarTicketPDF(int idFactura, String fecha, String nombreCajero, 
                                         double total, double efectivo, double cambio, 
                                         List<DetalleVenta> detalles) {
        try {
            // Crear directorio si no existe
            crearDirectorioSiNoExiste();
            
            // Generar nombre del archivo
            String nombreArchivo = generarNombreArchivo(idFactura);
            String rutaCompleta = RUTA_FACTURAS + nombreArchivo;
            
            // Crear documento PDF
            Document documento = new Document(PageSize.A4.rotate()); // Formato horizontal para ticket
            PdfWriter.getInstance(documento, new FileOutputStream(rutaCompleta));
            
            documento.open();
            
            // Configurar fuente
            Font fuenteTitulo = new Font(Font.FontFamily.COURIER, 14, Font.BOLD);
            Font fuenteNormal = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
            Font fuenteNegrita = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
            
            // Generar contenido del ticket
            generarContenidoTicket(documento, idFactura, fecha, nombreCajero, total, 
                                 efectivo, cambio, detalles, fuenteTitulo, fuenteNormal, fuenteNegrita);
            
            documento.close();
            
            System.out.println("✅ Ticket PDF generado: " + rutaCompleta);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error al generar ticket PDF: " + e.getMessage());
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
        return String.format("Factura_%d_%s.pdf", idFactura, ahora.format(formatter));
    }
    
    private static void generarContenidoTicket(Document documento, int idFactura, String fecha, 
                                             String nombreCajero, double total, double efectivo, 
                                             double cambio, List<DetalleVenta> detalles,
                                             Font fuenteTitulo, Font fuenteNormal, Font fuenteNegrita) 
                                             throws DocumentException {
        
        // Línea superior
        Paragraph lineaSuperior = new Paragraph("==========================", fuenteNormal);
        lineaSuperior.setAlignment(Element.ALIGN_CENTER);
        documento.add(lineaSuperior);
        
        // Título
        Paragraph titulo = new Paragraph("  SGVI - Sistema de Gestión\n     de Ventas e Inventarios", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        
        // Línea separadora
        Paragraph lineaSeparadora1 = new Paragraph("--------------------------", fuenteNormal);
        lineaSeparadora1.setAlignment(Element.ALIGN_CENTER);
        documento.add(lineaSeparadora1);
        
        // Información de la factura
        Paragraph infoFactura = new Paragraph();
        infoFactura.add(new Chunk("Factura: " + String.format("%05d", idFactura), fuenteNegrita));
        infoFactura.add(Chunk.NEWLINE);
        infoFactura.add(new Chunk("Fecha: " + fecha, fuenteNormal));
        infoFactura.add(Chunk.NEWLINE);
        infoFactura.add(new Chunk("Cajero: " + nombreCajero, fuenteNormal));
        infoFactura.setAlignment(Element.ALIGN_LEFT);
        documento.add(infoFactura);
        
        // Línea separadora
        Paragraph lineaSeparadora2 = new Paragraph("--------------------------", fuenteNormal);
        lineaSeparadora2.setAlignment(Element.ALIGN_CENTER);
        documento.add(lineaSeparadora2);
        
        // Productos
        for (DetalleVenta detalle : detalles) {
            String nombreProducto = obtenerNombreProducto(detalle.getId_producto());
            Paragraph producto = new Paragraph();
            
            // Formatear línea del producto
            String lineaProducto = String.format("%-15s x%-3d $%.0f", 
                nombreProducto.length() > 15 ? nombreProducto.substring(0, 15) : nombreProducto,
                detalle.getCantidad(), 
                detalle.getSubtotal());
            
            producto.add(new Chunk(lineaProducto, fuenteNormal));
            producto.setAlignment(Element.ALIGN_LEFT);
            documento.add(producto);
        }
        
        // Línea separadora
        Paragraph lineaSeparadora3 = new Paragraph("--------------------------", fuenteNormal);
        lineaSeparadora3.setAlignment(Element.ALIGN_CENTER);
        documento.add(lineaSeparadora3);
        
        // Totales
        Paragraph totales = new Paragraph();
        totales.add(new Chunk("TOTAL:                 $" + String.format("%.0f", total), fuenteNegrita));
        totales.add(Chunk.NEWLINE);
        totales.add(new Chunk("EFECTIVO:              $" + String.format("%.0f", efectivo), fuenteNormal));
        totales.add(Chunk.NEWLINE);
        totales.add(new Chunk("CAMBIO:                 $" + String.format("%.0f", cambio), fuenteNormal));
        totales.setAlignment(Element.ALIGN_LEFT);
        documento.add(totales);
        
        // Línea inferior
        Paragraph lineaInferior = new Paragraph("==========================", fuenteNormal);
        lineaInferior.setAlignment(Element.ALIGN_CENTER);
        documento.add(lineaInferior);
        
        // Mensaje de agradecimiento
        Paragraph agradecimiento = new Paragraph("   ¡GRACIAS POR SU COMPRA!", fuenteNegrita);
        agradecimiento.setAlignment(Element.ALIGN_CENTER);
        documento.add(agradecimiento);
        
        // Línea final
        Paragraph lineaFinal = new Paragraph("==========================", fuenteNormal);
        lineaFinal.setAlignment(Element.ALIGN_CENTER);
        documento.add(lineaFinal);
    }
    
    private static String obtenerNombreProducto(int idProducto) {
        // Crear instancia del DAO para obtener el nombre del producto
        VentasDAO ventasDAO = new VentasDAO();
        return ventasDAO.obtenerNombreProducto(idProducto);
    }
}
