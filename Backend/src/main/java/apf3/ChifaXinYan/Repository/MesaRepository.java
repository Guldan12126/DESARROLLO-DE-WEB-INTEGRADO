package apf3.ChifaXinYan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Enum.EstadoMesa;
import apf3.ChifaXinYan.Model.Mesa;

public interface MesaRepository extends JpaRepository<Mesa, Long> {

    // Buscar mesas activas
    List<Mesa> findByActivoTrue();

    // Buscar por estado
    List<Mesa> findByEstado(EstadoMesa estado);

    // Buscar mesas disponibles por estado
    List<Mesa> findByEstadoAndActivoTrue(EstadoMesa estado);

    // Buscar por número de mesa
    Optional<Mesa> findByNumero(Integer numero);

    // Contar mesas por estado
    long countByEstado(EstadoMesa estado);

    // Actualizar estado de la mesa
    @Modifying
    @Transactional
    @Query("UPDATE Mesa m SET m.estado = :estado, m.pedidoActualId = :pedidoId WHERE m.id = :id")
    int actualizarEstadoMesa(
            @Param("id") Long id,
            @Param("estado") EstadoMesa estado,
            @Param("pedidoId") Long pedidoId);

    // Liberar mesa (poner disponible)
    @Modifying
    @Transactional
    @Query("UPDATE Mesa m SET m.estado = 'DISPONIBLE', m.pedidoActualId = NULL WHERE m.id = :id")
    int liberarMesa(@Param("id") Long id);
}