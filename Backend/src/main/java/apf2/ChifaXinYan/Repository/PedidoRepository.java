package apf2.ChifaXinYan.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import apf2.ChifaXinYan.Enum.EstadoPedido;
import apf2.ChifaXinYan.Model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Buscar por estado
    List<Pedido> findByEstado(EstadoPedido estado);
    
    // Buscar por mesa
    List<Pedido> findByIdMesa(Long idMesa);
    
    // Buscar pedidos activos (no entregados ni anulados)
    @Query("SELECT p FROM Pedido p WHERE p.estado NOT IN ('ENTREGADO', 'ANULADO')")
    List<Pedido> findPedidosActivos();
    
    // Buscar pedidos por rango de fechas
    List<Pedido> findByFechaPedidoBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Actualizar estado del pedido
    @Modifying
    @Transactional
    @Query("UPDATE Pedido p SET p.estado = :estado WHERE p.id = :id")
    int actualizarEstado(@Param("id") Long id, @Param("estado") EstadoPedido estado);
    
    // Contar pedidos por estado
    long countByEstado(EstadoPedido estado);
}