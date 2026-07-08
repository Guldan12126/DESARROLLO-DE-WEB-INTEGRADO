package apf1.ChifaXinYan.Service;

import apf1.ChifaXinYan.Model.Usuario;
import apf1.ChifaXinYan.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Servicio que conecta Spring Security con la base de datos.
 * Spring Security llama a loadUserByUsername() cada vez que un usuario intenta autenticarse.
 * Aquí buscamos al usuario por email en la BD y devolvemos sus credenciales + rol.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + email));

        // Spring Security requiere el rol con prefijo "ROLE_"
        // Ejemplo: "ADMIN" → "ROLE_ADMIN", "MOZO" → "ROLE_MOZO"
        SimpleGrantedAuthority autoridad = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())  // ya está encriptado con BCrypt
                .authorities(Collections.singletonList(autoridad))
                .build();
    }
}
