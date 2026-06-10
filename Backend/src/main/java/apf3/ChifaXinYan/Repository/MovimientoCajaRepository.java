package apf3.ChifaXinYan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apf3.ChifaXinYan.Model.MovimientoCaja;

@Repository
public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {
    List<MovimientoCaja> findByCajaId(Long cajaId);
}
