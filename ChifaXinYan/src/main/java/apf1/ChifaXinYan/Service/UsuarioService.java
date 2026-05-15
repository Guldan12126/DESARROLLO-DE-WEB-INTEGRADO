package apf1.ChifaXinYan.Service;

import apf1.ChifaXinYan.Model.Usuario;
import apf1.ChifaXinYan.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario crearUsuario(Usuario usuario) {
        // Encripta la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(existente -> {
            existente.setNombre(usuarioActualizado.getNombre());
            existente.setEmail(usuarioActualizado.getEmail());
            existente.setRol(usuarioActualizado.getRol());
            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
                existente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
            }
            return usuarioRepository.save(existente);
        });
    }

    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
