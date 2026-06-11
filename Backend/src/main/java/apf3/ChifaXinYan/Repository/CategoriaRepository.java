package apf3.ChifaXinYan.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import apf3.ChifaXinYan.Model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
}
