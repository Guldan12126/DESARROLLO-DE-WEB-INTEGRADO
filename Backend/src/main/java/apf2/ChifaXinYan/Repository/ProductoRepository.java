package apf2.ChifaXinYan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import apf2.ChifaXinYan.Model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByCategoria(String categoria);
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    List<Producto> findByActivoTrue();
    
    List<Producto> findByStockLessThan(int stock);
    
    List<Producto> findByCategoriaAndActivoTrue(String categoria);
    
    @Modifying
    @Transactional
    @Query("UPDATE Producto p SET p.stock = :nuevoStock WHERE p.id = :id")
    int actualizarStock(@Param("id") Long id, @Param("nuevoStock") int nuevoStock);
    
    long countByCategoria(String categoria);
    
    boolean existsByNombre(String nombre);
}