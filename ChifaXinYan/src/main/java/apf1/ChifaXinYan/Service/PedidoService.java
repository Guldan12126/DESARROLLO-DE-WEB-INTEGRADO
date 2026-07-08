package apf1.ChifaXinYan.Service;

import apf1.ChifaXinYan.Model.DetallePedido;
import apf1.ChifaXinYan.Model.Pedido;
import apf1.ChifaXinYan.Model.Producto;
import apf1.ChifaXinYan.Repository.PedidoRepository;
import apf1.ChifaXinYan.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Pedido crearPedido(Pedido pedido) {
        pedido.setEstado("PENDIENTE");
        pedido.setFechaHora(LocalDateTime.now().format(formatter));
        pedido.calcularTotal();
        return pedidoRepository.guardar(pedido);
    }

    public Pedido agregarDetalle(Long idPedido, DetallePedido detalle) {
        Pedido pedido = pedidoRepository.buscarPorId(idPedido);
        if (pedido != null) {
            // Verificar stock
            Producto producto = productoRepository.buscarPorId(detalle.getIdProducto());
            if (producto != null && producto.getStock() >= detalle.getCantidad()) {
                // Descontar stock
                producto.setStock(producto.getStock() - detalle.getCantidad());
                productoRepository.guardar(producto);
                
                // Agregar detalle
                detalle.setNombreProducto(producto.getNombre());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setSubtotal(detalle.getCantidad() * producto.getPrecio());
                pedido.getDetalles().add(detalle);
                pedido.calcularTotal();
                return pedidoRepository.guardar(pedido);
            }
        }
        return null;
    }

    public Pedido actualizarEstado(Long id, String estado) {
        Pedido pedido = pedidoRepository.buscarPorId(id);
        if (pedido != null) {
            pedido.setEstado(estado);
            return pedidoRepository.guardar(pedido);
        }
        return null;
    }

    public List<Pedido> listarPedidosPorEstado(String estado) {
        if (estado == null || estado.isEmpty()) {
            return pedidoRepository.listarTodos();
        }
        return pedidoRepository.listarPorEstado(estado);
    }

    public List<Pedido> listarPedidosActivos() {
        return pedidoRepository.listarPedidosActivos();
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.buscarPorId(id);
    }

    public boolean cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.buscarPorId(id);
        if (pedido != null && !pedido.getEstado().equals("ENTREGADO")) {
            // Devolver stock
            for (DetallePedido detalle : pedido.getDetalles()) {
                Producto producto = productoRepository.buscarPorId(detalle.getIdProducto());
                if (producto != null) {
                    producto.setStock(producto.getStock() + detalle.getCantidad());
                    productoRepository.guardar(producto);
                }
            }
            pedido.setEstado("CANCELADO");
            pedidoRepository.guardar(pedido);
            return true;
        }
        return false;
    }
}