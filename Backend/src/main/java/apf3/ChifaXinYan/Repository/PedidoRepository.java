package apf3.ChifaXinYan.Repository;

import apf3.ChifaXinYan.Enum.EstadoPedido;
import apf3.ChifaXinYan.Model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository es opcional para interfaces que extienden JpaRepository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByMesaId(Long mesaId);
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByEstadoNot(EstadoPedido estado);
}