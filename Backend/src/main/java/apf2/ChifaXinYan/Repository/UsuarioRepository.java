package apf2.ChifaXinYan.Repository;

import java.util.List;
import java.util.Optional;
<<<<<<< HEAD

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
=======

import org.springframework.data.jpa.repository.JpaRepository;

import apf2.ChifaXinYan.Model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombre(String nombre);

    List<Usuario> findByRol(String rol);
>>>>>>> 0a71042947e863f58f51a24e38f758253f8febe9
}