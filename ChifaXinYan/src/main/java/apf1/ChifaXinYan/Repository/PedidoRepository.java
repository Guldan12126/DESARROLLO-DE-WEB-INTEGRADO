package apf1.ChifaXinYan.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import apf1.ChifaXinYan.Model.Pedido;

@Repository
public class PedidoRepository {
    private final ConcurrentHashMap<Long, Pedido> pedidos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Pedido guardar(Pedido pedido) {
        if (pedido.getId() == null) {
            pedido.setId(idGenerator.getAndIncrement());
        }
        pedidos.put(pedido.getId(), pedido);
        return pedido;
    }

    public Pedido buscarPorId(Long id) {
        return pedidos.get(id);
    }

    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos.values());
    }

    public List<Pedido> listarPorEstado(String estado) {
        return pedidos.values().stream()
                .filter(p -> p.getEstado().equals(estado))
                .collect(Collectors.toList());
    }

    public List<Pedido> listarPorMesa(Long idMesa) {
        return pedidos.values().stream()
                .filter(p -> p.getIdMesa().equals(idMesa))
                .collect(Collectors.toList());
    }

    public List<Pedido> listarPedidosActivos() {
        return pedidos.values().stream()
                .filter(p -> !p.getEstado().equals("ENTREGADO") && !p.getEstado().equals("CANCELADO"))
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        pedidos.remove(id);
    }
}