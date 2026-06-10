package apf3.ChifaXinYan.Repository;

import apf3.ChifaXinYan.Model.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    Optional<Caja> findByEstado(String estado); // Para encontrar la caja ABIERTA
}