package Model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketPDFGeneratorSimple {
    
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
            
            // Crear documento PDF con tamaño personalizado para ticket térmico
            Rectangle ticketSize = new Rectangle(226, 400); // Ancho: 80mm, Alto: 140mm (tamaño ticket térmico)
            Document documento = new Document(ticketSize, 10, 10, 10, 10); // Márgenes pequeños
            PdfWriter.getInstance(documento, new FileOutputStream(rutaCompleta));
            
            documento.open();
            
            // Configurar fuente más pequeña para ticket térmico
            Font fuenteTitulo = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);
            Font fuenteNormal = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            Font fuenteNegrita = new Font(Font.FontFamily.COURIER, 8, Font.BOLD);
            
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
        Paragraph titulo = new Paragraph("SGVI - Sistema de Gestión\nde Ventas e Inventarios", fuenteTitulo);
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
            
            // Crear tabla simple para alineación correcta
            com.itextpdf.text.pdf.PdfPTable tablaProducto = new com.itextpdf.text.pdf.PdfPTable(2);
            tablaProducto.setWidthPercentage(100);
            tablaProducto.setWidths(new float[]{70, 30});
            
            // Celda izquierda: nombre y cantidad
            com.itextpdf.text.pdf.PdfPCell celdaIzquierda = new com.itextpdf.text.pdf.PdfPCell(
                new Paragraph(nombreProducto + " x" + detalle.getCantidad(), fuenteNormal));
            celdaIzquierda.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
            celdaIzquierda.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablaProducto.addCell(celdaIzquierda);
            
            // Celda derecha: precio
            com.itextpdf.text.pdf.PdfPCell celdaDerecha = new com.itextpdf.text.pdf.PdfPCell(
                new Paragraph("$" + String.format("%.0f", detalle.getSubtotal()), fuenteNormal));
            celdaDerecha.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
            celdaDerecha.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaProducto.addCell(celdaDerecha);
            
            documento.add(tablaProducto);
        }
        
        // Línea separadora
        Paragraph lineaSeparadora3 = new Paragraph("--------------------------", fuenteNormal);
        lineaSeparadora3.setAlignment(Element.ALIGN_CENTER);
        documento.add(lineaSeparadora3);
        
        // Totales con tabla para alineación correcta
        com.itextpdf.text.pdf.PdfPTable tablaTotales = new com.itextpdf.text.pdf.PdfPTable(2);
        tablaTotales.setWidthPercentage(100);
        tablaTotales.setWidths(new float[]{70, 30});
        
        // TOTAL
        com.itextpdf.text.pdf.PdfPCell celdaTotalLabel = new com.itextpdf.text.pdf.PdfPCell(
            new Paragraph("TOTAL:", fuenteNegrita));
        celdaTotalLabel.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        celdaTotalLabel.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaTotales.addCell(celdaTotalLabel);
        
        com.itextpdf.text.pdf.PdfPCell celdaTotalValor = new com.itextpdf.text.pdf.PdfPCell(
            new Paragraph("$" + String.format("%.0f", total), fuenteNegrita));
        celdaTotalValor.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        celdaTotalValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(celdaTotalValor);
        
        // EFECTIVO
        com.itextpdf.text.pdf.PdfPCell celdaEfectivoLabel = new com.itextpdf.text.pdf.PdfPCell(
            new Paragraph("EFECTIVO:", fuenteNormal));
        celdaEfectivoLabel.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        celdaEfectivoLabel.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaTotales.addCell(celdaEfectivoLabel);
        
        com.itextpdf.text.pdf.PdfPCell celdaEfectivoValor = new com.itextpdf.text.pdf.PdfPCell(
            new Paragraph("$" + String.format("%.0f", efectivo), fuenteNormal));
        celdaEfectivoValor.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        celdaEfectivoValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(celdaEfectivoValor);
        
        // CAMBIO
        com.itextpdf.text.pdf.PdfPCell celdaCambioLabel = new com.itextpdf.text.pdf.PdfPCell(
            new Paragraph("CAMBIO:", fuenteNormal));
        celdaCambioLabel.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        celdaCambioLabel.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaTotales.addCell(celdaCambioLabel);
        
        com.itextpdf.text.pdf.PdfPCell celdaCambioValor = new com.itextpdf.text.pdf.PdfPCell(
            new Paragraph("$" + String.format("%.0f", cambio), fuenteNormal));
        celdaCambioValor.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        celdaCambioValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.addCell(celdaCambioValor);
        
        documento.add(tablaTotales);
        
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
