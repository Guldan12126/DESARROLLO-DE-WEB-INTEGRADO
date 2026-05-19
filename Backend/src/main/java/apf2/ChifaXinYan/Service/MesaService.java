package apf2.ChifaXinYan.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf2.ChifaXinYan.Enum.EstadoMesa;
import apf2.ChifaXinYan.Model.Mesa;
import apf2.ChifaXinYan.Repository.MesaRepository;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    @Transactional(readOnly = true)
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Mesa> listarActivas() {
        return mesaRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Mesa> listarDisponibles() {
        return mesaRepository.findByEstadoAndActivoTrue(EstadoMesa.DISPONIBLE);
    }

    @Transactional(readOnly = true)
    public List<Mesa> listarOcupadas() {
        return mesaRepository.findByEstado(EstadoMesa.OCUPADA);
    }

    @Transactional(readOnly = true)
    public List<Mesa> listarPendientePago() {
        return mesaRepository.findByEstado(EstadoMesa.PENDIENTE_PAGO);
    }

    @Transactional(readOnly = true)
    public Mesa obtenerPorId(Long id) {
        return mesaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Mesa obtenerPorNumero(Integer numero) {
        return mesaRepository.findByNumero(numero).orElse(null);
    }

    @Transactional(readOnly = true)
    public long contarDisponibles() {
        return mesaRepository.countByEstado(EstadoMesa.DISPONIBLE);
    }

    @Transactional(readOnly = true)
    public long contarOcupadas() {
        return mesaRepository.countByEstado(EstadoMesa.OCUPADA);
    }
    @Transactional(rollbackFor = Exception.class)
    public Mesa actualizarEstado(Long id, String estado) {
        Mesa mesa = mesaRepository.findById(id).orElse(null);
        if (mesa == null) {
            return null;
        }
        EstadoMesa nuevoEstado = EstadoMesa.fromString(estado);
        mesa.setEstado(nuevoEstado);
        return mesaRepository.save(mesa);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mesa crearMesa(Mesa mesa) {
        if (mesaRepository.findByNumero(mesa.getNumero()) != null) {
            throw new RuntimeException("Ya existe una mesa con el número: " + mesa.getNumero());
        }
        mesa.setEstado(EstadoMesa.DISPONIBLE);
        return mesaRepository.save(mesa);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mesa actualizarMesa(Long id, Mesa mesaActualizada) {
        Mesa existente = mesaRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNumero(mesaActualizada.getNumero());
            existente.setCapacidad(mesaActualizada.getCapacidad());
            existente.setUbicacion(mesaActualizada.getUbicacion());
            return mesaRepository.save(existente);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Mesa ocuparMesa(Long id, Long pedidoId) {
        Mesa mesa = mesaRepository.findById(id).orElse(null);
        if (mesa != null && mesa.getEstado() == EstadoMesa.DISPONIBLE) {
            mesa.setEstado(EstadoMesa.OCUPADA);
            mesa.setPedidoActualId(pedidoId);
            return mesaRepository.save(mesa);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Mesa marcarPendientePago(Long id) {
        Mesa mesa = mesaRepository.findById(id).orElse(null);
        if (mesa != null && mesa.getEstado() == EstadoMesa.OCUPADA) {
            mesa.setEstado(EstadoMesa.PENDIENTE_PAGO);
            return mesaRepository.save(mesa);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Mesa liberarMesa(Long id) {
        Mesa mesa = mesaRepository.findById(id).orElse(null);
        if (mesa != null) {
            mesa.setEstado(EstadoMesa.DISPONIBLE);
            mesa.setPedidoActualId(null);
            return mesaRepository.save(mesa);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarMesa(Long id) {
        if (mesaRepository.existsById(id)) {
            mesaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}