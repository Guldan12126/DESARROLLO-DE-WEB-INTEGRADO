package apf3.ChifaXinYan.Dto;

import java.time.LocalDateTime;

public class NotificacionDto {

    private String id;
    private String titulo;
    private String mensaje;
    private String tipo;
    private String rolDestino;
    private String entidad;
    private Long entidadId;
    private LocalDateTime fecha;
    private boolean leida;

    public NotificacionDto() {
    }

    public NotificacionDto(
            String id,
            String titulo,
            String mensaje,
            String tipo,
            String rolDestino,
            String entidad,
            Long entidadId,
            LocalDateTime fecha,
            boolean leida) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.rolDestino = rolDestino;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.fecha = fecha;
        this.leida = leida;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRolDestino() {
        return rolDestino;
    }

    public void setRolDestino(String rolDestino) {
        this.rolDestino = rolDestino;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(Long entidadId) {
        this.entidadId = entidadId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}
