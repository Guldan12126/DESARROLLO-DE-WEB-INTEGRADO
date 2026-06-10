package apf3.ChifaXinYan.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Model.Reserva;
import apf3.ChifaXinYan.Repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Reserva crearReserva(Reserva reserva) {
        // Por defecto una reserva nueva entra como PENDIENTE
        if (reserva.getEstado() == null) {
            reserva.setEstado("PENDIENTE");
        }
        return reservaRepository.save(reserva);
    }

    @Transactional(rollbackFor = Exception.class)
    public Reserva actualizarEstado(Long id, String nuevoEstado) {
        Reserva reserva = obtenerPorId(id);
        if (reserva != null) {
            reserva.setEstado(nuevoEstado);
            return reservaRepository.save(reserva);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarReserva(Long id) {
        if (reservaRepository.existsById(id)) {
            reservaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}