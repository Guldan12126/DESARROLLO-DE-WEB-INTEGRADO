package apf1.ChifaXinYan.Model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private Long idMesa;
    private List<DetallePedido> detalles;
    private String estado; // "PENDIENTE", "EN_COCINA", "LISTO", "ENTREGADO", "CANCELADO"
    private String fechaHora;
    private double total;
    
    public Pedido() {
        this.detalles = new ArrayList<>();
    }
    
    public Pedido(Long id, Long idMesa, String estado, String fechaHora) {
        this.id = id;
        this.idMesa = idMesa;
        this.estado = estado;
        this.fechaHora = fechaHora;
        this.detalles = new ArrayList<>();
        this.total = 0.0;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getIdMesa() { return idMesa; }
    public void setIdMesa(Long idMesa) { this.idMesa = idMesa; }
    
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    // Método para calcular total
    public void calcularTotal() {
        this.total = 0;
        for (DetallePedido detalle : detalles) {
            this.total += detalle.getSubtotal();
        }
    }
}