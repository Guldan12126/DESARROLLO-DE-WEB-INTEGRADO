package apf2.ChifaXinYan.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf2.ChifaXinYan.Model.Usuario;
import apf2.ChifaXinYan.Repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional(rollbackFor = Exception.class)
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario existente = usuarioRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNombre(usuarioActualizado.getNombre());
            existente.setEmail(usuarioActualizado.getEmail());
            existente.setRol(usuarioActualizado.getRol());
            if (usuarioActualizado.getPassword() != null) {
                existente.setPassword(usuarioActualizado.getPassword());
            }
            return usuarioRepository.save(existente);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }
}