package apf3.ChifaXinYan.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import apf3.ChifaXinYan.Model.Caja;

public interface CajaRepository extends JpaRepository<Caja, Long> {
    Optional<Caja> findByEstado(String estado); // Para encontrar la caja ABIERTA
}