package apf2.ChifaXinYan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apf2.ChifaXinYan.Model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByRol(String rol);
    
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> buscarPorEmailJPQL(@Param("email") String email);
    
    long countByRol(String rol);
}