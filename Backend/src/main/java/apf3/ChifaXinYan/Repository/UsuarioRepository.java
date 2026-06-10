package apf3.ChifaXinYan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import apf3.ChifaXinYan.Model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNombre(String nombre);

    List<Usuario> findByRol(String rol);

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> buscarPorEmailJPQL(@Param("email") String email);

    long countByRol(String rol);
}