package apf2.ChifaXinYan.Enum;


public enum EstadoMesa {
    DISPONIBLE("Disponible - Libre para nuevos clientes"),
    OCUPADA("Ocupada - Clientes atendiendo"),
    PENDIENTE_PAGO("Pendiente de pago - Cliente terminó de comer");

    private final String descripcion;

    EstadoMesa(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static EstadoMesa fromString(String estado) {
        for (EstadoMesa e : EstadoMesa.values()) {
            if (e.name().equalsIgnoreCase(estado)) {
                return e;
            }
        }
        return DISPONIBLE;
    }
}
