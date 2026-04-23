package apf1.ChifaXinYan.Service;

import apf1.ChifaXinYan.Model.Mesa;
import apf1.ChifaXinYan.Repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public List<Mesa> listarTodas() {
        return mesaRepository.listarTodas();
    }

    public List<Mesa> listarDisponibles() {
        return mesaRepository.listarPorEstado("LIBRE");
    }

    public Mesa obtenerPorId(Long id) {
        return mesaRepository.buscarPorId(id);
    }

    public Mesa crearMesa(Mesa mesa) {
        mesa.setEstado("LIBRE");
        return mesaRepository.guardar(mesa);
    }

    public Mesa actualizarEstado(Long id, String estado) {
        Mesa mesa = mesaRepository.buscarPorId(id);
        if (mesa != null) {
            mesa.setEstado(estado);
            return mesaRepository.guardar(mesa);
        }
        return null;
    }

    public boolean eliminarMesa(Long id) {
        Mesa mesa = mesaRepository.buscarPorId(id);
        if (mesa != null) {
            mesaRepository.eliminar(id);
            return true;
        }
        return false;
    }
}
