package apf1.ChifaXinYan.Config;

import apf1.ChifaXinYan.Model.Usuario;
import apf1.ChifaXinYan.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Se ejecuta automáticamente al iniciar la aplicación.
 * Crea los usuarios iniciales en la base de datos con contraseñas encriptadas con BCrypt.
 *
 * IMPORTANTE: Las contraseñas nunca se almacenan en texto plano.
 * BCrypt las transforma en un hash seguro antes de guardarlas en la BD.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Solo inserta datos si la tabla está vacía (evita duplicados al reiniciar)
        if (usuarioRepository.count() == 0) {
            crearUsuario("Josue Chavez",        "Josue.mozo@salonxinyan.com",    "Josue_mozo",     "MOZO");
            crearUsuario("Elsa Ramirez",         "Elsa.cocina@salonxinyan.com",   "elsa_cocina",    "COCINA");
            crearUsuario("Admin Xin Yan",        "admin@salonxinyan.com",         "Admin_xin_yan",  "ADMIN");
            crearUsuario("Gael Vasquez",         "pedro.mozo@salonxinyan.com",    "pedro_mozo",     "MOZO");
            crearUsuario("Andrea Arrunategui",   "andrea.cocina@salonxinyan.com", "andrea_cocina",  "COCINA");

            System.out.println("==============================================");
            System.out.println("  Usuarios iniciales creados en la base de datos");
            System.out.println("  Contraseñas encriptadas con BCrypt");
            System.out.println("==============================================");
        }
    }

    private void crearUsuario(String nombre, String email, String passwordPlano, String rol) {
        // passwordEncoder.encode() convierte "texto_plano" → "$2a$10$hash..."
        String passwordEncriptado = passwordEncoder.encode(passwordPlano);
        usuarioRepository.save(new Usuario(nombre, email, passwordEncriptado, rol));
    }
}
