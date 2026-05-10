package apf2.ChifaXinYan.Enum;

public enum EstadoPedido {
    PENDIENTE("Pendiente - Recién registrado, esperando enviar a cocina"),
    EN_PREPARACION("En preparación - Cocina está preparando el pedido"),
    LISTO("Listo - Pedido terminado, esperando mozo"),
    ENTREGADO("Entregado - Cliente ya recibió su pedido"),
    PAGADO("Pagado - Cliente ya canceló"),
    ANULADO("Anulado - Pedido cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static EstadoPedido fromString(String estado) {
        for (EstadoPedido e : EstadoPedido.values()) {
            if (e.name().equalsIgnoreCase(estado)) {
                return e;
            }
        }
        return PENDIENTE;
    }
    
    public static EstadoPedido[] getEstadosActivos() {
        return new EstadoPedido[]{PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO};
    }
}