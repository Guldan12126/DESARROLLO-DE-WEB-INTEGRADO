package apf1.ChifaXinYan.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import apf1.ChifaXinYan.Model.Mesa;

@Repository
public class MesaRepository {
    private final ConcurrentHashMap<Long, Mesa> mesas = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Datos iniciales
    public MesaRepository() {
        // Crear 8 mesas de ejemplo
        guardar(new Mesa(null, 1, "LIBRE", 4));
        guardar(new Mesa(null, 2, "LIBRE", 4));
        guardar(new Mesa(null, 3, "LIBRE", 2));
        guardar(new Mesa(null, 4, "LIBRE", 6));
        guardar(new Mesa(null, 5, "LIBRE", 4));
        guardar(new Mesa(null, 6, "LIBRE", 2));
        guardar(new Mesa(null, 7, "LIBRE", 8));
        guardar(new Mesa(null, 8, "LIBRE", 4));
        guardar(new Mesa(null, 9, "LIBRE", 6));
        guardar(new Mesa(null, 10, "LIBRE", 8));
    }

    public Mesa guardar(Mesa mesa) {
        if (mesa.getId() == null) {
            mesa.setId(idGenerator.getAndIncrement());
        }
        mesas.put(mesa.getId(), mesa);
        return mesa;
    }

    public Mesa buscarPorId(Long id) {
        return mesas.get(id);
    }

    public Mesa buscarPorNumero(int numero) {
        return mesas.values().stream()
                .filter(m -> m.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }

    public List<Mesa> listarTodas() {
        return new ArrayList<>(mesas.values());
    }

    public List<Mesa> listarPorEstado(String estado) {
        return mesas.values().stream()
                .filter(m -> m.getEstado().equals(estado))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void eliminar(Long id) {
        mesas.remove(id);
    }
}
