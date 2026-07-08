package apf1.ChifaXinYan.Model;

public class Venta {
    private Long id;
    private Long idPedido;
    private double monto;
    private String metodoPago; 
    private String fecha;
    private String estado; 
    
    public Venta() {}
    
    public Venta(Long id, Long idPedido, double monto, String metodoPago, String fecha, String estado) {
        this.id = id;
        this.idPedido = idPedido;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fecha = fecha;
        this.estado = estado;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}