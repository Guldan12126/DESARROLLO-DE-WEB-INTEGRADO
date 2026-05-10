package apf2.ChifaXinYan.Service;

import apf2.ChifaXinYan.Model.DetallePedido;
import apf2.ChifaXinYan.Model.Pedido;
import apf2.ChifaXinYan.Model.Producto;
import apf2.ChifaXinYan.Repository.PedidoRepository;
import apf2.ChifaXinYan.Repository.ProductoRepository;
import apf2.ChifaXinYan.Enum.EstadoPedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorMesa(Long idMesa) {
        return pedidoRepository.findByIdMesa(idMesa);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosActivos() {
        return pedidoRepository.findPedidosActivos();
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido crearPedido(Pedido pedido) {
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaPedido(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido agregarDetalle(Long pedidoId, DetallePedido detalle) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido != null) {
            Producto producto = productoRepository.findById(detalle.getIdProducto()).orElse(null);
            if (producto != null) {
                detalle.setNombreProducto(producto.getNombre());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setSubtotal(detalle.getCantidad() * producto.getPrecio());
                
                // Reducir stock
                producto.setStock(producto.getStock() - detalle.getCantidad());
                productoRepository.save(producto);
            }
            
            pedido.getDetalles().add(detalle);
            pedido.calcularTotal();
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido actualizarEstado(Long pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido != null) {
            pedido.setEstado(EstadoPedido.valueOf(nuevoEstado));
            if (nuevoEstado.equals(EstadoPedido.ENTREGADO.name())) {
                pedido.setFechaEntrega(LocalDateTime.now());
            }
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarPedido(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}