package apf2.ChifaXinYan.Enum;


public enum MetodoPago {
    EFECTIVO("Pago en efectivo"),
    TARJETA("Pago con tarjeta (débito/crédito)"),
    YAPE("Yape - Pago digital"),
    PLIN("Plin - Pago digital"),
    TRANSFERENCIA("Transferencia bancaria");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static MetodoPago fromString(String metodo) {
        for (MetodoPago m : MetodoPago.values()) {
            if (m.name().equalsIgnoreCase(metodo)) {
                return m;
            }
        }
        return EFECTIVO;
    }
}