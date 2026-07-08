package apf3.ChifaXinYan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import apf3.ChifaXinYan.Model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}