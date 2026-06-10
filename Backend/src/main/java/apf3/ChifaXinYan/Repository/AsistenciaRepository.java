package apf3.ChifaXinYan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apf3.ChifaXinYan.Model.Asistencia;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
}