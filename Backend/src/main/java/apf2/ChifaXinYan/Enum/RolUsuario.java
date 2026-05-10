package apf2.ChifaXinYan.Enum;

public enum RolUsuario {
    ADMIN("Administrador"),
    MOZO("Mozo / Mesero"),
    COCINA("Personal de Cocina"),
    CAJERO("Cajero / Tesorero");

    private final String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static RolUsuario fromString(String rol) {
        for (RolUsuario r : RolUsuario.values()) {
            if (r.name().equalsIgnoreCase(rol)) {
                return r;
            }
        }
        return null;
    }
}