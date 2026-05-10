package apf2.ChifaXinYan.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 50)
    private String categoria; 
    
    @Column(nullable = false)
    private double precio;
    
    @Column(nullable = false)
    private int stock;
    
    @Column(length = 500)
    private String descripcion;
    
    private String imagenUrl;
    
    @Column(nullable = false)
    private boolean activo = true;  
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;  
    
    // Constructores
    public Producto() {
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }
    
    public Producto(Long id, String nombre, String categoria, double precio, int stock, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }
    
    // Constructor completo
    public Producto(Long id, String nombre, String categoria, double precio, int stock, String imagenUrl, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.descripcion = descripcion;
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}