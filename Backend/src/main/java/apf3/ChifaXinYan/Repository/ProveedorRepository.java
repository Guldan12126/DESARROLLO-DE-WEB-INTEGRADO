package apf3.ChifaXinYan.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import apf3.ChifaXinYan.Model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByRuc(String ruc);
    Optional<Proveedor> findByNombre(String nombre);
}