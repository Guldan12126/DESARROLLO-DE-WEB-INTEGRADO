package apf2.ChifaXinYan.Model;

import java.time.LocalDateTime;

import apf2.ChifaXinYan.Enum.MetodoPago;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas")
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;  // Relación con el pedido
    
    @Column(nullable = false)
    private double monto;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Column(name = "monto_recibido")
    private double montoRecibido;  // Para efectivo
    
    private double vuelto; 
    
    @Column(name = "numero_comprobante")
    private String numeroComprobante;  
    
    @Column(name = "atendido_por")
    private Long atendidoPor; 
    
    public Venta() {
        this.fecha = LocalDateTime.now();
    }
    
    public Venta(Pedido pedido, MetodoPago metodoPago, double montoRecibido, Long atendidoPor) {
        this.pedido = pedido;
        this.monto = pedido.getTotal();
        this.metodoPago = metodoPago;
        this.montoRecibido = montoRecibido;
        this.fecha = LocalDateTime.now();
        this.atendidoPor = atendidoPor;
        
        // Calcular vuelto si es efectivo
        if (metodoPago == MetodoPago.EFECTIVO) {
            this.vuelto = montoRecibido - this.monto;
        } else {
            this.vuelto = 0;
        }
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    
    public double getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(double montoRecibido) { this.montoRecibido = montoRecibido; }
    
    public double getVuelto() { return vuelto; }
    public void setVuelto(double vuelto) { this.vuelto = vuelto; }
    
    public String getNumeroComprobante() { return numeroComprobante; }
    public void setNumeroComprobante(String numeroComprobante) { this.numeroComprobante = numeroComprobante; }
    
    public Long getAtendidoPor() { return atendidoPor; }
    public void setAtendidoPor(Long atendidoPor) { this.atendidoPor = atendidoPor; }
}