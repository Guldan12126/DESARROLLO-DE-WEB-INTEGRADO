package apf3.ChifaXinYan.Repository;

import apf3.ChifaXinYan.Model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
}