package apf2.ChifaXinYan.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import apf2.ChifaXinYan.Enum.EstadoPedido;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_mesa", nullable = false)
    private Long idMesa;
    
    @Column(name = "id_mozo")
    private Long idMozo;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    private List<DetallePedido> detalles;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    
    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
    
    private double total = 0.0;
    
    public Pedido() {
        this.detalles = new ArrayList<>();
        this.fechaPedido = LocalDateTime.now();
    }
    
    public Pedido(Long idMesa, Long idMozo) {
        this.idMesa = idMesa;
        this.idMozo = idMozo;
        this.detalles = new ArrayList<>();
        this.estado = EstadoPedido.PENDIENTE;
        this.fechaPedido = LocalDateTime.now();
        this.total = 0.0;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getIdMesa() { return idMesa; }
    public void setIdMesa(Long idMesa) { this.idMesa = idMesa; }
    
    public Long getIdMozo() { return idMozo; }
    public void setIdMozo(Long idMozo) { this.idMozo = idMozo; }
    
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { 
        this.detalles = detalles;
        calcularTotal();
    }
    
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }
    
    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    
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