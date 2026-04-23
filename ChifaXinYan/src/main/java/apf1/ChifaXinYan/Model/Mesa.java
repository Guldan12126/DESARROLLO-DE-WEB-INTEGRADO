package apf1.ChifaXinYan.Model;

public class Mesa {
    private Long id;
    private int numero;
    private String estado; 
    private int capacidad;
    
    // Constructor vacío
    public Mesa() {}
    
    // Constructor con parámetros
    public Mesa(Long id, int numero, String estado, int capacidad) {
        this.id = id;
        this.numero = numero;
        this.estado = estado;
        this.capacidad = capacidad;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
}
