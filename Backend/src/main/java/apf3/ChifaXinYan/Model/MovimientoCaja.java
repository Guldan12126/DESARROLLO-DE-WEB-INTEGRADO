package apf3.ChifaXinYan.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimientos_caja")
public class MovimientoCaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "caja_id", nullable = false)
    private Caja caja;

    @Column(nullable = false)
    private String tipo; // INGRESO, EGRESO

    @Column(nullable = false)
    private double monto;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "proveedor_id") 
    private Proveedor proveedor;

    public MovimientoCaja() {
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Caja getCaja() { return caja; }
    public void setCaja(Caja caja) { this.caja = caja; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
}