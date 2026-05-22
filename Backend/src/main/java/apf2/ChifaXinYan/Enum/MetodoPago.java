package apf2.ChifaXinYan.Enum;

<<<<<<< HEAD

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
=======
public enum MetodoPago {
    EFECTIVO,
    TARJETA,
    YAPE,
    PLIN;

    public static MetodoPago fromString(String metodoPago) {
        if (metodoPago == null) {
            throw new IllegalArgumentException("Método de pago inválido");
        }
        try {
            return MetodoPago.valueOf(metodoPago.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Método de pago inválido: " + metodoPago);
        }
>>>>>>> 0a71042947e863f58f51a24e38f758253f8febe9
    }
}