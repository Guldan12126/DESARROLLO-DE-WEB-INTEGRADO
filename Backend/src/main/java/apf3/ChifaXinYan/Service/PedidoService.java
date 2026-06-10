package apf3.ChifaXinYan.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Enum.EstadoPedido;
import apf3.ChifaXinYan.Model.DetallePedido;
import apf3.ChifaXinYan.Model.Pedido;
import apf3.ChifaXinYan.Model.Producto;
import apf3.ChifaXinYan.Repository.PedidoRepository;
import apf3.ChifaXinYan.Repository.ProductoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Pedido con ID " + id + " no encontrado."));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorMesa(Long idMesa) {
        return pedidoRepository.findByMesaId(idMesa);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosActivos() {
        // Es mejor filtrar por los estados que realmente requieren atención
        return listarTodos().stream()
                .filter(p -> p.getEstado() != EstadoPedido.PAGADO)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido crearPedido(Pedido pedido) {
        if (pedido.getMesa() == null) {
            throw new RuntimeException("Error: No se puede crear un pedido sin asignar una mesa.");
        }
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaPedido(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido agregarDetalle(Long pedidoId, Long productoId, int cantidad) {
        Pedido pedido = obtenerPorId(pedidoId);
        
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Error: El producto no existe."));

        // VALIDACIÓN: Verificar disponibilidad de stock
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Error: Stock insuficiente para '" + producto.getNombre() + 
                                     "'. Disponible: " + producto.getStock());
        }

        // Crear y configurar el detalle
        DetallePedido detalle = new DetallePedido(pedido, producto, cantidad, producto.getPrecio());
        
        // Lógica de negocio: Reducir stock del producto
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
        
        pedido.getDetalles().add(detalle);
        pedido.calcularTotal();
        
        return pedidoRepository.save(pedido);
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido actualizarEstado(Long pedidoId, String nuevoEstado) {
        Pedido pedido = obtenerPorId(pedidoId);
        
        try {
            EstadoPedido estado = EstadoPedido.valueOf(nuevoEstado.toUpperCase());
            pedido.setEstado(estado);
            return pedidoRepository.save(pedido);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error: El estado '" + nuevoEstado + "' no es un estado de pedido válido.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarPedido(Long id) {
        if (pedidoRepository.existsById(id)) {
            // Nota: Aquí deberías considerar si quieres devolver el stock de los productos al cancelar
            pedidoRepository.deleteById(id);
            return true;
        }
        throw new RuntimeException("Error: No se puede eliminar. Pedido no encontrado.");
    }
}