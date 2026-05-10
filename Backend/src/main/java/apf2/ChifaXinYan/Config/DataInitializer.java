package apf2.ChifaXinYan.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import apf2.ChifaXinYan.Model.Usuario;
import apf2.ChifaXinYan.Repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario(null, "Josue Chavez", "Josue.mozo@salonxinyan.com", "Josue_mozo", "MOZO"));
            usuarioRepository.save(new Usuario(null, "Elsa Ramirez", "Elsa.cocina@salonxinyan.com", "elsa_cocina", "COCINA"));
            usuarioRepository.save(new Usuario(null, "Admin Xin Yan", "admin@salonxinyan.com", "Admin_xin_yan", "ADMIN"));
            usuarioRepository.save(new Usuario(null, "Gael Vasquez", "gael.mozo@salonxinyan.com", "gael_mozo", "MOZO"));
            usuarioRepository.save(new Usuario(null, "Andrea Arrunategui", "andrea.cocina@salonxinyan.com", "andrea_cocina", "COCINA"));
            usuarioRepository.save(new Usuario(null, "Cajero Xin Yan", "cajero@salonxinyan.com", "cajero123", "CAJERO"));
            System.out.println("✅ Datos iniciales cargados en PostgreSQL");
        }
    }
}