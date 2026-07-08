package apf3.ChifaXinYan.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import apf3.ChifaXinYan.Model.Usuario;
import apf3.ChifaXinYan.Repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario(null, "Josue Chavez", "josue.mozo@salonxinyan.com", passwordEncoder.encode("Josue_mozo"), "MOZO"));
            usuarioRepository.save(new Usuario(null, "Elsa Ramirez", "elsa.cocina@salonxinyan.com", passwordEncoder.encode("elsa_cocina"), "COCINA"));
            usuarioRepository.save(new Usuario(null, "Admin Xin Yan", "admin@salonxinyan.com", passwordEncoder.encode("Admin_xin_yan"), "ADMIN"));
            usuarioRepository.save(new Usuario(null, "Gael Vasquez", "gael.mozo@salonxinyan.com", passwordEncoder.encode("gael_mozo"), "MOZO"));
            usuarioRepository.save(new Usuario(null, "Andrea Arrunategui", "andrea.cocina@salonxinyan.com", passwordEncoder.encode("andrea_cocina"), "COCINA"));
            usuarioRepository.save(new Usuario(null, "Cajero Xin Yan", "cajero@salonxinyan.com", passwordEncoder.encode("cajero123"), "CAJERO"));
            System.out.println("✅ Datos iniciales cargados en PostgreSQL");
        }
    }
}