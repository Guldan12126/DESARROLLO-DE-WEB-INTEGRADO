package apf1.ChifaXinYan.Repository;

import apf1.ChifaXinYan.Model.Usuario;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UsuarioRepository {
    private final ConcurrentHashMap<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Datos iniciales
    public UsuarioRepository() {
        guardar(new Usuario(null, "Josue Chavez", "Josue.mozo@salonxinyan.com", "Josue_mozo", "MOZO"));
        guardar(new Usuario(null, "Elsa Ramirez", "Elsa.cocina@salonxinyan.com", "elsa_cocina", "COCINA"));
        guardar(new Usuario(null, "Admin Xin Yan", "admin@salonxinyan.com", "Admin_xin_yan", "ADMIN"));
        guardar(new Usuario(null, "Gael Vasquez", "pedro.mozo@salonxinyan.com", "pedro_mozo", "MOZO"));
        guardar(new Usuario(null, "Andrea Arrunategui", "andrea.cocina@salonxinyan.com", "andrea_cocina", "COCINA"));
    }

    public Usuario guardar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(idGenerator.getAndIncrement());
        }
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    public Usuario buscarPorId(Long id) {
        return usuarios.get(id);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarios.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    public List<Usuario> listarPorRol(String rol) {
        return usuarios.values().stream()
                .filter(u -> u.getRol().equals(rol))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void eliminar(Long id) {
        usuarios.remove(id);
    }
}
