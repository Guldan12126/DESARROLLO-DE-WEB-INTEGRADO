package apf2.ChifaXinYan.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_producto", nullable = false)
    private Long idProducto;
    
    @Column(nullable = false)
    private String nombreProducto;
    
    @Column(nullable = false)
    private int cantidad;
    
    @Column(name = "precio_unitario", nullable = false)
    private double precioUnitario;
    
    private double subtotal;
    
    public DetallePedido() {}
    
    public DetallePedido(Long idProducto, String nombreProducto, int cantidad, double precioUnitario) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad;
        this.subtotal = this.cantidad * this.precioUnitario;
    }
    
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { 
        this.precioUnitario = precioUnitario;
        this.subtotal = this.cantidad * this.precioUnitario;
    }
    
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}