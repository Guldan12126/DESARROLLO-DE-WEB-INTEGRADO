package apf3.ChifaXinYan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Busca productos por el nombre de su categoría asociada
    List<Producto> findByCategoria_Nombre(String nombreCategoria);
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Búsqueda combinada por nombre y categoría
    List<Producto> findByNombreContainingIgnoreCaseAndCategoria_Nombre(String nombre, String nombreCategoria);
    
    List<Producto> findByActivoTrue();
    
    List<Producto> findByStockLessThan(int stock);
    
    List<Producto> findByCategoria_NombreAndActivoTrue(String nombreCategoria);
    
    @Modifying
    @Transactional
    @Query("UPDATE Producto p SET p.stock = :nuevoStock WHERE p.id = :id")
    int actualizarStock(@Param("id") Long id, @Param("nuevoStock") int nuevoStock);
    
    long countByCategoria_Nombre(String nombreCategoria);
    
    boolean existsByNombre(String nombre);
}