package Model;

public class ProductoStockBajo {
    private String nombre;
    private int stockActual;
    private int stockMinimo;
    
    public ProductoStockBajo() {}
    
    public ProductoStockBajo(String nombre, int stockActual, int stockMinimo) {
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
    
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
}