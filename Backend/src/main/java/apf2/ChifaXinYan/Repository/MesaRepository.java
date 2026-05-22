package apf2.ChifaXinYan.Repository;

import java.util.List;
<<<<<<< HEAD

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
=======
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
>>>>>>> 0a71042947e863f58f51a24e38f758253f8febe9

import apf2.ChifaXinYan.Enum.EstadoMesa;
import apf2.ChifaXinYan.Model.Mesa;

<<<<<<< HEAD
@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    
    // Buscar por estado
    List<Mesa> findByEstado(EstadoMesa estado);
    
    // Buscar mesas disponibles
    List<Mesa> findByEstadoAndActivoTrue(EstadoMesa estado);
    
    // Buscar por número de mesa
    Mesa findByNumero(Integer numero);
    
    // Buscar mesas activas
    List<Mesa> findByActivoTrue();
    
    // Contar mesas por estado
    long countByEstado(EstadoMesa estado);
    
    // Actualizar estado de la mesa
    @Modifying
    @Transactional
    @Query("UPDATE Mesa m SET m.estado = :estado, m.pedidoActualId = :pedidoId WHERE m.id = :id")
    int actualizarEstadoMesa(@Param("id") Long id, @Param("estado") EstadoMesa estado, @Param("pedidoId") Long pedidoId);
    
    // Liberar mesa (poner disponible)
    @Modifying
    @Transactional
    @Query("UPDATE Mesa m SET m.estado = 'DISPONIBLE', m.pedidoActualId = NULL WHERE m.id = :id")
    int liberarMesa(@Param("id") Long id);
=======
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    List<Mesa> findByActivoTrue();

    List<Mesa> findByEstado(EstadoMesa estado);

    List<Mesa> findByEstadoAndActivoTrue(EstadoMesa estado);

    Optional<Mesa> findByNumero(Integer numero);

    long countByEstado(EstadoMesa estado);
>>>>>>> 0a71042947e863f58f51a24e38f758253f8febe9
}