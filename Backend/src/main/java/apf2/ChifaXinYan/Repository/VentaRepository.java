package apf2.ChifaXinYan.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apf2.ChifaXinYan.Enum.MetodoPago;
import apf2.ChifaXinYan.Model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    // Buscar ventas por fecha
    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Buscar ventas por método de pago
    List<Venta> findByMetodoPago(MetodoPago metodoPago);
    
    // Buscar ventas por cajero
    List<Venta> findByAtendidoPor(Long idCajero);
    
    // Obtener total de ventas en un rango de fechas
    @Query("SELECT SUM(v.monto) FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
    Double obtenerTotalVentasPorRango(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    // Contar ventas por método de pago
    long countByMetodoPago(MetodoPago metodoPago);
    
    // Buscar venta por pedido
    Venta findByPedidoId(Long pedidoId);
}