package apf3.ChifaXinYan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apf3.ChifaXinYan.Model.MovimientoCaja;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {
    List<MovimientoCaja> findByCajaId(Long cajaId);
}
