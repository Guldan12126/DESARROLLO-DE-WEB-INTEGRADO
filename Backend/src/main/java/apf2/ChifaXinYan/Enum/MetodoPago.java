package apf2.ChifaXinYan.Enum;

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
    }
}