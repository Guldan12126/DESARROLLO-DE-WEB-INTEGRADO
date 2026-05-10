package apf2.ChifaXinYan.Model;

import apf2.ChifaXinYan.Enum.EstadoMesa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mesas")
public class Mesa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Integer numero;
    
    @Column(nullable = false)
    private Integer capacidad;
    
    private String ubicacion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMesa estado = EstadoMesa.DISPONIBLE;
    
    @Column(name = "pedido_actual_id")
    private Long pedidoActualId; 
    
    private boolean activo = true;
    
    public Mesa() {}
    
    public Mesa(Integer numero, Integer capacidad, String ubicacion) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.estado = EstadoMesa.DISPONIBLE;
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public EstadoMesa getEstado() { return estado; }
    public void setEstado(EstadoMesa estado) { this.estado = estado; }
    
    public Long getPedidoActualId() { return pedidoActualId; }
    public void setPedidoActualId(Long pedidoActualId) { this.pedidoActualId = pedidoActualId; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}