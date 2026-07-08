package apf3.ChifaXinYan.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import apf3.ChifaXinYan.Dto.NotificacionDto;
import apf3.ChifaXinYan.Enum.EstadoPedido;
import apf3.ChifaXinYan.Model.Pedido;
import apf3.ChifaXinYan.Model.Producto;

@Service
public class NotificacionService {

    private static final int LIMITE_STOCK_CRITICO = 10;
    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final Map<String, LocalDateTime> ultimaLecturaPorRol = new ConcurrentHashMap<>();

    public NotificacionService(ProductoService productoService, PedidoService pedidoService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
    }

    public List<NotificacionDto> listarPorRol(String rol) {
        String rolNormalizado = normalizarRol(rol);
        LocalDateTime ultimaLectura = ultimaLecturaPorRol.getOrDefault(rolNormalizado, LocalDateTime.MIN);

        return construirNotificaciones(rolNormalizado).stream()
                .peek(notificacion -> notificacion.setLeida(!notificacion.getFecha().isAfter(ultimaLectura)))
                .sorted(Comparator.comparing(NotificacionDto::getFecha).reversed())
                .toList();
    }

    public List<NotificacionDto> listarNoLeidasPorRol(String rol) {
        return listarPorRol(rol).stream()
                .filter(notificacion -> !notificacion.isLeida())
                .toList();
    }

    public void marcarComoLeidas(String rol) {
        ultimaLecturaPorRol.put(normalizarRol(rol), LocalDateTime.now());
    }

    private List<NotificacionDto> construirNotificaciones(String rol) {
        List<NotificacionDto> notificaciones = new ArrayList<>();

        if ("ADMIN".equals(rol)) {
            notificaciones.addAll(crearAlertasStock());
            notificaciones.addAll(crearAlertasPedidos(EstadoPedido.PENDIENTE, "Pedido pendiente", "warning", "COCINA"));
            notificaciones.addAll(crearAlertasPedidos(EstadoPedido.LISTO, "Pedido listo", "success", "MOZO"));
            notificaciones.addAll(crearAlertasPedidos(EstadoPedido.ENTREGADO, "Pago pendiente", "info", "CAJERO"));
        } else if ("COCINA".equals(rol)) {
            notificaciones.addAll(crearAlertasPedidos(EstadoPedido.PENDIENTE, "Nuevo pedido pendiente", "warning", rol));
        } else if ("MOZO".equals(rol)) {
            notificaciones.addAll(crearAlertasPedidos(EstadoPedido.LISTO, "Pedido listo para entregar", "success", rol));
        } else if ("CAJERO".equals(rol)) {
            notificaciones.addAll(crearAlertasPedidos(EstadoPedido.ENTREGADO, "Pedido listo para cobrar", "info", rol));
        }

        return notificaciones;
    }

    private List<NotificacionDto> crearAlertasStock() {
        return productoService.listarStockBajo(LIMITE_STOCK_CRITICO).stream()
                .map(producto -> new NotificacionDto(
                        "producto-" + producto.getId(),
                        "Stock critico",
                        "El producto '" + producto.getNombre() + "' tiene stock bajo (" + producto.getStock() + " unidades).",
                        "danger",
                        "ADMIN",
                        "PRODUCTO",
                        producto.getId(),
                        resolverFechaProducto(producto),
                        false))
                .toList();
    }

    private List<NotificacionDto> crearAlertasPedidos(EstadoPedido estado, String titulo, String tipo, String rolDestino) {
        return pedidoService.listarPorEstado(estado).stream()
                .map(pedido -> new NotificacionDto(
                        "pedido-" + estado.name().toLowerCase() + "-" + pedido.getId(),
                        titulo,
                        construirMensajePedido(pedido, estado),
                        tipo,
                        rolDestino,
                        "PEDIDO",
                        pedido.getId(),
                        resolverFechaPedido(pedido),
                        false))
                .toList();
    }

    private String construirMensajePedido(Pedido pedido, EstadoPedido estado) {
        Long mesaId = pedido.getIdMesa();
        String mesaTexto = mesaId != null ? "Mesa " + mesaId : "sin mesa";

        return switch (estado) {
            case PENDIENTE -> "El pedido #" + pedido.getId() + " de " + mesaTexto + " espera preparacion.";
            case LISTO -> "El pedido #" + pedido.getId() + " de " + mesaTexto + " ya esta listo para entregar.";
            case ENTREGADO -> "El pedido #" + pedido.getId() + " de " + mesaTexto + " quedo entregado y pendiente de cobro.";
            default -> "El pedido #" + pedido.getId() + " requiere atencion.";
        };
    }

    private LocalDateTime resolverFechaProducto(Producto producto) {
        return producto.getFechaRegistro() != null ? producto.getFechaRegistro() : LocalDateTime.now();
    }

    private LocalDateTime resolverFechaPedido(Pedido pedido) {
        return pedido.getFechaPedido() != null ? pedido.getFechaPedido() : LocalDateTime.now();
    }

    private String normalizarRol(String rol) {
        if (rol == null || rol.isBlank()) {
            throw new RuntimeException("El rol es obligatorio para consultar notificaciones.");
        }
        return rol.trim().toUpperCase();
    }
}
