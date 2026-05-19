package apf2.ChifaXinYan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import apf2.ChifaXinYan.Enum.EstadoMesa;
import apf2.ChifaXinYan.Model.Mesa;

public interface MesaRepository extends JpaRepository<Mesa, Long> {

    List<Mesa> findByActivoTrue();

    List<Mesa> findByEstado(EstadoMesa estado);

    List<Mesa> findByEstadoAndActivoTrue(EstadoMesa estado);

    Optional<Mesa> findByNumero(Integer numero);

    long countByEstado(EstadoMesa estado);
}