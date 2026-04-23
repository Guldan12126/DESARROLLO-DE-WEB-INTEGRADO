package apf1.ChifaXinYan.Service;

import apf1.ChifaXinYan.Model.Usuario;
import apf1.ChifaXinYan.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    public List<Usuario> listarPorRol(String rol) {
        return usuarioRepository.listarPorRol(rol);
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.buscarPorId(id);
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.guardar(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario existente = usuarioRepository.buscarPorId(id);
        if (existente != null) {
            existente.setNombre(usuarioActualizado.getNombre());
            existente.setEmail(usuarioActualizado.getEmail());
            existente.setRol(usuarioActualizado.getRol());
            if (usuarioActualizado.getPassword() != null) {
                existente.setPassword(usuarioActualizado.getPassword());
            }
            return usuarioRepository.guardar(existente);
        }
        return null;
    }

    public boolean eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario != null) {
            usuarioRepository.eliminar(id);
            return true;
        }
        return false;
    }

    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }
}
